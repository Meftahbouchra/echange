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
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Modifier extends Fragment {
    private EditText nomAnnonce;
    private ImageView imgAnnonc;
    private EditText desciAnnonce;
    private EditText editText;
    private TextView enregister;
    private TextView textView;
    private Annonce annonce;


    public Modifier() {
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
        enregister = view.findViewById(R.id.enregister);

        textView.setOnClickListener(v -> {
            // ( (Article_en_retour)getActivity()).ajoutArticle();
            Toast.makeText(getContext(), "heloooooooooooo", Toast.LENGTH_SHORT).show();
        });

        //UNPACK OUR DATA FROM OUR BUNDLE
      /* Bundle b3 = getArguments();
        String name = b3.getString("name");
       String titre = this.getArguments().getString("nomannonce");*/

        annonce = (Annonce) getArguments().getSerializable("annonce");
        nomAnnonce.setText(annonce.getTitreAnnonce());
        desciAnnonce.setText(annonce.getDescriptionAnnonce());
        Glide.with(this)
                .asBitmap()
                .load(annonce.getImages().get(0))
                .into(imgAnnonc);
        enregister.setOnClickListener(v -> {
            updateAnnonce();
        });
        return view;

    }

    private void updateAnnonce() {
        String titre_Annonce = nomAnnonce.getText().toString();
        String desc_Annonce = desciAnnonce.getText().toString();

        if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty()) {
            ////getting the specified artist reference
            DatabaseReference refannonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
            Annonce ann = new Annonce();
            //updating annonce
            ann.setDescriptionAnnonce(desc_Annonce);//c bn
            ann.setTitreAnnonce(titre_Annonce);// c bn
            ann.setDateAnnonce(annonce.getDateAnnonce());
            ann.setStatu(annonce.getStatu());
            ann.setUserId(annonce.getUserId());
            ann.setIdAnnonce(annonce.getIdAnnonce());
            ann.setImages(annonce.getImages());
            ann.setCommune(annonce.getCommune());////////methode static
            ann.setWilaya(annonce.getWilaya());////////
            ann.setArticleEnRetour(annonce.getArticleEnRetour());//////

            refannonce.setValue(ann);
        } else {
            Toast.makeText(getContext(), "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
        }


    }


}





