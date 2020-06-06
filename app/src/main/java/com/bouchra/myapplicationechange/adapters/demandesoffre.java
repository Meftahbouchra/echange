package com.bouchra.myapplicationechange.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.profilUser;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class demandesoffre extends RecyclerView.Adapter<demandesoffre.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Context context;
    private ArrayList<Offre> mesDemandeDoffres;
    private ArrayList<Membre> membres;
    private String nomAnnonce;
    private String annonce;// id offre selected
    private Dialog MyDialog;
    private String[] locationpermission;
    private static final int PERMISSION_ID = 44;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient mFusedLocationClient;

    public demandesoffre(Context context, ArrayList<Offre> mesDemandeDoffres, ArrayList<Membre> membres, String nomAnnonce, String annonce) {
        this.context = context;

        this.mesDemandeDoffres = mesDemandeDoffres;
        this.membres = membres;
        this.nomAnnonce = nomAnnonce;
        this.annonce = annonce;

    }


    public void setAnnonce(String annonce) {
        this.annonce = annonce;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public demandesoffre.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_demandesoffre, parent, false);
        demandesoffre.ViewHolder viewHolder = new demandesoffre.ViewHolder(view);
        return (viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull demandesoffre.ViewHolder holder, int position) {
        locationpermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        //////////********************************************************  offre
        Offre offre = mesDemandeDoffres.get(position);
        holder.titreOffre.setText(offre.getNomOffre());
        Glide.with(context)
                .load(offre.getImage())
                .centerCrop()
                .into(holder.imageOffre);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  kk:mm ");
        String str = simpleDateFormat.format(offre.getDateOffre());
        holder.timeOffre.setText(str);
        holder.descriptionOffre.setText(offre.getDescriptionOffre());
        holder.villeOffre.setText(offre.getWilaya() + ",");
        holder.communeOffre.setText(offre.getCommune());
/////////////*****************************************************membre
        Membre membre = membres.get(position);
        holder.nameUser.setText(membre.getNomMembre());
        if (membre.getPhotoUser() != null)
            Picasso.get().load(membre.getPhotoUser()).into(holder.imageUser);
        if (annonce != null) {

        } else {
            annonce = " ";
        }
        Log.e("id offre selected is", annonce);
        if (offre.getIdOffre().equals(annonce)) {
            holder.itemView.setBackgroundResource(R.drawable.card_offre_accepted);

        } else {

            holder.itemView.setBackgroundResource(R.drawable.card_offre_refuse);
        }

        holder.relative_profie.setOnClickListener(v -> {
            Intent profil = new Intent(context, profilUser.class);
            profil.putExtra("user", membre.getIdMembre());
            context.startActivity(profil);
        });


        holder.position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                LatLng myCoordinates = new LatLng(latitude, longitude);
                String source = getCityName(myCoordinates);
                DisplayTrack(source, offre.getCommune());
            }
        });
        holder.imageOffre.setOnClickListener(v -> showImage(offre.getImage()));
        holder.itemView.setOnLongClickListener(v -> {
            ConfirmeOffre confirmeOffre = new ConfirmeOffre();
            Bundle b2 = new Bundle();
            b2.putSerializable("offre", offre);
            b2.putString("nomAnnonce", nomAnnonce);
            confirmeOffre.setArguments(b2);
            confirmeOffre.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");
            return true;
            /*
            return truesignifie que l'événement est consommé. Il est géré. Aucun autre événement de clic ne sera notifié.
return falsesignifie que l'événement n'est pas consommé. Tout autre événement de clic continuera de recevoir des notifications.
Donc, si vous ne voulez onClickpas également être déclenché après un événement onLongClick, vous devriez le faire à return truepartir de l' onLongClickévénement.
             */
        });


    }

    @Override
    public int getItemCount() {
        return mesDemandeDoffres.size();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  obtenir les informations de localisation
                getLastLocation();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titreOffre;
        private TextView descriptionOffre;
        private TextView villeOffre;
        private TextView communeOffre;
        private TextView timeOffre;
        private ImageView imageOffre;
        private CircleImageView imageUser;
        private TextView nameUser;
        private RelativeLayout relative_profie;
        private LinearLayout position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titreOffre = itemView.findViewById(R.id.titte_offre);
            descriptionOffre = itemView.findViewById(R.id.desc_offre);
            villeOffre = itemView.findViewById(R.id.ville);
            communeOffre = itemView.findViewById(R.id.commune);
            timeOffre = itemView.findViewById(R.id.datH);
            imageOffre = itemView.findViewById(R.id.img_offre);
            imageUser = itemView.findViewById(R.id.img_user);
            nameUser = itemView.findViewById(R.id.nom_user);
            relative_profie = itemView.findViewById(R.id.relative_profie);
            position = itemView.findViewById(R.id.position);


        }


    }

    public void selectedoffre(String idOffre, Annonce annonce) {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
        Annonce annonce1 = new Annonce();
        annonce.setArticleEnRetour(annonce.getArticleEnRetour());
        annonce.setImages(annonce.getImages());
        annonce.setWilaya(annonce.getWilaya());
        annonce.setCommune(annonce.getCommune());
        annonce.setIdAnnonce(annonce.getIdAnnonce());
        annonce.setUserId(annonce.getUserId());
        annonce.setStatu("ASSINED");
        annonce.setTitreAnnonce(annonce.getTitreAnnonce());
        annonce.setDescriptionAnnonce(annonce.getDescriptionAnnonce());
        annonce.setDateAnnonce(annonce.getDateAnnonce());

        annonce.setIdOffreSelected(idOffre);

        databaseReference.setValue(annonce1).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                etatConfirmOffre(idOffre, annonce1.getIdAnnonce());

            }
        });


    }


    private void etatConfirmOffre(String idOffre, String idAnnonce) {

        Task<Void> databasereference;
        databasereference = FirebaseDatabase.getInstance().getReference("Offre").child(idAnnonce).child(idOffre).child("statu")
                .setValue("NEED_To_Be_CONFIRM")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// get and show proper error message
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showImage(String position) {
        View view = LayoutInflater.from(context).inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);
        Glide.with(context)
                .asBitmap()
                .load(position)
                .into(imageView);

        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });
        //full screen
        MyDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MyDialog.setContentView(view);
        MyDialog.show();

    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            if (addresses.size() > 0) {
                if (addresses.get(0).getAddressLine(0) != null) {
                    String address = addresses.get(0).getAddressLine(0);
                    myCity = addresses.get(0).getLocality();
                    Log.d("mylog", "Complete Address: " + addresses.toString());
                    Log.d("mylog", "Address: " + address);
                }
            }
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
            context.startActivity(intent);


        } catch (ActivityNotFoundException e) {
            // redirect to play store
            // google maps not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                ((Activity) context), locationpermission, PERMISSION_ID);
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
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
                Toast.makeText(context, "Activer l'emplacement", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

        }
    };


}
