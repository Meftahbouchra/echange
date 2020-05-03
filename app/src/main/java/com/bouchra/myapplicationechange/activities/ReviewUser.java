package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.confirmEchangeAnnonce;
import com.bouchra.myapplicationechange.fragments.confirmEchangeOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;

public class ReviewUser extends AppCompatActivity {
    private TextView chklazwja;//lwla fiha prblm ta3 hadik li mtbnch
    private Button chkla;
    Intent ajou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_user);
        chkla = findViewById(R.id.chkla);
        chklazwja = findViewById(R.id.chklazwja);
        // min njin intent b wahad

        ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("Statu")) {
                //  chkla.setVisibility(View.VISIBLE);
                chklazwja.setVisibility(View.GONE);


            } else {

                chklazwja.setVisibility(View.VISIBLE);
                // chkla.setVisibility(View.GONE);

            }


        }
        chkla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ajou != null) {
                    if (ajou.hasExtra("annonce")) {
                        Annonce annonce = (Annonce) getIntent().getSerializableExtra("annonce");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction t = manager.beginTransaction();
                        final confirmEchangeAnnonce m4 = new confirmEchangeAnnonce();
                        Bundle b2 = new Bundle();
                        b2.putSerializable("annonce", annonce);
                        b2.putString("fromREview", "nonButton");
                        m4.setArguments(b2);
                        t.add(R.id.fragment, m4);
                        t.commit();
                    }
                    if (ajou.hasExtra("offre")) {
                        Offre offre = (Offre) getIntent().getSerializableExtra("offre");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction t = manager.beginTransaction();
                        final confirmEchangeOffre m4 = new confirmEchangeOffre();
                        Bundle b2 = new Bundle();
                        b2.putSerializable("offre", offre);
                        b2.putString("fromREview", "nonButton");
                        m4.setArguments(b2);
                        t.add(R.id.fragment, m4);
                        t.commit();
                    }
                }
            }
        });
    }
}
