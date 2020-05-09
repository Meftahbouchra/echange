package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModiferOffre extends AppCompatActivity {
    private Offre offre;
    private TextView retour;
    private TextView enregister;
    private EditText titreOffre;
    private ImageView imgOffre;
    private TextView desciOffre;
    private Spinner spinner_wilaya;
    private Spinner spinner_ville;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifer_offre);
        retour = findViewById(R.id.retour);
        enregister = findViewById(R.id.enregister);
        titreOffre = findViewById(R.id.titreOffre);
        imgOffre = findViewById(R.id.imgOffre);
        desciOffre = findViewById(R.id.desciOffre);
        spinner_wilaya = findViewById(R.id.spinner_wilaya);
        spinner_ville = findViewById(R.id.spinner_ville);


        offre = (Offre) getIntent().getSerializableExtra("offre");
        titreOffre.setText(offre.getNomOffre());
       /* Glide.with(this)
                .asBitmap()
                .load(offre.getImages())
                .into(imgOffre);*/
        desciOffre.setText(offre.getDescriptionOffre());
        spinner_ville.setPrompt(offre.getWilaya());
        spinner_wilaya.setPrompt(offre.getCommune());
        enregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAnnonce();
            }
        });
    }
    private void updateAnnonce() {
        String titre_Offre = titreOffre.getText().toString();
        String desc_Offre = desciOffre.getText().toString();

        if (!titre_Offre.isEmpty() && !desc_Offre.isEmpty()) {

            DatabaseReference refannonce = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
            Offre offr = new Offre();
            offr.setAnnonceId(offre.getAnnonceId());
            offr.setDateOffre(offre.getDateOffre());
            offr.setDescriptionOffre(desc_Offre);
            offr.setIdOffre(offre.getIdOffre());
            offr.setNomOffre(titre_Offre);
            offr.setWilaya(offre.getWilaya());
            offr.setCommune(offre.getCommune());
            offr.setIdUser(offre.getIdUser());
            offr.setStatu(offre.getStatu());
            refannonce.setValue(offr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {



                }
            });


        }
    }
}
