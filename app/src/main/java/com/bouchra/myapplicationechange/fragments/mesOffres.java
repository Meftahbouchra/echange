package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.myoffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mesOffres extends Fragment {

    private myoffre myoffre;
    private ArrayList<Offre> offres;//////////////hadi li rna njobi fiha m fire base
    private ArrayList<Annonce> annonces;
    private RecyclerView recyclerView;
    // SearchView editsearch;

    public mesOffres() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mesoffres, container, false);

        recyclerView = view.findViewById(R.id.recyle_mesoffres);
        offres = new ArrayList<>();
        annonces=new ArrayList<>();
        myoffre = new myoffre(getContext(), offres, annonces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myoffre);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre");
        PreferenceUtils preferenceUtils;
        preferenceUtils = new PreferenceUtils(getContext());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot off : postSnapshot.getChildren()) {
                        String user = off.child("idUser").getValue().toString();
                        String idnonce = off.child("annonceId").getValue().toString();
                        Log.e("userli dar lofre howa: ", user);
                        Log.e("userli dar annon howa: ", idnonce);
                        if (user.equals(preferenceUtils.getMember().getIdMembre())) {
                            offres.add(off.getValue(Offre.class));
                            myoffre.notifyDataSetChanged();
                        }

                        // hna njib annonce

                        final FirebaseDatabase databas = FirebaseDatabase.getInstance();
                        DatabaseReference df = databas.getReference("Annonce").child(idnonce);
                        df.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                Log.e("Data  of annonce here", dataSnapshot3.toString());
                                Annonce ann = dataSnapshot3.getValue(Annonce.class);

                                annonces.add(ann);
                                myoffre.setAnnonces(annonces);
                               /* annonces.add(dataSnapshot3.getValue(Annonce.class));
                                myoffre.setAnnonces(annonces);*/                                /*Annonce annonce = dataSnapshot3.getValue(Annonce.class);
                                annonces.add(annonce);*/


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });

        return view;
    }
}
