package com.bouchra.myapplicationechange.activities.annonce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Commune;
import com.bouchra.myapplicationechange.models.Wilaya;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class AnnonceActivity extends AppCompatActivity {
    private Button next, annuler;
    private EditText titreAnnonce, descAnnonce;
    private String titre_Annonce = "";
    private String selectedWilaya, selectedVille, selectedCateg;
    private String desc_Annonce = "";
    private Boolean isSelected = false;
    private Spinner wilayaSpinner, villeSpinner;
    ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();
    ArrayList<Commune> communes = new ArrayList<Commune>();
    String[] wilayaname;
    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);

        titreAnnonce = findViewById(R.id.nom_annonce);
        descAnnonce = findViewById(R.id.desci_annonce);
        next = findViewById(R.id.next);
        villeSpinner = findViewById(R.id.spinner_ville);
        wilayaSpinner = findViewById(R.id.spinner_wilaya);
        annuler = findViewById(R.id.annuler);
        preferenceUtils = new PreferenceUtils(this);
        annuler.setOnClickListener(v -> {
            Intent annul = new Intent(AnnonceActivity.this, debut.class);
            startActivity(annul);
            finish();
        });
        // btn ajouter des article en retour
        next.setOnClickListener(v -> {
            titre_Annonce = titreAnnonce.getText().toString();
            desc_Annonce = descAnnonce.getText().toString();


            if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty() && isSelected) {
                Annonce annonce = new Annonce();
                annonce.setTitreAnnonce(titre_Annonce);
                annonce.setDescriptionAnnonce(desc_Annonce);
                annonce.setDateAnnonce(new Date());
                annonce.setStatu("CREATED");
                annonce.setIdOffreSelected("vide");
                annonce.setUserId(preferenceUtils.getMember().getIdMembre());
                annonce.setWilaya(selectedWilaya.split(" ")[1]);
                annonce.setCommune(selectedVille);
                annonce.setIdAnnonce(String.valueOf(annonce.getDateAnnonce().hashCode()) + annonce.getUserId().hashCode());
                Intent ajou = new Intent(AnnonceActivity.this, ImagesStorage.class);
                ajou.putExtra("annonce", annonce); //key* value
                ajou.putExtra("categorie", selectedCateg);
                startActivity(ajou);
                finish();
            } else {
                Toast.makeText(this, "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }

        });

        //Spinner

        Spinner spinner = findViewById(R.id.spinner_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choix_categorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


                selectedCateg = parent.getItemAtPosition(position).toString();

                    isSelected = true;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
