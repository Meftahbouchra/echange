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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.GoogleMaps;
import com.bouchra.myapplicationechange.activities.MainActivity;
import com.bouchra.myapplicationechange.activities.annonce.AnnonceActivity;
import com.bouchra.myapplicationechange.adapters.publicationannonceadapt;
import com.bouchra.myapplicationechange.models.Annonce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Acceuil extends Fragment implements Single_choice_classification.SingleChoiceListener{
    private FirebaseAuth firebaseAuth;

    private LinearLayout linearLayout1,linearLayout2;
    private TextView textView1,textView2;
    private  Button  google;
    private RelativeLayout addAnnonce;



    private publicationannonceadapt publicAdapter;
    private ArrayList<Annonce> annonces;
    private RecyclerView recyclerView ;


    public Acceuil(){

   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_acceuil, container, false);
        linearLayout1=view.findViewById(R.id.layout1);
        linearLayout2=view.findViewById(R.id.layout2);
        textView1=view.findViewById(R.id.txt11);
        textView2=view.findViewById(R.id.txt22);
        recyclerView=view.findViewById(R.id.recyle_public);
        google=view.findViewById(R.id.button5);


        addAnnonce=view.findViewById(R.id.ajou_annonce);
        addAnnonce.setOnClickListener(v -> {
            Intent loaddimage = new Intent(getActivity(), AnnonceActivity.class);
            startActivity(loaddimage);
        });

        google.setOnClickListener(v -> {
            Intent googlemap = new Intent(getActivity(), GoogleMaps.class);
            startActivity(googlemap);
        });
 // init
        firebaseAuth=FirebaseAuth.getInstance();

        linearLayout2.setOnClickListener(v -> {

            DialogFragment singleChoiceDialog = new Single_choice_classification();
            singleChoiceDialog.setCancelable(false);
            singleChoiceDialog.show(getActivity().getSupportFragmentManager(),"Single Choice Dialog");
        });

        //recycle view publication annonces
        annonces = new ArrayList<>();
        publicAdapter = new publicationannonceadapt(getContext(), annonces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(publicAdapter);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce");
//Pour lire des données sur un chemin et écouter les modifications,
// utilisez la méthode addValueEventListener() ou addListenerForSingleValueEvent()pour ajouter un ValueEventListenerà un DatabaseReference.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            //onDataChange()méthode pour lire un instantané statique du contenu d'un chemin donné!!au moment de l'événement
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.e("Data here" , postSnapshot.toString());
                    annonces.add(postSnapshot.getValue(Annonce.class));
                    publicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
// Getting model failed, log a message
            }

        });

    return view;
    }
    private  void checkUserStatus(){
        // get current user
        FirebaseUser us =firebaseAuth.getCurrentUser();
        if ( us !=null){
            //user is signed in stay here
// wla nkad hadi ndiha f ga" page lwla  ta3 li win ykhayar facgh  yanscrit kinlkaha m inscri yji l acceuil
        }
        else {
            //user not sign in ,go to main activity " li fiha ykhayar bach y inscrit"
            startActivity(new Intent(getActivity(), MainActivity.class));
           getActivity().finish();
        }


    }

    // nsayi mn3amarch les case memoire kima ta3winydir fb w login w gogle ndirhom f variable whda

    @Override
    public void onStart() {
        //check on start of app
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onPositiveButtononCliked(String[] list, int position) {
        Toast.makeText(getContext(), "Selected item = "+list[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeButtononCliked() {
        Toast.makeText(getContext(), "Dialog cancel", Toast.LENGTH_SHORT).show();
    }
}
