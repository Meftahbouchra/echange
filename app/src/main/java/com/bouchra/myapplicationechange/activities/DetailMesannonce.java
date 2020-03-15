package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;

public class DetailMesannonce extends AppCompatActivity  implements DetailAnnonce.affichage {
    private Button voirOffres;
    private Annonce annonce;
    private TextView tite;
    private TextView desc;
    private ImageView img;
    private  TextView  retour;
   private String imageUrl;
  private String imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mesannonce);
        tite=findViewById(R.id.titte_annonce);
        desc=findViewById(R.id.desc);
        img=findViewById(R.id.img_annonc);
        retour=findViewById(R.id.article_retour);

        voirOffres=findViewById(R.id.voir);
        voirOffres.setOnClickListener(v -> {
            Intent voir = new Intent(DetailMesannonce.this, DemandesOffre.class);
            startActivity(voir);
            finish();
        });
       // setImage(imageUrl,imageName);
        tite.setText("abcd");
        getIncomingIntent();
      /*  annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                           // name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            String nomUser=dataSnapshot.child("nomMembre").getValue().toString();
                            Log.e("User is :", nomUser);
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

        Date d = new Date(new Date().getTime() + 28800000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm "); // +heur
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
       time.setText(str);
        setImage(annonce.getImages().get(0), annonce.getTitreAnnonce());
        for (int i = 0; i < annonce.getArticleEnRetour().size(); i++) {
            retour.setText( retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }*/


   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater menu
        inflater.inflate(R.menu.setting_annonce,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handel menu item clicks
        int id= item.getItemId();
        if(id == R.id.modifier){
            Toast.makeText(getActivity(), "modifier", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.Supprimer){
            Toast.makeText(getActivity(), "Supprimer", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }*/
    }



    @Override
    public void getIncomingIntent() {

    }
   /* private void setImage(String imageUrl, String imageName) {


        tite.setText(imageName);


        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }*/
}