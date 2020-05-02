package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.confirmEchangeAnnonce;
import com.bouchra.myapplicationechange.fragments.confirmEchangeOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;

public class ConfirmEchange extends AppCompatActivity {
    private Offre offre;
    private Annonce annonce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_echange);

        Intent ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("annonce")) {
                annonce = (Annonce) getIntent().getSerializableExtra("Annonce");
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction t = manager.beginTransaction();
                final confirmEchangeAnnonce m4 = new confirmEchangeAnnonce();
                Bundle b2 = new Bundle();
                b2.putSerializable("annonce", annonce);
                m4.setArguments(b2);
                t.add(R.id.fragment, m4);
                t.commit();

            } else {
                offre = (Offre) getIntent().getSerializableExtra("offre");
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction t = manager.beginTransaction();
                final confirmEchangeOffre m4 = new confirmEchangeOffre();
                Bundle b2 = new Bundle();
                b2.putSerializable("offre", offre);
                m4.setArguments(b2);
                t.add(R.id.fragment, m4);
                t.commit();


            }
        }

    /* hadi ndiroha f offre
        Intent affiche = new Intent(context, ConfirmEchange.class);
        affiche.putExtra("offre", "b");
        context.startActivity(affiche);*/
    /*hadi ndiroha f anonce
      Intent affiche = new Intent(context, ConfirmEchange.class);
                affiche.putExtra("annonce", "a");
                context.startActivity(affiche);
     */


    }
}
