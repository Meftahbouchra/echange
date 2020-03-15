package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.demandesoffre;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DemandesOffre extends AppCompatActivity {

    private demandesoffre demandesoffre;
    private ArrayList<Offre>offres ;
    private RecyclerView recyclerView;


    public DemandesOffre() {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandes_offre);
        recyclerView = findViewById(R.id.recyle_demandesoffres);

        offres = new ArrayList<>();

        demandesoffre = new demandesoffre(this, offres);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(demandesoffre);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Data here", postSnapshot.toString());
                    offres.add(postSnapshot.getValue(Offre.class));
                    demandesoffre.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
// Getting model failed, log a message

            }

        });
    }
}
