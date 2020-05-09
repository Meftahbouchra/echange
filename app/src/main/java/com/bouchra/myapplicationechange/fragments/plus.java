package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.activities.Historique;
import com.bouchra.myapplicationechange.activities.MessageList;
import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.MainActivity;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;

public class plus extends Fragment {
    private TextView annonce;
    private TextView message;
    private TextView notification;
    private TextView historique;
    private TextView parametre;
    private TextView parteger;
    private TextView lougout;

    private FirebaseAuth firebaseAuth;
    private PreferenceUtils preferenceUtils;


    public plus() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_plus, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        //  annonce=view.findViewById(R.id.annonce);
        message = view.findViewById(R.id.message);
        notification = view.findViewById(R.id.notification);
        historique = view.findViewById(R.id.historique);
        parametre = view.findViewById(R.id.parametre);
        parteger = view.findViewById(R.id.parteger);
        lougout = view.findViewById(R.id.lougout);
        preferenceUtils = new PreferenceUtils(getActivity());
        //loug out
        lougout.setOnClickListener(v -> {

            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.signOut();
            // firebaseAuth.signOut();
            preferenceUtils.Clear();
            updateUI();



        });
        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Historique.class);
                startActivity(intent);
            }
        });
        //goto message
        message.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessageList.class);
            startActivity(intent);
        });
        //partager un message
        parteger.setOnClickListener(v -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);

            //this is to get the app link in the playstore without lunching your app
            //  final  String appPackageName = getApplicationContext
            //this is the  sharing part
            myIntent.setType("txt/link");
            String shareBody = "hey ! ";//title text
            String shareSub = "APP NAMe";//the datails of sharing content
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(myIntent, "share using"));// ce le msg

        });


        return view;
    }

    //logout
    private void updateUI() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
       /* Intent intent=new Intent(getContext(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();*/

    }
}
