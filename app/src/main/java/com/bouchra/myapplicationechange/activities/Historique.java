package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.myhistorique;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Historique extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    Button btn;
    private ArrayList<Annonce> annonces;
    private ArrayList<Offre> offres;
    private RecyclerView recyclerView;
    private myhistorique myhistorique;

    String oui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        preferenceUtils = new PreferenceUtils(this);

        recyclerView = findViewById(R.id.recyle_historique);
        annonces = new ArrayList<>();
        offres = new ArrayList<>();
        btn = findViewById(R.id.btn);
        myhistorique = new myhistorique(this, annonces, offres);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myhistorique);

        btn.setOnClickListener(v -> {
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Historique").child(preferenceUtils.getMember().getIdMembre());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String statu = postSnapshot.child("statu").getValue().toString();
                        if (statu.equals("DELETEOFFRE")) {
                            offres.add(postSnapshot.getValue(Offre.class));
                            myhistorique.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "offre suprimer", Toast.LENGTH_SHORT).show();
                        }
                        if (statu.equals("REJECTED")) {
                            Toast.makeText(getApplicationContext(), "annonce suprimer, REJECTED annonce non select cette offre", Toast.LENGTH_SHORT).show();
                            offres.add(postSnapshot.getValue(Offre.class));
                            myhistorique.notifyDataSetChanged();
                        }

                        if (statu.equals("COMPLETEDOFFRE")) {
                            Toast.makeText(getApplicationContext(), "achange terminer offre", Toast.LENGTH_SHORT).show();
                            offres.add(postSnapshot.getValue(Offre.class));
                            myhistorique.notifyDataSetChanged();
                        }
                        if (statu.equals("COMPLETEDANNONCE")) {
                            Toast.makeText(getApplicationContext(), " echange terminer anonce", Toast.LENGTH_SHORT).show();
                            annonces.add(postSnapshot.getValue(Annonce.class));
                            oui="oui";
                            myhistorique.notifyDataSetChanged();
                        }
                        if (statu.equals("DELETEDANNONCE")) {
                            Toast.makeText(getApplicationContext(), "annonce suprimer", Toast.LENGTH_SHORT).show();
                            annonces.add(postSnapshot.getValue(Annonce.class));
                            oui="oui";
                            myhistorique.notifyDataSetChanged();
                        }

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        });

    }


}
//                      DELETEOFFRE offre suprimer
//                                                                    DELETEDANNONCE annonce suprimer
//            REJECTED annonce suprimer, REJECTED annonce non select cette offre
//      COMPLETEDOFFRE achange terminer offre
//                                                                COMPLETEDANNONCE echange terminer anonce