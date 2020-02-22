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
import com.bouchra.myapplicationechange.models.Annonce;

import java.util.Date;

public class AnnonceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button next;
    private EditText titreAnnonce , descAnnonce;
    private String titre_Annonce = "";
    private String desc_Annonce = "";
    private Boolean isSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce);
        titreAnnonce=findViewById(R.id.nom_annonce);
        descAnnonce=findViewById(R.id.desci_annonce);
        next = findViewById(R.id.next);
        // btn ajouter des article en retour
        next.setOnClickListener(v -> {
            titre_Annonce = titreAnnonce.getText().toString();
            desc_Annonce = descAnnonce.getText().toString();

            if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty() && isSelected){
                Annonce annonce = new Annonce();
                annonce.setTitreAnnonce(titre_Annonce);
                annonce.setDescriptionAnnonce(desc_Annonce);
                annonce.setDateAnnonce(new Date());
                annonce.setStatu("created");
                annonce.setUserId("user_id");
                Intent ajou = new Intent(AnnonceActivity.this, ImagesStorage.class);
                ajou.putExtra("annonce",annonce); //key* value
                startActivity(ajou);
                finish();
            }else{
                Toast.makeText(this, "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            }

        });

        //Spinner
        Spinner spinner = findViewById(R.id.spinner_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choix_categorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        isSelected = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        isSelected = false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
