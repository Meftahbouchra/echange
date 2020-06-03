package com.bouchra.myapplicationechange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.annonce.AnnonceActivity;
import com.bouchra.myapplicationechange.adapters.publicationannonceadapt;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Acceuil extends Fragment implements Single_choice_classification.SingleChoiceListener, SearchView.OnQueryTextListener {
    private FirebaseAuth firebaseAuth;
    private LinearLayout selectWilaya;
    private TextView nom_wilaya;
    private RelativeLayout addAnnonce;
    private publicationannonceadapt publicAdapter;
    private ArrayList<Annonce> annonces = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView editsearch;
    private String categorie;
    private TextView informationDafault;
    private TextView informationRecherche;
    private String wilaya = "", searchText = "";
    private TextView tout, vehicules, telephones, Automobiles, pieces_detachees, immobilier, vetements, livres, eletronique_et_electromenager, accessoires_de_mode,
            cosmetiques_et_beaute, maison_et_fournitures, loisirs_et_devertissements, matiriaux_et_equipements;

    public Acceuil() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_acceuil, container, false);
        informationDafault = view.findViewById(R.id.informationDafault);
        informationRecherche = view.findViewById(R.id.informationRecherche);
        selectWilaya = view.findViewById(R.id.layout1);
        nom_wilaya = view.findViewById(R.id.nom_wilaya);
        recyclerView = view.findViewById(R.id.recyle_public);
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
        addAnnonce = view.findViewById(R.id.ajou_annonce);
        firebaseAuth = FirebaseAuth.getInstance();

        addAnnonce.setOnClickListener(v -> {
            Intent loaddimage = new Intent(getActivity(), AnnonceActivity.class);
            startActivity(loaddimage);
        });


//selectionner wilaya pour rechercher des publication
        selectWilaya.setOnClickListener(v -> {
            DialogFragment singleChoiceDialog = new Single_choice_classification(this);
            singleChoiceDialog.setCancelable(false);
            singleChoiceDialog.show(getActivity().getSupportFragmentManager(), "Single Choice Dialog");
        });

        //recycle view publication annonces

        publicAdapter = new publicationannonceadapt(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(publicAdapter);
        informationRecherche.setVisibility(View.VISIBLE);
        informationDafault.setVisibility(View.VISIBLE);
// dafault affichage
        affichageParDefaut();
        // add text to btn categorie
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
            affichageParCategorie(categorie);


        });
        telephones.setOnClickListener(v -> {
            categorie = telephones.getText().toString();
            affichageParCategorie(categorie);

        });
        Automobiles.setOnClickListener(v -> {
            categorie = Automobiles.getText().toString();
            affichageParCategorie(categorie);

        });
        pieces_detachees.setOnClickListener(v -> {
            categorie = pieces_detachees.getText().toString();
            affichageParCategorie(categorie);

        });
        immobilier.setOnClickListener(v -> {
            categorie = immobilier.getText().toString();
            affichageParCategorie(categorie);

        });
        vetements.setOnClickListener(v -> {
            categorie = vetements.getText().toString();
            affichageParCategorie(categorie);

        });
        livres.setOnClickListener(v -> {
            categorie = livres.getText().toString();
            affichageParCategorie(categorie);

        });
        eletronique_et_electromenager.setOnClickListener(v -> {
            categorie = eletronique_et_electromenager.getText().toString();
            affichageParCategorie(categorie);

        });
        accessoires_de_mode.setOnClickListener(v -> {
            categorie = accessoires_de_mode.getText().toString();
            affichageParCategorie(categorie);

        });
        cosmetiques_et_beaute.setOnClickListener(v -> {
            categorie = cosmetiques_et_beaute.getText().toString();
            affichageParCategorie(categorie);

        });
        maison_et_fournitures.setOnClickListener(v -> {
            categorie = maison_et_fournitures.getText().toString();
            affichageParCategorie(categorie);

        });
        loisirs_et_devertissements.setOnClickListener(v -> {
            categorie = loisirs_et_devertissements.getText().toString();
            affichageParCategorie(categorie);

        });
        matiriaux_et_equipements.setOnClickListener(v -> {
            categorie = matiriaux_et_equipements.getText().toString();
            affichageParCategorie(categorie);

        });


      /*editsearch = view.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);*/
        return view;
    }


    @Override
    public void onPositiveButtononCliked(String name) {
        // Toast.makeText(getContext(), "Selected item = " + name, Toast.LENGTH_SHORT).show();
        nom_wilaya.setText(name);
        wilaya = name;
        Recherche(searchText, wilaya);
    }

    @Override
    public void onNegativeButtononCliked() {
        // Toast.makeText(getContext(), "Dialog cancel", Toast.LENGTH_SHORT).show();
    }

    //apeler lorsque user soumet la requete
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //appzle lorsque le text de lan requete est modifier
        searchText = newText;
        Recherche(searchText, wilaya);
        return false;
    }

    public void Recherche(String keyWord, String wilaya) {
        ArrayList<Annonce> output = new ArrayList<>();
        output.clear();
        for (Annonce object : annonces) {
            String obj = object.getTitreAnnonce().toLowerCase();
            if (obj.contains(keyWord.toLowerCase()) && object.getWilaya().toLowerCase().contains(wilaya.toLowerCase())) {
                output.add(object);

            }


        }
        if (output.size() == 0) {
            informationRecherche.setText("Dèsolè ,il n'y a pas d'annonce pour cette recherche actuellement");
            informationDafault.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        } else {
            informationDafault.setVisibility(View.GONE);
            informationRecherche.setVisibility(View.GONE);
            publicAdapter.setMesannonce(output);
            publicAdapter.notifyDataSetChanged();

        }


    }

    public void affichageParDefaut() {
        annonces.clear();
        PreferenceUtils preferenceUtils = new PreferenceUtils(getContext());
        // get all annonces
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
                    String statu = postSnapshot.child("statu").getValue().toString();
                    Log.e("Statu is :", statu);

                    if (!user.equals(preferenceUtils.getMember().getIdMembre())) {
                        if (!statu.equals("NEED_REVIEW") && !statu.equals("COMPLETED")) {
                            annonces.add(postSnapshot.getValue(Annonce.class));

                        }


                    }
                }
                if (annonces.size() == 0) {
                    informationDafault.setText("Pour le moment il n'y a aucune anonce publiè");
                    informationRecherche.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    informationDafault.setVisibility(View.GONE);
                    informationRecherche.setVisibility(View.GONE);
                    publicAdapter.setMesannonce(annonces);
                    publicAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

    }

    public void affichageParCategorie(String categ) {

        annonces.clear();
        Log.e("nom categorie ", categ);
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Categorie").child(categ);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        String IDannonce = id.getKey();
                        Log.e("Data here", IDannonce);

                        //annonces par categorie
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
                                    informationRecherche.setVisibility(View.GONE);
                                    informationDafault.setVisibility(View.GONE);
                                    publicAdapter.setMesannonce(annonces);
                                    publicAdapter.notifyDataSetChanged();
                                } else {
                                    informationRecherche.setText("Dèsolè ,il n'y a pas d'annonce pour cette recherche actuellement");
                                    informationDafault.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }

                        });


                    }


                } else {
                    informationDafault.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    informationRecherche.setText("Dèsolè ,il n'y a pas d'annonce pour cette recherche actuellement");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
