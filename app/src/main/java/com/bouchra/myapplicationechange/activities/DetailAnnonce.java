package com.bouchra.myapplicationechange.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Posts;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAnnonce extends AppCompatActivity {
    private RelativeLayout profileUser;
    private TextView tite;
    private TextView desc;
    private Annonce annonce;
    private CarouselView images;
    private TextView retour;
    private TextView time;
    private DatabaseReference ref;
    private TextView txtRetout;
    private TextView name_user;
    private TextView ville;
    private TextView commune;
    private LinearLayout position;
    private CircleImageView imgUser;
    private Button offre;
    private Dialog MyDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView sendMsg;
    private TextView shar_publication;
    private TextView etoiles_user;
    private int nbrComm = 0;
    private float totalRepos = 0;
    private String[] locationpermission;
    private static final int PERMISSION_ID = 44;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_annonce);
        initViews();
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        locationpermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        //RECEIVE detaill data of annonce
        getIncomingIntent();

        //get user  avis moyen ( moyenn etoiles)
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Commentaire").child(annonce.getUserId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String etoile = postSnapshot.child("repos").getValue().toString();
                    nbrComm++;
                    totalRepos = totalRepos + Float.valueOf(etoile);
                }
                if (nbrComm == 0) {
                    etoiles_user.setText("0");
                } else {
                    float resultat = totalRepos / nbrComm;
                    etoiles_user.setText(String.valueOf(resultat));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });
        // ajouter un offre
        offre.setOnClickListener(v -> {
            MyDialog();
        });
        // go to profil user
        profileUser.setOnClickListener(v -> {
            Intent profil = new Intent(DetailAnnonce.this, profilUser.class);
            profil.putExtra("user", annonce.getUserId());
            startActivity(profil);
        });
        //retour
        txtRetout.setOnClickListener(v -> {
            finish();
        });

        // envoyer i=un messag
        sendMsg.setOnClickListener(v -> {
            Intent intent = new Intent(DetailAnnonce.this, MessageActivity.class);
            intent.putExtra("user", annonce.getUserId());
            startActivity(intent);

        });

        // show position of annonce
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                String distination = "adrar";
                LatLng myCoordinates = new LatLng(latitude, longitude);
                String source = getCityName(myCoordinates);
                DisplayTrack(source, distination);
            }
        });
        //share Post *FileProvider*
       /*shar_publication.setOnClickListener(v -> {
            String nom = tite.getText().toString().trim();
            String description = desc.getText().toString().trim();
            ArrayList<Uri> im=new ArrayList<>();
            for(int i=0;i<images.getPageCount();i++){
                int k=images.getDrawableState()[i];
                im.add(Uri.parse(String.valueOf(k)));
            }
            get image from image coa
            BitmapDrawable bitmapDrawable = (BitmapDrawable) images.getDrawable();
         BitmapDrawable bitmapDrawable = (BitmapDrawable) images.getDrawableState();

           convert image to bitmap
          Bitmap bitmap = bitmapDrawable.getBitmap();
           share(nom, description, bitmap);


        });*/

    }

    public void getIncomingIntent() {
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            String photouser = dataSnapshot.child("photoUser").getValue().toString();
                            Picasso.get().load(photouser).into(imgUser);
                            Log.e("User is :", dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tite.setText(annonce.getTitreAnnonce());
        desc.setText(annonce.getDescriptionAnnonce());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        time.setText(str);
        ville.setText(annonce.getWilaya() + ", ");
        commune.setText(annonce.getCommune());
        ArrayList<String> Images = new ArrayList<>();
        for (String image : annonce.getImages()) {
            Images.add(image);
        }
        images.setPageCount(Images.size());
        images.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                Glide.with(DetailAnnonce.this)
                        .load(Images.get(position))
                        .centerCrop()
                        .into(imageView);


            }
        });
        for (int i = 0; i < annonce.getArticleEnRetour().size(); i++) {
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }
        images.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //image clicable
                showImage(Images.get(position));

            }
        });

    }

    private void share(String nom, String description, Bitmap bitmap) {
        //concatenate title and desc to share
        String shareBody = nom + "\n" + description;
        //first we will save the image  in cache, get the saved image uri
        Uri uri = saveImgeToShare(bitmap);
        //share intent
        ////-------------send image
       /* Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"Partager avec "));*/


//////////////----------------------send text
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sendIntent.setType("text/*");
        startActivity(sendIntent);

        //startActivity(Intent.createChooser(sInent, "Partager avec "));
/*
 private void share(String nom, String description, Bitmap bitmap) {
        //concatenate title and desc to share
        String shareBody = nom + "\n" + description;
        //first we will save the image  in cache, get the saved image uri
        Uri uri = saveImgeToShare(bitmap);
        //share intent
        Intent sInent = new Intent(Intent.ACTION_SEND);//ACTION_SEND -  Cette intention démarrera l'activité d'envoi.
        sInent.putExtra(Intent.EXTRA_STREAM, uri);//Put extra mettra le flux supplémentaire avec le nom de chemin de l'image que nous partageons.
       // sInent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sInent.putExtra(Intent.EXTRA_SUBJECT, shareBody);

        sInent.setType("image/*");// s appele un  MIME;Nous devons définir le type de données d'envoi,
        sInent.setType("text/*");//Nous devons changer le type d'envoi car nous envoyons du texte.
        //sInent.setType("image/png");
        //  sInent.setType("txt/link");
        sInent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(sInent, "Partager avec "));
        //dans facebook, il ne peut partager l'image que parce que facebook ne permet pas de partager le texte via l'intention
    }
 */

    }

    private Uri saveImgeToShare(Bitmap bitmap) {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdir();//create if not exist
            File file = new File(imageFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.bouchra.myapplicationechange.fileprovider", file);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return uri;
    }


    private void showImage(String position) {
        View view = getLayoutInflater().inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);
        Glide.with(this)
                .asBitmap()
                .load(position)
                .into(imageView);

        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });
        //full screen
        MyDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MyDialog.setContentView(view);
        MyDialog.show();

    }

    // deb dialog offre
    private void MyDialog() {
        final Dialog MyDialog = new Dialog(DetailAnnonce.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.customdialog);

        Button ajout = MyDialog.findViewById(R.id.ajout);
        Button selec = MyDialog.findViewById(R.id.selec);
        TextView close = MyDialog.findViewById(R.id.close);

        ajout.setEnabled(true);
        selec.setEnabled(true);
        close.setEnabled(true);
        close.setOnClickListener(v -> {
            MyDialog.cancel();
        });
        // ajouter un nov offre
        ajout.setOnClickListener(v -> {
            Intent ajou = new Intent(DetailAnnonce.this, AjoutOffre.class);
            ajou.putExtra("Annonce", annonce);
            startActivity(ajou);
            finish();
            MyDialog.dismiss();


        });
        // selectonne depuis mes posts
        selec.setOnClickListener(v -> {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction t = manager.beginTransaction();
            final Posts m4 = new Posts();
            Bundle b2 = new Bundle();
            b2.putString("edttext", annonce.getIdAnnonce());
            m4.setArguments(b2);
            t.add(R.id.fragmentposts, m4);
            t.commit();
            MyDialog.dismiss();

        });

        MyDialog.show();
    }

    private void initViews() {

        txtRetout = findViewById(R.id.retour);
        time = findViewById(R.id.date_h);
        tite = findViewById(R.id.titte_annonce);
        desc = findViewById(R.id.desc);
        images = findViewById(R.id.img_annonc);
        retour = findViewById(R.id.article_retour);
        name_user = findViewById(R.id.nom_user);
        imgUser = findViewById(R.id.img_user);
        shar_publication = findViewById(R.id.shar_publication);
        profileUser = findViewById(R.id.relative_profie);
        imgUser = findViewById(R.id.img_user);
        etoiles_user = findViewById(R.id.etoiles_user);
        offre = findViewById(R.id.offre);
        sendMsg = findViewById(R.id.send_Msg);
        ville = findViewById(R.id.ville);
        commune = findViewById(R.id.commune);
        position = findViewById(R.id.position);

    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(DetailAnnonce.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    private void DisplayTrack(String source, String distination) {
        try {
            //when google map is installed
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + source + "/" + distination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } catch (ActivityNotFoundException e) {
            // redirect to play store
            // google maps not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
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
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

        }
    };


}
