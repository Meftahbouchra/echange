package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.confirmEchangeAnnonce;
import com.bouchra.myapplicationechange.fragments.confirmEchangeOffre;

public class ConfirmEchange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_echange);

        Intent ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("annonce")) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new confirmEchangeOffre(), "confirmEchangeOffre").commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new confirmEchangeAnnonce(), "confirmEchangeAnnonce").commit();
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
