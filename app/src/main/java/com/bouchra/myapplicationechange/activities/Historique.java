package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    private ArrayList<com.bouchra.myapplicationechange.models.Historique> historiques = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView information;
    private myhistorique myhistorique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        preferenceUtils = new PreferenceUtils(this);

        recyclerView = findViewById(R.id.recyle_historique);
        myhistorique = new myhistorique(this, historiques);
        information = findViewById(R.id.information);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //recyclerView.setAdapter(myhistorique);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Historique").child(preferenceUtils.getMember().getIdMembre());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.e("data", postSnapshot.toString());
                        Offre offre = new Offre();
                        Annonce annonce = new Annonce();
                        String statu = postSnapshot.child("statu").getValue().toString();
                        if (statu.equals("DELETEOFFRE") || statu.equals("REJECTED") || statu.equals("COMPLETEDOFFRE")) {
                            //  offres.add(postSnapshot.getValue(Offre.class));
                            offre = postSnapshot.getValue(Offre.class);
                            historiques.add(offre);


                        }

                        if (statu.equals("COMPLETEDANNONCE") || statu.equals("DELETEDANNONCE")) {
                            //annonces.add(postSnapshot.getValue(Annonce.class));
                            annonce = postSnapshot.getValue(Annonce.class);
                            historiques.add(annonce);


                        }

                        myhistorique.notifyDataSetChanged();
                        information.setVisibility(View.GONE);
                        recyclerView.setAdapter(myhistorique);


                    }

                } else {
                    information.setText("Vous n'avez pas d' article ici ");
                    recyclerView.setVisibility(View.GONE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
//                      DELETEOFFRE offre suprimer
//                                                                    DELETEDANNONCE annonce suprimer
//            REJECTED annonce suprimer, REJECTED annonce non select cette offre
//      COMPLETEDOFFRE achange terminer offre
//                                                                COMPLETEDANNONCE echange terminer anonce