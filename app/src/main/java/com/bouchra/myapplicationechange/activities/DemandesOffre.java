package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.demandesoffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class DemandesOffre extends AppCompatActivity {

    private demandesoffre demandesoffre;
    private ArrayList<Offre> offres;
    private RecyclerView recyclerView;
    private ArrayList<Membre> membres;
  //  private ArrayList<Annonce>annonces;
    private String annonce;//if offre


    public DemandesOffre() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandes_offre);
        recyclerView = findViewById(R.id.recyle_demandesoffres);

        offres = new ArrayList<>();
        membres = new ArrayList<>();
        Intent ajou = getIntent();
        String nomAnnon = ajou.getStringExtra("nomAnnonce");

        String IdAnnonce=ajou.getStringExtra("idAnnonce");

        demandesoffre = new demandesoffre(this, offres, membres, nomAnnon,annonce);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(demandesoffre);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(IdAnnonce);
        ref.addValueEventListener(new ValueEventListener() {//Nous attacherons un ValueEventListener à la référence pour lire les données.
            @Override
            public void onDataChange(DataSnapshot snapshot) {/*
            Chaque fois que vous changez quelque chose dans la base de données,
            la méthode onDataChange () sera exécutée. Il contient toutes les données
            à l'intérieur du chemin spécifié dans la référence. Nous pouvons utiliser l' objet
             DataSnapshot pour lire toutes les données à l'intérieur de la référence
            */
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Data here", postSnapshot.toString());
                    String iduser = postSnapshot.child("idUser").getValue().toString();// id user f offre
                    DatabaseReference refe = FirebaseDatabase.getInstance().getReference("Membre");
                    refe.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshott : dataSnapshot.getChildren()) {
                                String iduserMembre = postSnapshott.child("idMembre").getValue().toString(); //ism att ta3 id membre
                                if (iduser.equals(iduserMembre)) {

                                    membres.add(postSnapshott.getValue(Membre.class));
                                    offres.add(postSnapshot.getValue(Offre.class));
                                    demandesoffre.notifyDataSetChanged();
                                }


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
//Si une erreur se produit, la méthode onCancelled () sera appelée.
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
// Getting model failed, log a message

            }

        });
        //////////////////////////////////////////////////////////
        String idAnnonce = ajou.getStringExtra("idAnnonce");
         FirebaseDatabase databasee = FirebaseDatabase.getInstance();
        DatabaseReference reff = databasee.getReference("Annonce").child(idAnnonce);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {

                            String Idoffre = dataSnapshot.child("idOffreSelected").getValue().toString();
                            Log.e("iiiiiiiiiiiiid offre is", Idoffre);
                            annonce=Idoffre;


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
                // Getting model failed, log a message
            }
        });


    }

    public void selectedoffre(String idOffre) {
        Intent ajou = getIntent();
        String idAnnonce = ajou.getStringExtra("idAnnonce");

        String descp = ajou.getStringExtra("descp");
       // String date = ajou.getStringExtra("date");
        String statu = ajou.getStringExtra("statu");
        String userid = ajou.getStringExtra("userid");
        String commune = ajou.getStringExtra("commune");
        String wilaya = ajou.getStringExtra("wilaya");
        ArrayList<String>articleret = ajou.getStringArrayListExtra("articleret");
        ArrayList<String>images = ajou.getStringArrayListExtra("images");
        String nomAnnon = ajou.getStringExtra("nomAnnonce");
        String date = ajou.getStringExtra("date");



        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Annonce").child(idAnnonce); // nkharaj id ta3 annonce
        Annonce annonce = new Annonce();
       annonce.setArticleEnRetour(articleret);
       annonce.setImages(images);
       annonce.setWilaya(wilaya);
       annonce.setCommune(commune);
       annonce.setIdAnnonce(idAnnonce);
       annonce.setUserId(userid);
       annonce.setStatu(statu);
       annonce.setTitreAnnonce(nomAnnon);
       annonce.setDescriptionAnnonce(descp);

       // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm "); // +heur
      //  String str = simpleDateFormat.format(date);
       // annonce.setDateAnnonce(str);
        annonce.setDateAnnonce(new Date());////////////////////////////////////////////////////***************************************
// hda  li zdnah
        annonce.setIdOffreSelected(idOffre);
        databaseReference.setValue(annonce).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {


            }
        });


    }

}
