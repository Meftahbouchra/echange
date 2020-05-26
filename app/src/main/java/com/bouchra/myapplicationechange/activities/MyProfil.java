package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.CommentaireAdapter;
import com.bouchra.myapplicationechange.models.Commentaire;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfil extends AppCompatActivity {
    private CircleImageView image_user;
    private TextView nom_user;
    private TextView avis;
    private TextView nbrCommentaire;
    private TextView mail_user;
    private TextView tel_user;
    private TextView adress_user;
    private RecyclerView recyle_commentaire;
    private Button editProfil;
    private Button back;
    private PreferenceUtils preferenceUtils;
    private CommentaireAdapter commentaireAdapter;
    private ArrayList<Commentaire> commentaires;
    private String idUSer;
    private float totalRepos = 0;
    private int nbrComm = 0;
    private TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);


        //Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(profile_img);

        image_user = findViewById(R.id.image_user);
        nom_user = findViewById(R.id.nom_user);

        avis = findViewById(R.id.avis);
        nbrCommentaire = findViewById(R.id.nbrCommentaire);
        recyle_commentaire = findViewById(R.id.recyle_commentaire);
        information = findViewById(R.id.information);
        mail_user = findViewById(R.id.mail_user);
        tel_user = findViewById(R.id.tel_user);
        adress_user = findViewById(R.id.adress_user);

        editProfil = findViewById(R.id.editProfil);
        back = findViewById(R.id.back);
        commentaires = new ArrayList<>();
        commentaireAdapter = new CommentaireAdapter(this, commentaires, idUSer);
        preferenceUtils = new PreferenceUtils(this);

        // user
        Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(image_user);
        nom_user.setText(preferenceUtils.getMember().getNomMembre());
        mail_user.setText(preferenceUtils.getMember().getEmail());
        String number=String.valueOf(preferenceUtils.getMember().getNumTel() );
        if(number.equals("0")){
            tel_user.setText("+123...");
        }else {
            tel_user.setText(String.valueOf(preferenceUtils.getMember().getNumTel()));
        }
        adress_user.setText(preferenceUtils.getMember().getAdresseMembre());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("nom tel :",String.valueOf(preferenceUtils.getMember().getNumTel() ));
            }
        });
        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MyProfil.this, editMyProfil.class));
               finish();
            }
        });

        recyle_commentaire.setLayoutManager(new LinearLayoutManager(this));
        recyle_commentaire.setAdapter(commentaireAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Commentaire").child(preferenceUtils.getMember().getIdMembre());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Log.e("data here", postSnapshot.getValue().toString());
                    String idUserSender = postSnapshot.child("idSender").getValue().toString();
                    String etoile = postSnapshot.child("repos").getValue().toString();
                    //user
                    Log.e("nbr etoile", etoile);
                    idUSer = idUserSender;
                    commentaireAdapter.setIdUSer(idUSer);
                    commentaires.add(postSnapshot.getValue(Commentaire.class));
                    nbrComm++;
                    totalRepos = totalRepos + Float.valueOf(etoile);

                }
             /*   if (nbrComm == 0) {
                    avis.setText("0");
                    nbrCommentaire.setText("0");
                } else {
                    float resultat = totalRepos / nbrComm;
                    avis.setText(String.valueOf(resultat));
                    nbrCommentaire.setText(String.valueOf(nbrComm));
                }*/
                if (commentaires.size() == 0) {
                    avis.setText("0");
                    nbrCommentaire.setText("0");
                    recyle_commentaire.setVisibility(View.GONE);
                    information.setText("Vous n'avez pas des commentaires ");
                } else {
                    float resultat = totalRepos / nbrComm;
                    avis.setText(String.valueOf(resultat));
                    nbrCommentaire.setText(String.valueOf(nbrComm));
                    information.setVisibility(View.GONE);
                    commentaireAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });


    }
}
