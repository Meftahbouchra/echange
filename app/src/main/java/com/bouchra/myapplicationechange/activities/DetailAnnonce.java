package com.bouchra.myapplicationechange.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

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
// name_user.setText(PreferenceUtils.getNAme(cont));

       // name_user.setText(PreferenceUtils.getName(c));
       /* String photoUrl = "https://graph.facebook.com/" + facebookUserTd + "/picture?height=500";
        PreferenceUtils.savePassword(photoUrl, this);
        Picasso.get().load(photoUrl).into(profile_img);*/
      // PreferenceUtils.getPhoto(c);
       // Picasso.get().load(PreferenceUtils.getPhoto(c)).into(imgUser);


        txtRetout.setOnClickListener(v -> {

            finish();
        });
        //RECEIVE OUR DATA
       // getIncomingIntent();
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        tite.setText(annonce.getTitreAnnonce());
        desc.setText(annonce.getDescriptionAnnonce());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // +heur
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        time.setText(str);
        setImage(annonce.getImages().get(0),annonce.getTitreAnnonce());
        for(int i = 0 ; i < annonce.getArticleEnRetour().size() ; i++){
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }

    /*
ref= FirebaseDatabase.getInstance().getReference().child("Annonce");
ref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String TITLE=dataSnapshot.child("titreAnnonce").getValue().toString();
        String DES=dataSnapshot.child("descriptionAnnonce").getValue().toString();
    // String RETOUR=dataSnapshot.child("articleEnRetour").getValue().toString();
        String TIME=dataSnapshot.child("dateAnnonce").getValue().toString();
        tite.setText(TITLE);
        desc.setText(DES);
        time.setText(TIME);

    }



    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}); */


    }
   /* private void getIncomingIntent(){
        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")){


            String imageUrl = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");

            setImage(imageUrl, imageName);
        }
    }
*/
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
