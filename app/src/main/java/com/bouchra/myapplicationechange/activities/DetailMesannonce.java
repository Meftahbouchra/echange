package com.bouchra.myapplicationechange.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BottomsheetManipAnnonceOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailMesannonce extends AppCompatActivity {
    private Button voirOffres;
    private Annonce annonce;
    private TextView tite;
    private TextView desc;
    private TextView retour;
    private TextView back;
    private TextView menu;
    private String nameCategorie;
    private TextView ville;
    private TextView commune;
    private CarouselView images;
    private TextView date_h;
    private Dialog MyDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mesannonce);
        tite = findViewById(R.id.titte_annonce);
        desc = findViewById(R.id.desc);
        images = findViewById(R.id.img_annonc);
        retour = findViewById(R.id.article_retour);
        back=findViewById(R.id.retour);
        menu = findViewById(R.id.menu);
        ville = findViewById(R.id.ville);
        commune = findViewById(R.id.commune);
        voirOffres = findViewById(R.id.voir);
        date_h = findViewById(R.id.date_h);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menu.setOnClickListener(v -> {
            BottomsheetManipAnnonceOffre bottomsheet = new BottomsheetManipAnnonceOffre();
            Bundle b2 = new Bundle();
            b2.putSerializable("fromAnnonce", annonce);
            bottomsheet.setArguments(b2);
            bottomsheet.show(getSupportFragmentManager(), "manipAnnonce");

//get name categorie
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Categorie");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot key : postSnapshot.getChildren()) {

                            String IDannonce = key.getKey();
                            if (IDannonce.equals(annonce.getIdAnnonce())) {
                                String nom = postSnapshot.getKey();
                                nameCategorie = nom;
                                Log.e("catego", nameCategorie);


                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        voirOffres.setOnClickListener(v -> {
            Intent ajou = new Intent(DetailMesannonce.this, DemandesOffre.class);
            ajou.putExtra("annonce", annonce);
            startActivity(ajou);
            finish();

        });
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        tite.setText(annonce.getTitreAnnonce());
        desc.setText(annonce.getDescriptionAnnonce());
        ville.setText(annonce.getWilaya() + ", ");
        commune.setText(annonce.getCommune());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  kk:mm ");
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        date_h.setText(str);

        ArrayList<String> Images = new ArrayList<>();
        for (String image : annonce.getImages()) {
            Images.add(image);
        }
        images.setPageCount(Images.size());
        images.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                Glide.with(DetailMesannonce.this)
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


    public void deleteAnnonce() {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(annonce.getUserId()).child(annonce.getIdAnnonce());
        Map<String, Object> ANNONCE = new HashMap<>();
        ANNONCE.put("idAnnonce", annonce.getIdAnnonce());
        ANNONCE.put("titreAnnonce", annonce.getTitreAnnonce());
        ANNONCE.put("commune", annonce.getCommune());
        ANNONCE.put("wilaya", annonce.getWilaya());
        ANNONCE.put("images", annonce.getImages());
        ANNONCE.put("descriptionAnnonce", annonce.getDescriptionAnnonce());
        ANNONCE.put("articleEnRetour", annonce.getArticleEnRetour());
        ANNONCE.put("dateAnnonce", annonce.getDateAnnonce());
        ANNONCE.put("IdOffreSelected", annonce.getIdOffreSelected());
        ANNONCE.put("statu", "DELETEDANNONCE");
        ANNONCE.put("categorie", nameCategorie);
        mDbRef.updateChildren(ANNONCE);
        if (annonce.getStatu().equals("CREATED")) {
            // ya pas d offre
            DatabaseReference dCategorie = FirebaseDatabase.getInstance().getReference("Categorie").child(nameCategorie).child(annonce.getIdAnnonce());
            dCategorie.removeValue();
            DatabaseReference dAnnonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
            dAnnonce.removeValue();
        } else {
            getOffres(annonce.getIdAnnonce());
            DatabaseReference dAnnonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
            dAnnonce.removeValue();
            DatabaseReference dCategorie = FirebaseDatabase.getInstance().getReference("Categorie").child(nameCategorie).child(annonce.getIdAnnonce());
            dCategorie.removeValue();
        }
        finish();

    }

    private void getOffres(String idAnnonce) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(idAnnonce);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // njib l object w ndirah fi methode w hadik l methode nsuprimi fiha
                    Offre offre = postSnapshot.getValue(Offre.class);
                    deletOffre(offre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });
    }

    private void deletOffre(Offre offre) {
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(offre.getIdUser()).child(offre.getIdOffre());
        Map<String, Object> OFFRE = new HashMap<>();
        OFFRE.put("nomOffre", offre.getNomOffre());
        OFFRE.put("idOffre", offre.getIdOffre());
        OFFRE.put("annonceId", offre.getAnnonceId());
        OFFRE.put("commune", offre.getCommune());
        OFFRE.put("dateOffre", offre.getDateOffre());
        OFFRE.put("descriptionOffre", offre.getDescriptionOffre());
        OFFRE.put("idUser", offre.getIdUser());
        OFFRE.put("images", offre.getImage());
        OFFRE.put("wilaya", offre.getWilaya());
        OFFRE.put("statu", "REJECTED");
        mDbRef.updateChildren(OFFRE);
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();
    }

    public void goToFragmentModifier() {

        Intent intent = new Intent(DetailMesannonce.this, ModifierAnnonce.class);
        intent.putExtra("annonce", annonce);
        startActivity(intent);
        finish();

    }

}