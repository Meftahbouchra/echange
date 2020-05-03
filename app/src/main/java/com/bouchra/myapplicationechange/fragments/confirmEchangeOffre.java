package com.bouchra.myapplicationechange.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.activities.ReviewUser;
import com.bouchra.myapplicationechange.activities.profilUser;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmEchangeOffre extends Fragment {
    private Offre offre;
    private Annonce annonce;
    private TextView titte_offre;
    private ImageView img_offre;
    private RelativeLayout relative_profie;
    private TextView titte_annonce;
    private ImageView img_annonc;
    private LinearLayout layout_annonce;
    private Button Annuler;
    private Button Confirmer;
    private CircleImageView img_user;
    private TextView nom_user;
    private LinearLayout layoyt_button;

    public confirmEchangeOffre() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_confirmechangeoffre, container, false);
        //offer
        titte_offre = view.findViewById(R.id.titte_offre);
        img_offre = view.findViewById(R.id.img_offre);
        //user da aanonce
        img_user = view.findViewById(R.id.img_user);
        nom_user = view.findViewById(R.id.nom_user);
        relative_profie = view.findViewById(R.id.relative_profie);//ta3 win troh l profil ta3 user
        // annonce
        titte_annonce = view.findViewById(R.id.titte_annonce);
        img_annonc = view.findViewById(R.id.img_annonc);
        layout_annonce = view.findViewById(R.id.layout_annonce);//troh l aanoince detaill t3ha

        Annuler = view.findViewById(R.id.Annuler);
        Confirmer = view.findViewById(R.id.Confirmer);
        layoyt_button = view.findViewById(R.id.layoyt_button);

        String fromREview = getArguments().getString("fromREview");
        if (!fromREview.isEmpty()) {
            layoyt_button.setVisibility(View.GONE);
        } else {
            layout_annonce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ajou = new Intent(getContext(), DetailAnnonce.class);
                    ajou.putExtra("annonce", annonce);
                    startActivity(ajou);
                    getActivity().finish();
                }
            });
        }
        offre = (Offre) getArguments().getSerializable("offre");
        // nomAnnonce.setText(annonce.getTitreAnnonce());
        titte_offre.setText(offre.getNomOffre());
        // hadi mzal mndirha la foto ta3 offre
       /* Glide.with(this)
                .asBitmap()
                .load(offre.getImages())
                .into(img_offre);*/


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(offre.getAnnonceId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    String TitreAnnonce = snapshot.child("titreAnnonce").getValue().toString();
                    String ImageAnnoce = snapshot.child("images").child("0").getValue().toString();
                    String UserAnnonce = snapshot.child("userId").getValue().toString();
                    annonce = snapshot.getValue(Annonce.class);

                    titte_annonce.setText(TitreAnnonce);
                    Glide.with(getContext())
                            .asBitmap()
                            .load(ImageAnnoce)
                            .into(img_annonc);

                    recupererUser(UserAnnonce);
                    Log.e("user li dar aanonce is", UserAnnonce);

                } else {
                    Log.e("TAG", " it's null.");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Confirmer.setOnClickListener(v -> {
            //ila kanat assined
            Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(offre.getAnnonceId()).child("statu")
                    .setValue("NEED_To_Be_CONFIRM")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre()).child("statu")
                                        .setValue("NEED_REVIEW")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent review = new Intent(getActivity(), ReviewUser.class);
                                                    review.putExtra("Statu", "wait");
                                                    review.putExtra("offre", offre);//offre
                                                    getActivity().startActivity(review);
// hna mn3tihch win ydir review psq  moul annonce mzal mdrtch confirme , ndirha mlfog

                                                }
                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        });


        return view;

    }


    private void recupererUser(String userAnnonce) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Membre").child(userAnnonce);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    String NameUser = snapshot.child("nomMembre").getValue().toString();
                    String PicUSer = snapshot.child("photoUser").getValue().toString();


                    nom_user.setText(NameUser);
                    Glide.with(getActivity())
                            .asBitmap()
                            .load(PicUSer)
                            .into(img_user);
                    relative_profie.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profil = new Intent(getContext(), profilUser.class);
                            profil.putExtra("user", userAnnonce);
                            startActivity(profil);
                        }
                    });

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