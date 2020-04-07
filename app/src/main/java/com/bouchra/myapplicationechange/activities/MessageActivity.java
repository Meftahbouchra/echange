package com.bouchra.myapplicationechange.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;


public class MessageActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private TextView username;
    private ImageButton btn_send, btn_send_location, btn_send_image;
    private EditText txt_send;
    private RecyclerView recyclerView;
    private int PERMISSION_ID = 44;//nous aide à identifier l'action de l'utilisateur avec quelle demande d'autorisation
    private boolean notify = false;
    private PreferenceUtils preferenceUtils;
    private Intent intent;
    private FusedLocationProviderClient mFusedLocationClient;
    private DatabaseReference reference, r, l;
    private ArrayList<Message> mchat;
    private APIService apiService;
    private MessageAdapter messageAdapter;

    ///////////////******************************* add a msg image
    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    //permissions arry
    String[] cameraPermission;
    String[] storagePermission;
    //image picked will be samed in this uri
    Uri image_uri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        preferenceUtils = new PreferenceUtils(this);
        btn_send_location = findViewById(R.id.btn_send_location);
        Toolbar toolbar = findViewById(R.id.tollbar);
        recyclerView = findViewById(R.id.recycle_view);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        txt_send = findViewById(R.id.txt_send);
        btn_send_image = findViewById(R.id.btn_send_image);
        //******************************************************************
        //init permission arrys
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


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


        intent = getIntent();
        String userid = intent.getStringExtra("user");
        reference = FirebaseDatabase.getInstance().getReference("Membre").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Membre membre = dataSnapshot.getValue(Membre.class);
                username.setText(membre.getNomMembre());
                Glide.with(MessageActivity.this).load(membre.getPhotoUser()).into(profile_image);
                mchat = new ArrayList<>();

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
        btn_send.setOnClickListener(v -> {
            notify = true;
            String msg = txt_send.getText().toString();
            if (!msg.equals("")) {//TextUtils.isEmpty(msg)

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
                Toast.makeText(this, "you can't send empty message", Toast.LENGTH_SHORT).show();
            }
            txt_send.setText("");
            //notification
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
        //click button  to omport image
        btn_send_image.setOnClickListener(v1 -> {
//show image pick dalog
            Toast.makeText(this, "send image lessage!!!!!!!!! rah 3ndi lfog ta3 les permission", Toast.LENGTH_SHORT).show();
        });
    }

    private void senNotification(final String userid, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(preferenceUtils.getMember().getIdMembre(), name + " : " + message, "Nouveau message", userid, R.drawable.user); // logo of application

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
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
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
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  obtenir les informations de localisation
                getLastLocation();
            }
        }
    }


}
