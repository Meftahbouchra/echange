package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.demandesoffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
    private String annonce;
    private TextView information;
    private TextView retour;


    public DemandesOffre() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandes_offre);

        recyclerView = findViewById(R.id.recyle_demandesoffres);
        information=findViewById(R.id.information);
        retour=findViewById(R.id.retour);
        offres = new ArrayList<>();
        membres = new ArrayList<>();


        Intent ajou = getIntent();
        Annonce annonce1 = (Annonce) ajou.getSerializableExtra("annonce");

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        demandesoffre = new demandesoffre(this, offres, membres, annonce1.getTitreAnnonce(), annonce1.getIdAnnonce());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(demandesoffre);
// get annonce offres && user offre
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(annonce1.getIdAnnonce());
        ref.addValueEventListener(new ValueEventListener() {//Nous attacherons un ValueEventListener à la référence pour lire les données.
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                        /*
            Chaque fois que vous changez quelque chose dans la base de données,
            la méthode onDataChange () sera exécutée. Il contient toutes les données
            à l'intérieur du chemin spécifié dans la référence. Nous pouvons utiliser l' objet
             DataSnapshot pour lire toutes les données à l'intérieur de la référence
            */
                if(snapshot.exists()){
                    information.setVisibility(View.GONE);
                    offres.clear();
                    Log.e("Count ", "" + snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Log.e("Data here", postSnapshot.toString());
                        String iduser = postSnapshot.child("idUser").getValue().toString();
                        DatabaseReference refe = FirebaseDatabase.getInstance().getReference("Membre");
                        refe.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshott : dataSnapshot.getChildren()) {
                                    String iduserMembre = postSnapshott.child("idMembre").getValue().toString();
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

                }else {
                    recyclerView.setVisibility(View.GONE);
                    information.setText("Vous n'avez pas des offres pour cette annonce ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });

// knt kdra ga3 mndirch had l requete psq deja raha andi
        FirebaseDatabase databasee = FirebaseDatabase.getInstance();
        DatabaseReference reff = databasee.getReference("Annonce").child(annonce1.getIdAnnonce());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Idoffre = "";
                if (dataSnapshot.getValue() != null) {
                    Idoffre = dataSnapshot.child("idOffreSelected").getValue().toString();
                    annonce = Idoffre;
                    demandesoffre.setAnnonce(annonce);
                } else {
                    Log.e("TAG", " it's null.");
                    annonce = " ";
                }
                Log.e("id Offre", Idoffre);
                Log.e("id annonce", annonce);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });


    }

   public void selectedoffre(String idOffre) {
        Intent ajou = getIntent();
        Annonce annonce1 = (Annonce) ajou.getSerializableExtra("annonce");
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce1.getIdAnnonce());
        Annonce annonce = new Annonce();
        annonce.setArticleEnRetour(annonce1.getArticleEnRetour());
        annonce.setImages(annonce1.getImages());
        annonce.setWilaya(annonce1.getWilaya());
        annonce.setCommune(annonce1.getCommune());
        annonce.setIdAnnonce(annonce1.getIdAnnonce());
        annonce.setUserId(annonce1.getUserId());
        annonce.setStatu("ASSINED");
        annonce.setTitreAnnonce(annonce1.getTitreAnnonce());
        annonce.setDescriptionAnnonce(annonce1.getDescriptionAnnonce());
        annonce.setDateAnnonce(new Date());

        annonce.setIdOffreSelected(idOffre);

        databaseReference.setValue(annonce).addOnCompleteListener(task2 -> {
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
                        Toast.makeText(DemandesOffre.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


}
