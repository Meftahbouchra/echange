package com.bouchra.myapplicationechange.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Commune;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.models.Wilaya;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ModiferOffre extends AppCompatActivity {
    private Offre offre;
    private TextView retour;
    private TextView enregister;
    private EditText titreOffre;
    private ImageView imgOffre;
    private TextView desciOffre;
    private Spinner spinner_wilaya;
    private Spinner spinner_ville;
    private String selectedWilaya, selectedVille;
    private ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();
    private ArrayList<Commune> communes = new ArrayList<Commune>();
    private String[] wilayaname;
    private Dialog MyDialog;

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
        Glide.with(this)
                .asBitmap()
                .load(offre.getImage())
                .into(imgOffre);
        desciOffre.setText(offre.getDescriptionOffre());


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
            spinner_wilaya.setAdapter(wilayaAdapter);
            spinner_wilaya.setSelection(getIndex(spinner_wilaya, offre.getWilaya()));

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


        spinner_wilaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                spinner_ville.setAdapter(communeAdapter);
                spinner_ville.setSelection(getIndexCommune(spinner_ville, offre.getCommune()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        spinner_ville.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedVille = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgOffre.setOnClickListener(v -> showImage(offre.getImage()));

        enregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOffre();
                finish();
            }
        });
        retour.setOnClickListener(v -> finish());
    }

    private int getIndexCommune(Spinner spinner_ville, String commune) {
        for (int i = 0; i < spinner_ville.getCount(); i++) {
            String nom = (String) spinner_ville.getItemAtPosition(i);
            if (nom.equals(commune)) {
                return i;
            }

        }
        return 0;
    }

    private int getIndex(Spinner spinner_wilaya, String wilaya) {
        for (int i = 0; i < spinner_wilaya.getCount(); i++) {
            String nom = (String) spinner_wilaya.getItemAtPosition(i);
            if (nom.split(" ")[1].equals(wilaya)) {
                return i;
            }

        }
        return 0;
    }


    private void updateOffre() {
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
            offr.setWilaya(selectedWilaya.split(" ")[1]);
            offr.setCommune(selectedVille);
            offr.setIdUser(offre.getIdUser());
            offr.setStatu(offre.getStatu());
            offr.setImage(offre.getImage());
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

    private void showImage(String position) {
        View view = getLayoutInflater().inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);
        Glide.with(this)
                .asBitmap()
                .load(position)
                .into(imageView);

        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });
        //full screen
        MyDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MyDialog.setContentView(view);
        MyDialog.show();

    }
}
