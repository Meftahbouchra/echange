package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.annonce.AnnonceActivity;
import com.bouchra.myapplicationechange.fragments.Single_choice_classification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Acceuil extends AppCompatActivity implements Single_choice_classification.SingleChoiceListener{
   private   FirebaseAuth firebaseAuth;
  private Button button_Profil;
 private LinearLayout  linearLayout1,linearLayout2;
private  TextView textView1,textView2;
private  Button esy , google, loadImage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        linearLayout1=findViewById(R.id.layout1);
        linearLayout2=findViewById(R.id.layout2);
        textView1=findViewById(R.id.txt11);
        textView2=findViewById(R.id.txt22);
        esy = findViewById(R.id.button4);
        google=findViewById(R.id.button5);
        loadImage=findViewById(R.id.button6);
        loadImage.setOnClickListener(v -> {
            Intent loaddimage = new Intent(Acceuil.this, AnnonceActivity.class);
            startActivity(loaddimage);
            finish();
        });
        google.setOnClickListener(v -> {
            Intent googlemap = new Intent(Acceuil.this, GoogleMaps.class);
            startActivity(googlemap);
            finish();
        });
        esy.setOnClickListener(v -> {
            Intent ProfilUs = new Intent(Acceuil.this, essai.class);
            startActivity(ProfilUs);
        });


        // init
        firebaseAuth=FirebaseAuth.getInstance();
        button_Profil= findViewById(R.id.button3);
        // hadi nrmlm tkon f navigation ya3ni l menu
        button_Profil.setOnClickListener(v -> {
            Intent ProfilUs = new Intent(Acceuil.this, profilUser.class);
            startActivity(ProfilUs);
        });
        linearLayout2.setOnClickListener(v -> {

            DialogFragment singleChoiceDialog = new Single_choice_classification();
            singleChoiceDialog.setCancelable(false);
            singleChoiceDialog.show(getSupportFragmentManager(),"Single Choice Dialog");
        });
    }
    private  void checkUserStatus(){
        // get current user
        FirebaseUser us =firebaseAuth.getCurrentUser();
        if ( us !=null){
            //user is signed in stay here
// wla nkad hadi ndiha f ga" page lwla  ta3 li win ykhayar facgh  yanscrit kinlkaha m inscri yji l acceuil
        }
        else {
            //user not sign in ,go to main activity " li fiha ykhayar bach y inscrit"
            startActivity(new Intent(Acceuil.this,MainActivity.class));
            finish();
        }


    }

    // nsayi mn3amarch les case memoire kima ta3winydir fb w login w gogle ndirhom f variable whda

    @Override
    protected void onStart() {
        //check on start of app
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onPositiveButtononCliked(String[] list, int position) {
        Toast.makeText(this, "Selected item = "+list[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeButtononCliked() {
        Toast.makeText(this, "Dialog cancel", Toast.LENGTH_SHORT).show();
    }
}