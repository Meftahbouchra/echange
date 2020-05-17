package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailMesannonce extends AppCompatActivity {
    private Button voirOffres;
    private Annonce annonce;
    private TextView tite;
    private TextView desc;
    private ImageView img;
    private TextView retour;
    private TextView menu;
    String nameCategorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mesannonce);
        tite = findViewById(R.id.titte_annonce);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img_annonc);
        retour = findViewById(R.id.article_retour);
        menu = findViewById(R.id.menu);
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
                            // Log.e("Data here", IDannonce);
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



           /* BottomsheetManipAnnonceOffre bottomsheet = new BottomsheetManipAnnonceOffre();
            bottomsheet.show(getSupportFragmentManager(), "manipAnnonce");*/
        });
        voirOffres = findViewById(R.id.voir);
        voirOffres.setOnClickListener(v -> {
            Intent ajou = new Intent(DetailMesannonce.this, DemandesOffre.class);
            ajou.putExtra("annonce", annonce);
            startActivity(ajou);
            finish();

        });
        // setImage(imageUrl,imageName);
        //  tite.setText("abcd");
        // getIncomingIntent();
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            // name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            String nomUser = dataSnapshot.child("nomMembre").getValue().toString();
                            Log.e("User is :", nomUser);
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

        Date d = new Date(new Date().getTime() + 28800000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm "); // +heur
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        //   time.setText(str);
        setImage(annonce.getImages().get(0), annonce.getTitreAnnonce());
        for (int i = 0; i < annonce.getArticleEnRetour().size(); i++) {
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }


   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater menu
        inflater.inflate(R.menu.setting_annonce,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handel menu item clicks
        int id= item.getItemId();
        if(id == R.id.modifier){
            Toast.makeText(getActivity(), "modifier", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.Supprimer){
            Toast.makeText(getActivity(), "Supprimer", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }*/
    }


    /*  @Override
      public void getIncomingIntent() {

      }*/
    private void setImage(String imageUrl, String imageName) {


        tite.setText(imageName);


        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
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




        /*
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(annonce.getIdAnnonce());
       //removeValue ()  : utilisé pour supprimer les données.
        dOffre.removeValue();*/

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
        // getSupportFragmentManager().beginTransaction().add(R.id.fragment,new ModifierAnnonce(),"ModifierAnnonce").commit();
        //PACK DATA IN A BUNDLE

        Intent intent = new Intent(DetailMesannonce.this, ModifierAnnonce.class);
        intent.putExtra("annonce", annonce);
        startActivity(intent);
        finish();

    }

}