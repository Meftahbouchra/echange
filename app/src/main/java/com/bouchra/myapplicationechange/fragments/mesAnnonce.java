package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.myannonce;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mesAnnonce extends Fragment implements SearchView.OnQueryTextListener {


    private myannonce myannonce;
    private ArrayList<Annonce> annonces;
    private RecyclerView recyclerView;
    private SearchView editsearch;
    private TextView information;
    private String offre;
    PreferenceUtils preferenceUtils;

    public mesAnnonce() {
    }

    public mesAnnonce(String offre) {
        this.offre = offre;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mesannonces, container, false);

        recyclerView = view.findViewById(R.id.recyle_mesannonces);
        information = view.findViewById(R.id.information);
        annonces = new ArrayList<>();
        preferenceUtils = new PreferenceUtils(getContext());
        if (offre == null) {
            myannonce = new myannonce(getContext(), annonces);
        } else {
            myannonce = new myannonce(getContext(), annonces, offre);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myannonce);

        // get mes annonceqs
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                annonces.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Data here", postSnapshot.toString());
                    String user = postSnapshot.child("userId").getValue().toString();
                    if (user.equals(preferenceUtils.getMember().getIdMembre())) {
                        annonces.add(postSnapshot.getValue(Annonce.class));
                        myannonce.notifyDataSetChanged();
                    }


                }
                if (annonces.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    information.setText("Vous n'avez pas d'annonces");


                } else {
                    information.setVisibility(View.GONE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });
        // Locate the EditText in listview_main.xml
    /*    editsearch =view.findViewById(R.id.search);/////////////////////////////////////////////////
        editsearch.setOnQueryTextListener(this);*/
        // récupère la chaîne de requête actuellement dans le champ de texte
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Annonce> output = new ArrayList<>();
        for (Annonce object : annonces) {
            String obj = object.getTitreAnnonce().toLowerCase();
            if (obj.contains(newText.toLowerCase())) output.add(object);
        }
        myannonce.setMesannonce(output);
        myannonce.notifyDataSetChanged();
        return false;
    }
}
