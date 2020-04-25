package com.bouchra.myapplicationechange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.activities.GoogleMaps;
import com.bouchra.myapplicationechange.activities.MainActivity;
import com.bouchra.myapplicationechange.activities.annonce.AnnonceActivity;
import com.bouchra.myapplicationechange.adapters.publicationannonceadapt;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Acceuil extends Fragment implements Single_choice_classification.SingleChoiceListener, SearchView.OnQueryTextListener {
    private FirebaseAuth firebaseAuth;
    private LinearLayout linearLayout2;
    private TextView textView1, textView2;
    private Button google;
    private RelativeLayout addAnnonce;
    private publicationannonceadapt publicAdapter;
    private ArrayList<Annonce> annonces;
    private RecyclerView recyclerView;
    private SearchView editsearch;
    private String categorie;
    private String wilaya = "", searchText = "";
    private TextView tout, vehicules, telephones, Automobiles, pieces_detachees, immobilier, vetements, livres, eletronique_et_electromenager, accessoires_de_mode,
            cosmetiques_et_beaute, maison_et_fournitures, loisirs_et_devertissements, matiriaux_et_equipements;

    public Acceuil() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_acceuil, container, false);

        linearLayout2 = view.findViewById(R.id.layout1);
        textView1 = view.findViewById(R.id.txt11);
        // textView2 = view.findViewById(R.id.txt22);
        recyclerView = view.findViewById(R.id.recyle_public);
        google = view.findViewById(R.id.button5);

        tout = view.findViewById(R.id.tout);
        vehicules = view.findViewById(R.id.vehicules);
        telephones = view.findViewById(R.id.telephones);
        Automobiles = view.findViewById(R.id.Automobiles);
        pieces_detachees = view.findViewById(R.id.pieces_detachees);
        immobilier = view.findViewById(R.id.immobilier);
        vetements = view.findViewById(R.id.vetements);
        livres = view.findViewById(R.id.livres);
        eletronique_et_electromenager = view.findViewById(R.id.eletronique_et_electromenager);
        accessoires_de_mode = view.findViewById(R.id.accessoires_de_mode);
        cosmetiques_et_beaute = view.findViewById(R.id.cosmetiques_et_beaute);
        maison_et_fournitures = view.findViewById(R.id.maison_et_fournitures);
        loisirs_et_devertissements = view.findViewById(R.id.loisirs_et_devertissements);
        matiriaux_et_equipements = view.findViewById(R.id.matiriaux_et_equipements);


        Button button44 = view.findViewById(R.id.button3);

        button44.setOnClickListener(v -> {
            Intent goin = new Intent(getActivity(), DetailMesannonce.class);
            startActivity(goin);
        });

        addAnnonce = view.findViewById(R.id.ajou_annonce);
        addAnnonce.setOnClickListener(v -> {
            Intent loaddimage = new Intent(getActivity(), AnnonceActivity.class);
            startActivity(loaddimage);
        });

        google.setOnClickListener(v -> {
            Intent googlemap = new Intent(getActivity(), GoogleMaps.class);
            startActivity(googlemap);
        });
        // init
        firebaseAuth = FirebaseAuth.getInstance();

        linearLayout2.setOnClickListener(v -> {

            DialogFragment singleChoiceDialog = new Single_choice_classification(this);
            singleChoiceDialog.setCancelable(false);
            singleChoiceDialog.show(getActivity().getSupportFragmentManager(), "Single Choice Dialog");
        });

        //recycle view publication annonces
        annonces = new ArrayList<>();
        publicAdapter = new publicationannonceadapt(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(publicAdapter);
        //affichageParDefaut();
        //hna njibha par defaolt
        String[] Choix = getResources().getStringArray(R.array.choix_categorie);
        tout.setText(Choix[0]);
        vehicules.setText(Choix[1]);
        telephones.setText(Choix[2]);
        Automobiles.setText(Choix[3]);
        pieces_detachees.setText(Choix[4]);
        immobilier.setText(Choix[5]);
        vetements.setText(Choix[6]);
        livres.setText(Choix[7]);
        eletronique_et_electromenager.setText(Choix[8]);
        accessoires_de_mode.setText(Choix[9]);
        cosmetiques_et_beaute.setText(Choix[10]);
        maison_et_fournitures.setText(Choix[11]);
        loisirs_et_devertissements.setText(Choix[12]);
        matiriaux_et_equipements.setText(Choix[13]);
        //click
        tout.setOnClickListener(v -> {
            affichageParDefaut();
        });
        vehicules.setOnClickListener(v -> {
            categorie = vehicules.getText().toString();
            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Categorie").child(categorie);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String IDannonce = postSnapshot.getKey();// hna psq nskak ghir key li howa id annonce
                        Log.e("Data here", IDannonce);
                        //annonce
            PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Annonce").child(IDannonce);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count ", "" + snapshot.getChildrenCount());// yhsab ch3al kayan man fils
                        Log.e("Data here", snapshot.toString());
                        Log.e("USER", snapshot.child("userId").getValue().toString());
                        String user = snapshot.child("userId").getValue().toString();

                        if (!user.equals(preferenceUtils.getMember().getIdMembre())) {
                            annonces.add(snapshot.getValue(Annonce.class));
                        }



                    publicAdapter.setMesannonce(annonces);
                    publicAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        });
        telephones.setOnClickListener(v -> {

        });
        Automobiles.setOnClickListener(v -> {

        });
        pieces_detachees.setOnClickListener(v -> {

        });
        immobilier.setOnClickListener(v -> {

        });
        vetements.setOnClickListener(v -> {

        });
        livres.setOnClickListener(v -> {

        });
        eletronique_et_electromenager.setOnClickListener(v -> {

        });
        accessoires_de_mode.setOnClickListener(v -> {

        });
        cosmetiques_et_beaute.setOnClickListener(v -> {

        });
        maison_et_fournitures.setOnClickListener(v -> {

        });
        loisirs_et_devertissements.setOnClickListener(v -> {

        });
        matiriaux_et_equipements.setOnClickListener(v -> {

        });

        // hadi jcp fach khrabt **********************************************************************
      /*editsearch = view.findViewById(R.id.search);////////////////////////////////////////////////////
        editsearch.setOnQueryTextListener(this);*/
        return view;
    }

    private void checkUserStatus() {
        // get current user
        FirebaseUser us = firebaseAuth.getCurrentUser();
        if (us != null) {
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        //check on start of app
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onPositiveButtononCliked(String name) {
        Toast.makeText(getContext(), "Selected item = " + name, Toast.LENGTH_SHORT).show();
        wilaya = name;
        Recherche(searchText, wilaya);
    }

    @Override
    public void onNegativeButtononCliked() {
        Toast.makeText(getContext(), "Dialog cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchText = newText;
        Recherche(searchText, wilaya);
        return false;
    }

    public void Recherche(String keyWord, String wilaya) {
        ArrayList<Annonce> output = new ArrayList<>();
        for (Annonce object : annonces) {
            String obj = object.getTitreAnnonce().toLowerCase();
            if (obj.contains(keyWord.toLowerCase()) && object.getWilaya().toLowerCase().contains(wilaya.toLowerCase()))
                output.add(object);
        }
        publicAdapter.setMesannonce(output);
        publicAdapter.notifyDataSetChanged();
    }

    public void affichageParDefaut() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {// //onDataChange()méthode pour lire un instantané statique du contenu d'un chemin donné!!au moment de l'événement
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("Data here", postSnapshot.toString());
                    Log.e("USER", postSnapshot.child("userId").getValue().toString());
                    String user = postSnapshot.child("userId").getValue().toString();

                    if (!user.equals(preferenceUtils.getMember().getIdMembre())) {
                        annonces.add(postSnapshot.getValue(Annonce.class));
                    }


                }
                publicAdapter.setMesannonce(annonces);
                publicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }

}
