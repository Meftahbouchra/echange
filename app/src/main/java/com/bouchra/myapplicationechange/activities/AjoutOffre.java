package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Commune;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.models.Wilaya;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class AjoutOffre extends AppCompatActivity {
    private Button buttonChoosePic;
    private EditText titleObjet;
    private EditText descObjet;
    private ImageView pic;
    private Button suiv;
    private String titre = "";
    private String desc = "";
    private String idAnnc = "";
    private Boolean isSelected = false;////////////////////////////////////
    private Spinner wilayaSpinner, villeSpinner;
    private ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();/////////////////////
    private ArrayList<Commune> communes = new ArrayList<Commune>();////////////////
    private String[] wilayaname;///////////////////////////////////
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_offre);
        buttonChoosePic = findViewById(R.id.selectbtn);
        titleObjet = findViewById(R.id.nom_objet);
        descObjet = findViewById(R.id.desci_object);
        pic = findViewById(R.id.img_offre);
        wilayaSpinner = findViewById(R.id.spinner_wilayaobj);
        villeSpinner = findViewById(R.id.spinner_villeobj);
        suiv = findViewById(R.id.suiv);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonChoosePic.setOnClickListener(v -> {
            //   ImagesStorage imag = new ImagesStorage();
            Toast.makeText(this, "ajouter de tof ", Toast.LENGTH_SHORT).show();
        });


        //njib data
        Intent ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("anonceId")) {
                idAnnc = ajou.getStringExtra("anonceId");
            }
        }

        suiv.setOnClickListener(v -> {
            titre = titleObjet.getText().toString();
            desc = descObjet.getText().toString();
            if (!titre.isEmpty() && !desc.isEmpty() ) {
                 databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(idAnnc);
                Offre offre = new Offre();
                offre.setAnnonceId(idAnnc);
                offre.setDateOffre(new Date());
                offre.setDescriptionOffre(desc);
                offre.setIdOffre(String.valueOf(offre.getDateOffre().hashCode()) + offre.getAnnonceId().hashCode());
                offre.setNomOffre(titre);
                // offre.setImages();
                // offre.setWilaya();
                //  offre.setCommune();
                // khasni id user li dar l offre
                databaseReference.setValue(offre).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {

                        Toast.makeText(this, "votre offre est bien ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(this, "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
