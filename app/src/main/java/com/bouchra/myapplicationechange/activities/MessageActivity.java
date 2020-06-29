package com.bouchra.myapplicationechange.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.MessageAdapter;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Message;
import com.bouchra.myapplicationechange.notification.APIService;
import com.bouchra.myapplicationechange.notification.Client;
import com.bouchra.myapplicationechange.notification.Data;
import com.bouchra.myapplicationechange.notification.Response;
import com.bouchra.myapplicationechange.notification.Sender;
import com.bouchra.myapplicationechange.notification.Token;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;


public class MessageActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private TextView username;
    private ImageButton btn_send, btn_send_location, btn_send_image;
    private EditText txt_send;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private boolean notify = false;
    private PreferenceUtils preferenceUtils;
    private Intent intent;
    private FusedLocationProviderClient mFusedLocationClient;
    private DatabaseReference reference, r, l;
    private ArrayList<Message> mchat;
    private APIService apiService;
    private MessageAdapter messageAdapter;
    //image picked will be samed in this uri
    private Uri image_uri = null;

    //nous aide à identifier l'action de l'utilisateur avec quelle demande d'autorisatio

    //request
    //permission constants
    private static final int PERMISSION_ID = 44;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 200;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 300;
    private StorageReference mStorageRef;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 500;
    //permissions arry
    String[] cameraPermission;
    String[] storagePermissionWRITE;
    String[] storagePermissionREAD;
    String[] locationpermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images Messages");
        preferenceUtils = new PreferenceUtils(this);
        btn_send_location = findViewById(R.id.btn_send_location);
        toolbar = findViewById(R.id.tollbar);
        recyclerView = findViewById(R.id.recycle_view);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
        btn_send_image = findViewById(R.id.btn_send_image);
        mchat = new ArrayList<>();
        //init permission arrys
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissionWRITE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissionREAD = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        locationpermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());


        //layout ( linear layout) for recycleview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recycleview properties
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //create api service
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

// get user sender from intent
        intent = getIntent();
        String userid = intent.getStringExtra("user");
        reference = FirebaseDatabase.getInstance().getReference("Membre").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Membre membre = dataSnapshot.getValue(Membre.class);
                username.setText(membre.getNomMembre());
                Picasso.get().load(membre.getPhotoUser()).into(profile_image);
