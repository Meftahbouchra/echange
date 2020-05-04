package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.confirmEchangeAnnonce;
import com.bouchra.myapplicationechange.fragments.confirmEchangeOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Commentaire;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ReviewUser extends AppCompatActivity {

    private Button showEchange;
    private Intent ajou;
    private TextView information;// ta3 bali mknch
    private RelativeLayout view_review;
    private RatingBar ratingBar;
    private EditText review;
    private Button sumbit;
    private String textREview;
    private Float nbrEtoiles;
    private String IdSender;
    private String IDResiver;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_user);
        showEchange = findViewById(R.id.showEchange);
        information = findViewById(R.id.information);
        view_review = findViewById(R.id.view_review);
        ratingBar = findViewById(R.id.ratingBar);
        review = findViewById(R.id.review);
        sumbit = findViewById(R.id.sumbit);


        ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("Statu")) {
                information.setVisibility(View.VISIBLE);
                view_review.setVisibility(View.GONE);

            } else {
                information.setVisibility(View.GONE);
                view_review.setVisibility(View.VISIBLE);
                if (ajou.hasExtra("annonce")) {
                    Annonce annonce = (Annonce) getIntent().getSerializableExtra("annonce");
                    IdSender = annonce.getUserId();// wla shared prefe
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce()).child(annonce.getIdOffreSelected());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {

                                IDResiver = snapshot.child("idUser").getValue().toString();

                            } else {
                                Log.e("TAG", " it's null.");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                if (ajou.hasExtra("offre")) {
                    Offre offre = (Offre) getIntent().getSerializableExtra("offre");
                    IdSender = offre.getIdUser();// hada li dar offre
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Annonce").child(offre.getAnnonceId());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {
                                IDResiver = snapshot.child("userId").getValue().toString();
                            } else {
                                Log.e("TAG", " it's null.");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }


        }
        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textREview = review.getText().toString();
                nbrEtoiles = ratingBar.getRating();
                if (textREview.isEmpty() && nbrEtoiles == 0.0) {
                    Toast.makeText(ReviewUser.this, textREview + "+" + nbrEtoiles, Toast.LENGTH_SHORT).show();
                }
                if (nbrEtoiles == 0.0) {
                    Toast.makeText(ReviewUser.this, "Toucher une étoile pour noter", Toast.LENGTH_SHORT).show();

                } else {
                    if (textREview.isEmpty()) {
                        Toast.makeText(ReviewUser.this, "Ajouter votre commaintaire", Toast.LENGTH_SHORT).show();
                    } else {
                        // kindir commnt w f in srec ncgof mn win rah jay b intent w nbadal statu t3ha
                        Log.e(" lidar commentaire howa", IdSender);
                        Log.e(" lidar commentaire howa", IDResiver);
                        databaseReference = FirebaseDatabase.getInstance().getReference("Commentaire").child(IDResiver);
                        Commentaire commentaire = new Commentaire();
                        commentaire.setIDResiver(IDResiver);
                        commentaire.setIdSender(IdSender);
                        commentaire.setRepos(nbrEtoiles);
                        commentaire.setDateCommentaire(new Date());
                        commentaire.setContenuCommentaire(textREview);
                        commentaire.setIdCommentaire(String.valueOf(commentaire.getDateCommentaire().hashCode()) + commentaire.getIdSender().hashCode());
                        databaseReference.child(String.valueOf(commentaire.getDateCommentaire().hashCode()) + commentaire.getIdSender().hashCode()).setValue(commentaire).addOnCompleteListener(task2 -> {


                            if (task2.isSuccessful()) {
                                Intent an = new Intent(ReviewUser.this, debut.class);
                                startActivity(an);
                                finish();
                                if (ajou.hasExtra("annonce")) {
                                    Annonce annonce = (Annonce) getIntent().getSerializableExtra("annonce");
                                    Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce()).child("statu")
                                            .setValue("COMPLETED")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    }
                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(ReviewUser.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                }
                                if (ajou.hasExtra("offre")) {
                                    Offre offre = (Offre) getIntent().getSerializableExtra("offre");
                                    Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre()).child("statu")
                                            .setValue("COMPLETED")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    }
                                                }

                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(ReviewUser.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }

                            }
                        });

                    }
                }

            }
        });
        showEchange.setOnClickListener(new View.OnClickListener() {
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
