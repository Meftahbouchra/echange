package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private Spinner wilayaSpinner, villeSpinner;
    private ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();/////////////////////
    private ArrayList<Commune> communes = new ArrayList<Commune>();////////////////
    private String[] wilayaname;///////////////////////////////////
    private DatabaseReference databaseReference;
    private String selectedWilaya, selectedVille;
    private Button annuler;

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
        annuler=findViewById(R.id.annuler);
        annuler.setOnClickListener(v -> {
          Intent an = new Intent(AjoutOffre.this,debut.class);
          startActivity(an);
          finish();
           /* Intent annul = new Intent(AjoutOffre.this, Acceuil.class);
            startActivity(annul);
            finish();*/
        });
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
            if (!titre.isEmpty() && !desc.isEmpty()) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(idAnnc);
                Offre offre = new Offre();
                offre.setAnnonceId(idAnnc);
                offre.setDateOffre(new Date());
                offre.setDescriptionOffre(desc);
                offre.setIdOffre(String.valueOf(offre.getDateOffre().hashCode()) + offre.getAnnonceId().hashCode());
                offre.setNomOffre(titre);
                offre.setWilaya(selectedWilaya.split(" ")[1]);
                offre.setCommune(selectedVille);
                // offre.setImages();
                // khasni id user li dar l offre
                databaseReference.setValue(offre).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {

                        Toast.makeText(this, "Votre offre a été soumise auec succès ", Toast.LENGTH_LONG).show();
                        Intent an = new Intent(AjoutOffre.this,debut.class);
                        startActivity(an);
                        finish();

                    } else {
                        Toast.makeText(this, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(this, "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }
        });

        // spinners

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readFileFromRawDirectory(R.raw.wilayas));
            wilayaname = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    wilaya.add(new Wilaya(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")), jsonArray.getJSONObject(i).getString("nom")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wilayaname[i] = wilaya.get(i).getId() + " " + wilaya.get(i).getName();
            }
            ArrayAdapter<String> wilayaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wilayaname);
            wilayaSpinner.setAdapter(wilayaAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArrayCommune = new JSONArray(readFileFromRawDirectory(R.raw.communes));
            for (int i = 0; i < jsonArrayCommune.length(); i++) {
                communes.add(new Commune(Integer.parseInt(jsonArrayCommune.getJSONObject(i).getString("id")), Integer.parseInt(jsonArrayCommune.getJSONObject(i).getString("wilaya_id")), jsonArrayCommune.getJSONObject(i).getString("nom")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedWilaya = parent.getItemAtPosition(position).toString();
                int selectedId = Integer.parseInt(selectedWilaya.subSequence(0, 2).toString().trim());
                ArrayList<Commune> communeSelected = new ArrayList<Commune>();
                for (int i = 0; i < communes.size(); i++)
                    if (selectedId == communes.get(i).getWilaya_id()) {
                        communeSelected.add(communes.get(i));
                    }
                String[] communeName = new String[communeSelected.size()];
                for (int i = 0; i < communeSelected.size(); i++)
                    communeName[i] = communeSelected.get(i).getName();
                ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, communeName);
                villeSpinner.setAdapter(communeAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        villeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedVille = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private String readFileFromRawDirectory(int resourceId) {
        InputStream iStream = getApplicationContext().getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }
}
