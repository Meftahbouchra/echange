package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.myoffre;
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
    private ArrayList<Offre> offres;
    private String idannoncE;
    private RecyclerView recyclerView;
    // SearchView editsearch;
    private String offre;// id annonce
    private TextView information;
    PreferenceUtils preferenceUtils;


    public mesOffres() {
    }

    public mesOffres(String offre) {
        this.offre = offre;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mesoffres, container, false);

        recyclerView = view.findViewById(R.id.recyle_mesoffres);
        information = view.findViewById(R.id.information);
        preferenceUtils = new PreferenceUtils(getContext());
        offres = new ArrayList<>();
        if (offre == null) {
            myoffre = new myoffre(getContext(), offres, idannoncE);
        } else {
            myoffre = new myoffre(getContext(), offres, idannoncE, offre);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myoffre);

// get mes offres
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                offres.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot off : postSnapshot.getChildren()) {
                        String user = off.child("idUser").getValue().toString();
                        String idnonce = off.child("annonceId").getValue().toString();
                        Log.e("user poste offre ", user);
                        Log.e("usr poste annonce-offre", idnonce);
                        if (user.equals(preferenceUtils.getMember().getIdMembre())) {
                            offres.add(off.getValue(Offre.class));
                            myoffre.notifyDataSetChanged();
                            idannoncE = idnonce;

                        }
// hna f dakhal had l requete kont n3ayat l annonce njbd annonce bch yrohlgha kiytovhi 3la l offre aya wlat dirli f array ta3 annonce wlat dirli bali
                        //rahi khawya pssq processeur mrhch y execute sequentielle w rah ydir vue kbal myjib l annonce 3labiha drth f adapter


                    }


                }
                if (offres.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    information.setText("Vous n'avez pas d'offres");


                } else {
                    information.setVisibility(View.GONE);
                    myoffre.setIdAnnonce(idannoncE);
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
