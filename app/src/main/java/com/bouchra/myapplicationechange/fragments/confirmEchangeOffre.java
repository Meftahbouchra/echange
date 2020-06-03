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
import com.bouchra.myapplicationechange.activities.debut;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class confirmEchangeOffre extends Fragment {
    private Offre offre;
    private Annonce annonce;
    private TextView titte_offre;
    private ImageView img_offre;
    private RelativeLayout relative_profie;
    private TextView titte_annonce;
    private LinearLayout layout_annonce;
    private Button Annuler;
    private Button Confirmer;
    private CircleImageView img_user;
    private TextView nom_user;
    private LinearLayout layoyt_button;
    private String fromREview;
    private CarouselView images;


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
        relative_profie = view.findViewById(R.id.relative_profie);//go profil user
        // annonce
        titte_annonce = view.findViewById(R.id.titte_annonce);
        images = view.findViewById(R.id.img_annonc);
        layout_annonce = view.findViewById(R.id.layout_annonce);//go detail annonce

        Annuler = view.findViewById(R.id.Annuler);
        Confirmer = view.findViewById(R.id.Confirmer);
        layoyt_button = view.findViewById(R.id.layoyt_button);

        fromREview = this.getArguments().getString("fromReview", "");
        if (!fromREview.equals("")) {
            // show echange from review , pas de btn confirm ou annuler ,pas detaill annoncec
            layoyt_button.setVisibility(View.GONE);
        } else {
            layout_annonce.setOnClickListener(v -> {
                gotoDetail();
            });
            images.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    gotoDetail();
                }
            });
        }
        offre = (Offre) getArguments().getSerializable("offre");
        titte_offre.setText(offre.getNomOffre());
        Glide.with(this)
                .asBitmap()
                .load(offre.getImage())
                .into(img_offre);

// get annonce
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(offre.getAnnonceId());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {

                    annonce = snapshot.getValue(Annonce.class);
                    titte_annonce.setText(annonce.getTitreAnnonce());
                    ArrayList<String> Images = new ArrayList<>();
                    for (String image : annonce.getImages()) {
                        Images.add(image);
                    }
                    images.setImageListener(new ImageListener() {
                        @Override
                        public void setImageForPosition(int position, ImageView imageView) {

                            Glide.with(getActivity())
                                    .load(Images.get(position))
                                    .centerCrop()
                                    .into(imageView);


                        }
                    });
                    images.setPageCount(Images.size());

                    recupererUser(annonce.getUserId());
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
                                                    review.putExtra("Statu", "wait");// signifier att de autrre commenter
                                                    review.putExtra("offre", offre);
                                                    getActivity().startActivity(review);
                                                    getActivity().finish();

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
        Annuler.setOnClickListener(v -> {
            final FirebaseDatabase data = FirebaseDatabase.getInstance();
            DatabaseReference refere = data.getReference("Offre").child(annonce.getIdAnnonce()).child(annonce.getIdOffreSelected());
            refere.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getValue() != null) {
                        Offre offre = snapshot.getValue(Offre.class);
                        deletOffre(offre);
                        updateEtatAnnonce();
                        Intent intent = new Intent(getActivity(), debut.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();


                    } else {
                        Log.e("TAG", " it's null.");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });
        return view;

    }


    private void gotoDetail() {
        Intent ajou = new Intent(getContext(), DetailAnnonce.class);
        ajou.putExtra("annonce", annonce);
        startActivity(ajou);
        getActivity().finish();
    }

    // gezt user
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
                    Picasso.get().load(PicUSer).into(img_user);
                    relative_profie.setOnClickListener(v -> {
                        Intent profil = new Intent(getContext(), profilUser.class);
                        profil.putExtra("user", userAnnonce);
                        startActivity(profil);
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
        OFFRE.put("image", offre.getImage());
        OFFRE.put("wilaya", offre.getWilaya());
        OFFRE.put("statu", "CANCEL");
        mDbRef.updateChildren(OFFRE);
        DatabaseReference dOffre = FirebaseDatabase.getInstance().getReference("Offre").child(offre.getAnnonceId()).child(offre.getIdOffre());
        dOffre.removeValue();
    }

    private void updateEtatAnnonce() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // il y a des offre
                    Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce()).child("statu")
                            .setValue("ATTEND_DE_CONFIRMATION_D_OFFRE")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    // pas d offre
                    Task<Void> databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce()).child("statu")
                            .setValue("CREATED")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



