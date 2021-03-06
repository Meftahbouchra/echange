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
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewUser extends AppCompatActivity {

    private Button showEchange;
    private Intent ajou;
    private TextView information;
    private RelativeLayout view_review;
    private RatingBar ratingBar;
    private TextView retour;
    private EditText review;
    private Button sumbit;
    private String textREview;
    private Float nbrEtoiles;
    private String IdSender;
    private String IDResiver;
    private DatabaseReference databaseReference;
    private PreferenceUtils preferences;
    private String nameCategorie;

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
        retour = findViewById(R.id.retour);
        preferences = new PreferenceUtils(ReviewUser.this);

        retour.setOnClickListener(v -> finish());
        ajou = getIntent();
        if (ajou != null) {
            if (ajou.hasExtra("Statu")) {
                // show echange apr la confirmation de change
                information.setVisibility(View.VISIBLE);
                view_review.setVisibility(View.GONE);

            } else {
                // get sender and reciver review(from offre, annonce)
                information.setVisibility(View.GONE);
                view_review.setVisibility(View.VISIBLE);
                if (ajou.hasExtra("annonce")) {
                    // add review from annonce
                    Annonce annonce = (Annonce) getIntent().getSerializableExtra("annonce");
                    IdSender = annonce.getUserId();
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
                    // add review from offre
                    Offre offre = (Offre) getIntent().getSerializableExtra("offre");
                    IdSender = offre.getIdUser();
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
        sumbit.setOnClickListener(v -> {
            textREview = review.getText().toString();
            nbrEtoiles = ratingBar.getRating();

            if (nbrEtoiles == 0.0) {
                Toast.makeText(ReviewUser.this, "Toucher une étoile pour noter", Toast.LENGTH_SHORT).show();

            } else {
                if (textREview.isEmpty()) {
                    Toast.makeText(ReviewUser.this, "Ajouter votre commaintaire", Toast.LENGTH_SHORT).show();
                } else {
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

                            if (ajou.hasExtra("annonce")) {
                                // review from annonce,modifier statu annonce to completed
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
                                //get name categorie
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("Categorie");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            for (DataSnapshot key : postSnapshot.getChildren()) {
                                                String IDannonce = key.getKey();
                                                if (IDannonce.equals(offre.getAnnonceId())) {
                                                    String nom = postSnapshot.getKey();
                                                    nameCategorie = nom;
                                                    Log.e("catego", nameCategorie);
                                                }
                                            }


                                        }

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                // daplace offre in historique ey delete in offre,annonce aussi
                                Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre()).child("statu")
                                        .setValue("COMPLETED")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(preferences.getMember().getIdMembre()).child(offre.getIdOffre());
                                                    Map<String, Object> OFFRE = new HashMap<>();
                                                    OFFRE.put("nomOffre", offre.getNomOffre());
                                                    OFFRE.put("idOffre", offre.getIdOffre());
                                                    OFFRE.put("annonceId", offre.getAnnonceId());
                                                    OFFRE.put("commune", offre.getCommune());
                                                    OFFRE.put("dateOffre", offre.getDateOffre());
                                                    OFFRE.put("descriptionOffre", offre.getDescriptionOffre());
                                                    OFFRE.put("idUser", offre.getIdUser());
                                                    OFFRE.put("images", offre.getImage());
                                                    OFFRE.put("wilaya", offre.getWilaya());
                                                    OFFRE.put("statu", "COMPLETEDOFFRE");
                                                    mDbRef.updateChildren(OFFRE);
                                                    deplaceAnnoncewithOffre(offre.getAnnonceId());
                                                    DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
                                                    dOffre.removeValue();
                                                    //  Log.e("id annonce",offre.getAnnonceId());

                                                }
                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(ReviewUser.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                            Intent an = new Intent(ReviewUser.this, debut.class);
                            startActivity(an);
                            finish();

                        }
                    });

                }
            }

        });
        showEchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ajou != null) {
                    String msg = "nonButton";
                    if (ajou.hasExtra("annonce")) {
                        Annonce annonce = (Annonce) getIntent().getSerializableExtra("annonce");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction t = manager.beginTransaction();
                        final confirmEchangeAnnonce m4 = new confirmEchangeAnnonce();
                        Bundle b2 = new Bundle();
                        b2.putSerializable("annonce", annonce);
                        b2.putString("fromReview", msg);
                        m4.setArguments(b2);
                        t.add(R.id.fragment, m4);
                        t.commit();
                    }
                    if (ajou.hasExtra("offre")) {
                        Offre offre = (Offre) getIntent().getSerializableExtra("offre");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction t = manager.beginTransaction();
                        final confirmEchangeOffre confirmEchangeOffre = new confirmEchangeOffre();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("offre", offre);
                        bundle.putString("fromReview", msg);
                        confirmEchangeOffre.setArguments(bundle);
                        t.add(R.id.fragment, confirmEchangeOffre);
                        t.commit();
                    }
                }
            }
        });
    }

    private void deplaceAnnoncewithOffre(String annonceId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(annonceId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Annonce annonce = snapshot.getValue(Annonce.class);
                Log.e("titre annonce", snapshot.toString());
                if(annonce != null) deplaceAnnonce(annonce);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });

    }

    private void deplaceAnnonce(Annonce annonce) {

        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(annonce.getUserId()).child(annonce.getIdAnnonce());
        Map<String, Object> ANNONCE = new HashMap<>();
        ANNONCE.put("idAnnonce", annonce.getIdAnnonce());
        ANNONCE.put("titreAnnonce", annonce.getTitreAnnonce());
        ANNONCE.put("commune", annonce.getCommune());
        ANNONCE.put("wilaya", annonce.getWilaya());
        ANNONCE.put("images", annonce.getImages());
        ANNONCE.put("descriptionAnnonce", annonce.getDescriptionAnnonce());
        ANNONCE.put("articleEnRetour", annonce.getArticleEnRetour());
        ANNONCE.put("dateAnnonce", annonce.getDateAnnonce());
        ANNONCE.put("IdOffreSelected", annonce.getIdOffreSelected());
        ANNONCE.put("statu", "COMPLETEDANNONCE");
        ANNONCE.put("categorie", nameCategorie);
        mDbRef.updateChildren(ANNONCE);
        DatabaseReference dAnnonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
        dAnnonce.removeValue();
        DatabaseReference dCategorie = FirebaseDatabase.getInstance().getReference("Categorie").child(nameCategorie).child(annonce.getIdAnnonce());
        dCategorie.removeValue();
    }

    // offre ne depose pas review pour terminer le cycle
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra("send")) {
            String fromREview = intent.getStringExtra("send");
            Annonce annonce = (Annonce) intent.getSerializableExtra("annonce");
            String msg = "nonButton";
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction t = manager.beginTransaction();
            final confirmEchangeAnnonce m4 = new confirmEchangeAnnonce();
            Bundle b2 = new Bundle();
            b2.putString("fromReview", msg);
            b2.putSerializable("annonce", annonce);
            m4.setArguments(b2);
            t.add(R.id.fragment, m4);
            t.commit();
        }

    }
}
