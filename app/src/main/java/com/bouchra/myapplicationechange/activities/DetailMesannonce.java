package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BottomsheetManipAnnonce;
import com.bouchra.myapplicationechange.fragments.Modifier;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailMesannonce extends AppCompatActivity {
    private Button voirOffres;
    private Annonce annonce;
    private TextView tite;
    private TextView desc;
    private ImageView img;
    private TextView retour;
    private TextView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mesannonce);
        tite = findViewById(R.id.titte_annonce);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img_annonc);
        retour = findViewById(R.id.article_retour);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {
            BottomsheetManipAnnonce bottomsheet = new BottomsheetManipAnnonce();
            bottomsheet.show(getSupportFragmentManager(), "manipAnnonce");


        });
        voirOffres = findViewById(R.id.voir);
        voirOffres.setOnClickListener(v -> {
            Intent ajou = new Intent(DetailMesannonce.this, DemandesOffre.class);
            ajou.putExtra("nomAnnonce", annonce.getTitreAnnonce()); //key* value
            startActivity(ajou);
            finish();

        });
        // setImage(imageUrl,imageName);
        //  tite.setText("abcd");
        // getIncomingIntent();
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            // name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            String nomUser = dataSnapshot.child("nomMembre").getValue().toString();
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
        //   time.setText(str);
        setImage(annonce.getImages().get(0), annonce.getTitreAnnonce());
        for (int i = 0; i < annonce.getArticleEnRetour().size(); i++) {
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }


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


    /*  @Override
      public void getIncomingIntent() {

      }*/
    private void setImage(String imageUrl, String imageName) {


        tite.setText(imageName);


        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }

    public void deleteAnnonce() {
        DatabaseReference dAnnonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());

        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(annonce.getIdAnnonce());
        dAnnonce.removeValue();//removeValue ()  : utilisé pour supprimer les données.
        dOffre.removeValue();

    }

    public void goToFragmentModifier() {
        // getSupportFragmentManager().beginTransaction().add(R.id.fragment,new Modifier(),"Modifier").commit();
        //PACK DATA IN A BUNDLE
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction t = manager.beginTransaction();
        final Modifier m4 = new Modifier();
        Bundle b2 = new Bundle();
        b2.putSerializable("annonce", annonce);
        m4.setArguments(b2);
        t.add(R.id.fragment, m4);
        t.commit();


    }
}