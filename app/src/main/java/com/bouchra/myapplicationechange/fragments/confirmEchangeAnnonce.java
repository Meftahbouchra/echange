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
import com.bouchra.myapplicationechange.activities.DemandesOffre;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmEchangeAnnonce extends Fragment {
    private LinearLayout layout_annonce;
    private TextView titte_annonce;
    private ImageView img_annonc;
    private RelativeLayout relative_profie;
    private CircleImageView img_user;
    private TextView nom_user;
    private LinearLayout layout_offre;// hadi tadiha l win kayan les offre gaa3 w hada offre b vert bacground
    private TextView titte_offre;
    private ImageView img_offre;
    private Annonce annonce;
    private Button Annuler;
    private Button Confirmer;
    private LinearLayout layoyt_button;
    String fromREview;


    public confirmEchangeAnnonce() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_confirmechangeannonce, container, false);
        // ta3 aanonce ta3i
        layout_annonce = view.findViewById(R.id.layout_annonce);
        titte_annonce = view.findViewById(R.id.titte_annonce);
        img_annonc = view.findViewById(R.id.img_annonc);
        //user de oofre
        relative_profie = view.findViewById(R.id.relative_profie);
        img_user = view.findViewById(R.id.img_user);
        nom_user = view.findViewById(R.id.nom_user);
        //offre
        layout_offre = view.findViewById(R.id.layout_offre);
        titte_offre = view.findViewById(R.id.titte_offre);
        Annuler = view.findViewById(R.id.Annuler);
        Confirmer = view.findViewById(R.id.Confirmer);
        layoyt_button = view.findViewById(R.id.layoyt_button);

        img_offre = view.findViewById(R.id.img_offre);
        fromREview = this.getArguments().getString("fromReview", "");
        if (!fromREview.equals("")) {
            layoyt_button.setVisibility(View.GONE);
        } else {
            layout_annonce.setOnClickListener(v -> {
                Intent affiche = new Intent(getContext(), DetailMesannonce.class);
                affiche.putExtra("annonce", annonce);
                getActivity().startActivity(affiche);
                getActivity().finish();
            });
            layout_offre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hadi ta3 aghbiyaa khasni nrsal ga3 l object ta3 annonce
                    Intent ajou = new Intent(getContext(), DemandesOffre.class);
                    ajou.putExtra("nomAnnonce", annonce.getTitreAnnonce()); //key* value
                    ajou.putExtra("idAnnonce", annonce.getIdAnnonce());
                    ajou.putExtra("descp", annonce.getDescriptionAnnonce());
                    ajou.putExtra("statu", annonce.getStatu());
                    ajou.putExtra("userid", annonce.getUserId());
                    ajou.putExtra("commune", annonce.getCommune());
                    ajou.putExtra("wilaya", annonce.getWilaya());
                    ajou.putStringArrayListExtra("articleret", annonce.getArticleEnRetour());
                    ajou.putStringArrayListExtra("images", annonce.getImages());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm "); // +heur
                    String str = simpleDateFormat.format(annonce.getDateAnnonce());
                    ajou.putExtra("date", str);
                    startActivity(ajou);
                    getActivity().finish();
                }
            });
        }
        annonce = (Annonce) getArguments().getSerializable("annonce");
        Log.e("annonce is ", annonce.getTitreAnnonce());
        titte_annonce.setText(annonce.getTitreAnnonce());
        Glide.with(this)
                .asBitmap()
                .load(annonce.getImages().get(0))
                .into(img_annonc);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce()).child(annonce.getIdOffreSelected());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    String TitleOffre = snapshot.child("nomOffre").getValue().toString();
                    // String ImageOffre = snapshot.child("photoUser").getValue().toString();
                    String IDUser = snapshot.child("idUser").getValue().toString();
                    titte_offre.setText(TitleOffre);
                    recupererUser(IDUser);
                    Log.e("UserOFFreIs", IDUser);
                } else {
                    Log.e("TAG", " it's null.");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Confirmer.setOnClickListener(v -> {
            Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce()).child("statu")
                    .setValue("NEED_REVIEW")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // yanuler ga3 les offre lokhrin
                                rejectedOffres(annonce.getIdAnnonce(), annonce.getIdOffreSelected());
// hna wi ndir win ydir review psq deja lakhor dar confirm w rah ykara3
                                Intent review = new Intent(getActivity(), ReviewUser.class);
                                review.putExtra("annonce", annonce);//offre
                                getActivity().startActivity(review);
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
                   /* Glide.with(getContext())
                            .asBitmap()
                            .load(PicUSer)
                            .into(img_user);*/
                    Picasso.get().load(PicUSer).into(img_user);
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

    private void rejectedOffres(String idAnnonce, String idOFfreSelected) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(idAnnonce);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // njib l object w ndirah fi methode w hadik l methode nsuprimi fiha
                    String idOffre = postSnapshot.child("idOffre").getValue().toString();
                    Log.e("id offre", idOffre);
                    Log.e("id Selected", idOFfreSelected);
                    if (!idOffre.equals(idOFfreSelected)) {
                        Offre offre = postSnapshot.getValue(Offre.class);
                        deletOffre(offre);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });
    }

    private void deletOffre(Offre offre) {
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference("Historique").child(offre.getIdUser()).child(offre.getIdOffre());
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
        OFFRE.put("statu", "REJECTED");
        mDbRef.updateChildren(OFFRE);
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();
    }
}