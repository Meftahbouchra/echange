package com.bouchra.myapplicationechange.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.GoogleMaps;
import com.bouchra.myapplicationechange.activities.MainActivity;
import com.bouchra.myapplicationechange.activities.annonce.AnnonceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Acceuil extends Fragment implements Single_choice_classification.SingleChoiceListener{
    private FirebaseAuth firebaseAuth;

    private LinearLayout linearLayout1,linearLayout2;
    private TextView textView1,textView2;
    private  Button  google;
    private LinearLayout addAnnonce;

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

        google=view.findViewById(R.id.button5);

        addAnnonce=view.findViewById(R.id.ajou_annonce);
        addAnnonce.setOnClickListener(v -> {
            Intent loaddimage = new Intent(getActivity(), AnnonceActivity.class);
            startActivity(loaddimage);
            getActivity().finish();
        });

        google.setOnClickListener(v -> {
            Intent googlemap = new Intent(getActivity(), GoogleMaps.class);
            startActivity(googlemap);
            getActivity().finish();
        });



        // init
        firebaseAuth=FirebaseAuth.getInstance();

        linearLayout2.setOnClickListener(v -> {

            DialogFragment singleChoiceDialog = new Single_choice_classification();
            singleChoiceDialog.setCancelable(false);
            singleChoiceDialog.show(getActivity().getSupportFragmentManager(),"Single Choice Dialog");
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
