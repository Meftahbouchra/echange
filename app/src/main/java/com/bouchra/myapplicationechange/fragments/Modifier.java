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


public class Modifier extends Fragment {
    private EditText nomAnnonce;
    private ImageView imgAnnonc;
    private EditText desciAnnonce;
    private EditText editText;
    private TextView enregister;
    private TextView textView;


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
     /*   Bundle b3 = getArguments();
        String name = b3.getString("name");*/
        String titre = this.getArguments().getString("nomannonce");
        String description = this.getArguments().getString("descannonce");
        String image = this.getArguments().getString("imgannonce");
        String idannonce = this.getArguments().getString("idanoonce");
        nomAnnonce.setText(titre);
        desciAnnonce.setText(description);
        Glide.with(this)
                .asBitmap()
                .load(image)
                .into(imgAnnonc);

        enregister.setOnClickListener(v -> {
          //updateAnnonce(idannonce);
            Toast.makeText(getContext(), "modifer here", Toast.LENGTH_SHORT).show();
        });
        return view;

    }

   /* private void updateAnnonce(String id) {
        String titre_Annonce = nomAnnonce.getText().toString();
        String desc_Annonce = desciAnnonce.getText().toString();

        if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty()) {
            ////getting the specified artist reference
            DatabaseReference refannonce = FirebaseDatabase.getInstance().getReference("Annonce").child(id);
            Annonce annonce = new Annonce();
            //updating annonce
            annonce.setDescriptionAnnonce(desc_Annonce);
            annonce.setTitreAnnonce(titre_Annonce);
            refannonce.setValue(annonce);
        } else {
            Toast.makeText(getContext(), "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
        }


    }*/


}





