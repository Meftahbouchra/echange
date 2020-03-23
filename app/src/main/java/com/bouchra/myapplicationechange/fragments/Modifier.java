package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bumptech.glide.Glide;


public class Modifier extends Fragment  {
    private EditText nomAnnonce;
    private ImageView imgAnnonc;//
    private EditText desciAnnonce;//
    private EditText editText;
    //
    private TextView textView;
    //

    public Modifier() {// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_modifier, container, false);
        nomAnnonce = view.findViewById(R.id.nom_annonce);
        imgAnnonc = view.findViewById(R.id.img_annonc);
        desciAnnonce = view.findViewById(R.id.desci_annonce);
        editText = view.findViewById(R.id.edittxt_article);
        textView = view.findViewById(R.id.ajout_article);
        textView.setOnClickListener(v -> {
           // ( (Article_en_retour)getActivity()).ajoutArticle();
            Toast.makeText(getContext(), "heloooooooooooo", Toast.LENGTH_SHORT).show();
        });

        //UNPACK OUR DATA FROM OUR BUNDLE
     /*   Bundle b3 = getArguments();
        String name = b3.getString("name");*/
        String titre = this.getArguments().getString("nomannonce");
        String description = this.getArguments().getString("descannonce");
        String image = this.getArguments().getString("imgannonce");
        nomAnnonce.setText(titre);
        desciAnnonce.setText(description);
        Glide.with(this)
                .asBitmap()
                .load(image)
                .into(imgAnnonc);


        return view;

    }



}





