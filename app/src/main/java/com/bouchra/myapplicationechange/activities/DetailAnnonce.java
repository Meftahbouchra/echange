package com.bouchra.myapplicationechange.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAnnonce extends AppCompatActivity {
private  RelativeLayout relativeLayout;//////////////////////////////////////profil
    private TextView tite ;
    private TextView desc ;
    private Annonce annonce;
    private ImageView img ;
    private TextView retour ;
    private TextView time;
    private DatabaseReference ref;
    private TextView txtRetout;
    private TextView name_user;
    private CircleImageView imgUser;
    private  Context cont;
    private PreferenceUtils preferenceUtils;
    private Membre membre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_annonce);
        relativeLayout=findViewById(R.id.relative_profie);
        // go to prifil
        relativeLayout.setOnClickListener(v -> {
            startActivity(new Intent(DetailAnnonce.this, profilUser.class));
            finish();
        });

        initViews();

        txtRetout.setOnClickListener(v -> {
            finish();
        });
        //RECEIVE OUR DATA
       // getIncomingIntent();
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            Log.e("User is :", dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tite.setText(annonce.getTitreAnnonce());
        desc.setText(annonce.getDescriptionAnnonce());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // +heur
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        time.setText(str);
        setImage(annonce.getImages().get(0),annonce.getTitreAnnonce());
        for(int i = 0 ; i < annonce.getArticleEnRetour().size() ; i++){
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }

    }

    private void setImage(String imageUrl, String imageName){



        tite.setText(imageName);


        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }
    private void initViews() {

        txtRetout=findViewById(R.id.retour);
        time=findViewById(R.id.date_h);
        tite = findViewById(R.id.titte_annonce);
        desc=findViewById(R.id.desc);
        img=findViewById(R.id.img_annonc);
        retour=findViewById(R.id.article_retour);
        name_user=findViewById(R.id.nom_user);
        imgUser=findViewById(R.id.img_user);

    }


}