// get chat
                l = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (userid.hashCode())));
                l.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mchat.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);// getvalue  de lbrary gson yroh yhws yjbd direct child w ydirh f object message ila li jbdth mchikima classe yhbs
                            if (message.getIdreceiver().equals(preferenceUtils.getMember().getIdMembre()) && message.getIdsender().equals(userid) ||
                                    message.getIdreceiver().equals(userid) && message.getIdsender().equals(preferenceUtils.getMember().getIdMembre())) {
                                mchat.add(message);

                            }
                            messageAdapter = new MessageAdapter(MessageActivity.this, mchat, membre.getPhotoUser());
                            recyclerView.setAdapter(messageAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // send message
        btn_send.setOnClickListener(v -> {
            notify = true;
            String msg = txt_send.getText().toString();
            if (!msg.equals("")) {

                r = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (userid.hashCode())));

                Message chat = new Message();
                chat.setTextMessage(msg);
                chat.setIdreceiver(userid);
                chat.setIdsender(preferenceUtils.getMember().getIdMembre());
                chat.setNomsender(preferenceUtils.getMember().getNomMembre());
                chat.setDateMessage(new Date());
                chat.setIdMessage(String.valueOf(chat.getDateMessage().getTime()));
                r.child(String.valueOf(chat.getDateMessage().getTime())).setValue(chat).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Votre message a été soumise avec succès ", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, "Erreur !! ", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                Toast.makeText(this, "Vous ne pouvez pas envoyer un message vide !", Toast.LENGTH_SHORT).show();
            }
            txt_send.setText("");
            // send notification
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre());
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Membre user = dataSnapshot.getValue(Membre.class);
                    if (notify) {
                        senNotification(userid, user.getNomMembre(), msg);
                    }
                    notify = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        btn_send_location.setOnClickListener(v -> {
// utiliser l'API Fused Location Provider pour obtenir la position actuelle des utilisateurs
            // LocationManager ou FusedLocationClient*
            getLastLocation();
            /*
            La getLastLocation() méthode renvoie un Task que vous pouvez utiliser pour obtenir un Location objet avec les coordonnées de latitude et de longitude d'un emplacement géographique.
             L'objet de localisation peut se trouver nulldans les situations suivantes:
           Dans certains appareils, si vous désactivez la position et la rallumez, les informations de position enregistrées précédemment seront effacées.
           Si l'utilisateur n'a jamais activé la localisation avant d'utiliser votre application, cette fois, les informations de localisation précédentes seront également nulles.
            De plus, cette méthode vérifiera d'abord si notre autorisation est accordée ou non et si le paramètre de localisation est activé.*/
        });

        //get image from camera/ gallery on click
        btn_send_image.setOnClickListener(v1 -> {
//show image pick dalog
            showImagePickDialog();


        });
    }

    private void showImagePickDialog() {
        //option (camera,gallery) to show in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter une image a partir de :");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //camera clicked
                    pickFromCamera();
                }
                if (which == 1) {
                    pickFromGallery();
                }
            }
        });
        //crete and show dialog
        builder.create().show();
    }


    public void pickFromGallery() {

        if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {

            //intent to pick image from gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
        }

    }

    public void pickFromCamera() {
        if (!checkCameraPermission()) {
            resuestCameraPermission();
        } else if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {
            //intent to pick image from camera
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
        }

    }


    private boolean checkCameraPermission() {
        //check if camera permission is enabel or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultl = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && resultl;
    }

    private void resuestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkExternalStorageWritePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkExternalStorageREADPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void resuestExternalStorageWritePermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, storagePermissionWRITE, WRITE_EXTERNAL_STORAGE_REQUEST);

    }

    private void resuestExternalStorageREADPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, storagePermissionREAD, READ_EXTERNAL_STORAGE_REQUEST);

    }


    private void senNotification(final String userid, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(preferenceUtils.getMember().getIdMembre(), name + " : " + message, "Nouveau message", userid,R.mipmap.logo);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(MessageActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    txt_send.setText("https://www.google.com/maps/@" + location.getLatitude() + "," + location.getLongitude() + ",15z");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Activer l'emplacement", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {//enregistrera les informations de localisation lors de l'exécution.nouvelle demande de localisation

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);//indiquez à l'application à quelle fréquence vous avez besoin de la mise à jour de l'emplacement d'un intervalle de temps maximum
        mLocationRequest.setFastestInterval(0);//à minimum
        mLocationRequest.setNumUpdates(1);//permettra aux utilisateurs de mettre à jour l'emplacement en temps réel

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    //lorsque nous obtenons la mise à jour de l'emplacement, nous définissons les valeurs de latitude et de longitude
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            txt_send.setText("https://www.google.com/maps/@" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + ",15z");

        }
    };

    //Cette méthode nous dira si l'utilisateur nous autorise à accéder à ACCESS_COARSE_LOCATIONet ACCESS_FINE_LOCATION.
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //Cette méthode demandera nos autorisations nécessaires à l'utilisateur si elles ne sont pas déjà accordées.
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this, locationpermission, PERMISSION_ID);
    }

    //Cela vérifiera si l'utilisateur a activé l'emplacement à partir du paramètre,
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    //cette méthode est appelée lorsqu'un utilisateur autorise ou refuse nos autorisations demandées.
    // Cela nous aidera donc à aller de l'avant si les autorisations sont accordées.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      /*  if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  obtenir les informations de localisation
                getLastLocation();
            }
        }*/
        switch (requestCode) {
            case PERMISSION_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  obtenir les informations de localisation
                    getLastLocation();
                }

            }
            break;
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //both permission are garented
                        pickFromCamera();

                    } else {
                        // camera or gallery or both permission ware denied
                        Toast.makeText(this, "camera et storage permission sont necessaire !... ", Toast.LENGTH_SHORT).show();
                    }

                } else {


                }
            }
            break;
            case WRITE_EXTERNAL_STORAGE_REQUEST:
            case READ_EXTERNAL_STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //storage permission garented
                        pickFromGallery();

                    } else {
                        // camera or gallery or both permission ware denied
                        Toast.makeText(this, " storage permission  necessaire !... ", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == IMAGE_PICK_GALLERY_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    image_uri = Uri.parse(saveImage(bitmap));
                    if (image_uri != null) {
                        sendImageMessage();
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            image_uri = Uri.parse(saveImage(thumbnail));
            if (image_uri != null) {
                sendImageMessage();
            }

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    throw new IOException("Cant able to create file");
                }
            }
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void sendImageMessage() {
        //progress dialog
        notify = true;
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("envoyer ... ");
        progressDialog.show();
        try {
            InputStream stream = new FileInputStream(String.valueOf(image_uri));
            StorageReference ref = mStorageRef.child("messafges/" + UUID.randomUUID().toString());
            ref.putStream(stream)
                    .addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(task -> {

                                    progressDialog.dismiss();
                                    intent = getIntent();
                                    String userid = intent.getStringExtra("user");
                                    // add imahe uri and other data to databse
                                    r = FirebaseDatabase.getInstance().getReference("Message").child(String.valueOf((preferenceUtils.getMember().getIdMembre().hashCode()) + (userid.hashCode())));

                                    Message chat = new Message();
                                    chat.setTextMessage(String.valueOf(task));
                                    chat.setIdreceiver(userid);
                                    chat.setIdsender(preferenceUtils.getMember().getIdMembre());
                                    chat.setNomsender(preferenceUtils.getMember().getNomMembre());
                                    chat.setDateMessage(new Date());
                                    chat.setIdMessage(String.valueOf(chat.getDateMessage().getTime()));
                                    r.child(String.valueOf(chat.getDateMessage().getTime())).setValue(chat).addOnCompleteListener(taskk -> {
                                        if (taskk.isSuccessful()) {
                                            Toast.makeText(MessageActivity.this, "Votre message a été soumise avec succès", Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(MessageActivity.this, "Erreur !! ", Toast.LENGTH_LONG).show();
                                        }

                                    });
                                    ////send notification
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre());
                                    database.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Membre user = dataSnapshot.getValue(Membre.class);
                                            if (notify) {
                                                senNotification(userid, user.getNomMembre(), "sent you a photo ...");
                                            }
                                            notify = false;
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                        );
                    })
                    .addOnFailureListener(e -> Toast.makeText(MessageActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
