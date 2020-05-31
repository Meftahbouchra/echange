package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.activities.Historique;
import com.bouchra.myapplicationechange.activities.MessageList;
import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.MainActivity;
import com.bouchra.myapplicationechange.activities.MyProfil;
import com.bouchra.myapplicationechange.activities.Notification;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class plus extends Fragment {

    private TextView message;
    private TextView notification;
    private TextView historique;
    private TextView parteger;
    private TextView lougout;
    private LinearLayout Profil;
    private FirebaseAuth firebaseAuth;
    private PreferenceUtils preferenceUtils;
    private CircleImageView profile_pic;
    private TextView nomUser;


    public plus() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_plus, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        message = view.findViewById(R.id.message);
        profile_pic = view.findViewById(R.id.profile_pic);
        nomUser = view.findViewById(R.id.nomUser);
        notification = view.findViewById(R.id.notification);
        historique = view.findViewById(R.id.historique);
        parteger = view.findViewById(R.id.parteger);
        lougout = view.findViewById(R.id.lougout);
        Profil = view.findViewById(R.id.Profil);
        preferenceUtils = new PreferenceUtils(getActivity());

        // show  user information   ²
        nomUser.setText(preferenceUtils.getMember().getNomMembre());
        Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(profile_pic);
        // go to my profile
        Profil.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MyProfil.class));
        });
        //loug out
        lougout.setOnClickListener(v -> {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            preferenceUtils.Clear();
            updateUI();
        });
        //goto notiifcation
        notification.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Notification.class));
            // getActivity().finish(); nn bacg mnshkoch ndiro frame layout f notification pour backvv
        });
        //goto historique
        historique.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Historique.class);
            startActivity(intent);
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
            startActivity(Intent.createChooser(myIntent, "Parteger avec"));

        });


        return view;
    }

    //go to activity principal
    private void updateUI() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();

    }
}
