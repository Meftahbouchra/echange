package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.ConfirmEchange;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.activities.ReviewUser;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class myannonce extends RecyclerView.Adapter<myannonce.ViewHolder> {

    private Context context;
    private ArrayList<Annonce> mesannonce;
    private String offre;
    private Task<Void> databasereference;
    private DatabaseReference databaseReference;


    public myannonce(Context context, ArrayList<Annonce> mesannonce) {
        this.context = context;
        this.mesannonce = mesannonce;
    }

    public myannonce(Context context, ArrayList<Annonce> mesannonce, String offre) {
        this.context = context;
        this.mesannonce = mesannonce;
        this.offre = offre;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.myannonce_layout, parent, false);
        myannonce.ViewHolder h = new myannonce.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Annonce annonce = mesannonce.get(position);
        holder.titreAnnonce.setText(annonce.getTitreAnnonce());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  kk:mm ");
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        holder.dateh.setText(str);

        ArrayList<String> images = new ArrayList<>();
        for (String image : annonce.getImages()) {
            images.add(image);
        }
        holder.images.setPageCount(images.size());
        holder.images.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                //imageView.setImageURI(images.get(position));
                Glide.with(context)
                        .load(images.get(position))
                        .centerCrop()
                        .into(imageView);


            }
        });


        switch (annonce.getStatu()) {
            case "CREATED":
                holder.statu.setText("Nouvaux");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
                break;
            case "ATTEND_DE_CONFIRMATION_D_OFFRE":
                holder.statu.setText("attend de confirmation d'offre");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case "ASSINED":
                holder.statu.setText("attribué a un offre");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.bleu));//jaune
                break;
            case "NEED_To_Be_CONFIRM":
                holder.statu.setText("attend de confirmation d 'change");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
                break;
            case "NEED_REVIEW":
                holder.statu.setText("attend de commentaire");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));

                break;
            case "COMPLETED":
                holder.statu.setText("termine, attend de review de partenaire");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;

            default:
                break;
        }

        holder.images.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                onclick(annonce);

            }
        });


        holder.itemView.setOnClickListener(v -> {

            onclick(annonce);
        });


    }

    private void onclick(Annonce annonce) {
        if (offre == null) {
            //non select comme offre
            switch (annonce.getStatu()) {
                case "NEED_To_Be_CONFIRM":
                    Intent affichee = new Intent(context, ConfirmEchange.class);
                    affichee.putExtra("annonce", annonce);
                    context.startActivity(affichee);
                    break;
                case "NEED_REVIEW":
                    Intent review = new Intent(context, ReviewUser.class);
                    review.putExtra("annonce", annonce);
                    context.startActivity(review);
                    break;

                case "COMPLETED":// ndir khod3a nrslha l review w mntamak f onresume nrslahl voir echange psq mnkdrvh n3ayatalh l frgm direct psq hada adapter
                    Intent intent = new Intent(context, ReviewUser.class);
                    intent.putExtra("annonce", annonce);
                    intent.putExtra("send", "khod3a");
                    context.startActivity(intent);

                    break;
                default:
                    //NEED_To_Be_CONFIRM ,NEED_REVIEW , COMPLETED non
                    Intent affiche = new Intent(context, DetailMesannonce.class);
                    affiche.putExtra("annonce", annonce);
                    context.startActivity(affiche);
                    break;

            }


        } else {
            if (annonce.getStatu().equals("NEED_To_Be_CONFIRM")) {
                Toast.makeText(context, "Vous ne peux pas attribuée cette annonce,elle attend d'etre confirme !", Toast.LENGTH_SHORT).show();


            } else {
                if (annonce.getStatu().equals("NEED_REVIEW")) {
                    Toast.makeText(context, "Vous ne peux pas attribuée cette annonce ,elle a été déja échngé !", Toast.LENGTH_SHORT).show();


                } else {

                    new AlertDialog.Builder(context)
                            .setTitle("Echanger votre object")
                            .setMessage("Souhaitez vous vraiment envoyer cette " + annonce.getTitreAnnonce() + " comme un offre ?")
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    PreferenceUtils preferenceUtils = new PreferenceUtils(context);
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(offre);
                                    Offre offrre = new Offre();
                                    offrre.setAnnonceId(offre);
                                    offrre.setDateOffre(new Date());
                                    offrre.setDescriptionOffre(annonce.getDescriptionAnnonce());
                                    offrre.setIdOffre(String.valueOf(offrre.getDateOffre().hashCode()) + offrre.getAnnonceId().hashCode());
                                    offrre.setNomOffre(annonce.getTitreAnnonce());
                                    offrre.setWilaya(annonce.getWilaya());
                                    offrre.setCommune(annonce.getCommune());
                                    offrre.setIdUser(preferenceUtils.getMember().getIdMembre());
                                    offrre.setStatu("CREATED");
                                    offrre.setImage(annonce.getImages().get(0));

                                    databaseReference.child(String.valueOf(offrre.getDateOffre().hashCode()) + offrre.getAnnonceId().hashCode()).setValue(offrre).addOnCompleteListener(task2 -> {

                                        if (task2.isSuccessful()) {
                                            setStatuAnnonce();
                                            addNotification(offre, offrre);
                                            Toast.makeText(context, "Votre offre a été soumise auec succès ", Toast.LENGTH_LONG).show();
                                            Intent an = new Intent(context, debut.class);
                                            context.startActivity(an);

                                        } else {
                                            Toast.makeText(context, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            })

                            .setNegativeButton("Annuler", null)
                            //  .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

            }


        }

    }

    private void addNotification(String idannonce, Offre offre) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(idannonce);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Annonce annonce = snapshot.getValue(Annonce.class);
                DatabaseReference data = FirebaseDatabase.getInstance().getReference("Notification").child(annonce.getUserId());
                Notification notification = new Notification();
                notification.setIdsender(offre.getIdUser());
                notification.setIdreceiver(annonce.getUserId());
                notification.setDateNotification(new Date());
                notification.setContenuNotification("sendOffre");
                notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
                data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });


    }


    private void setStatuAnnonce() {
        databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(offre).child("statu")
                .setValue("NEED_To_Be_CONFIRM")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // get and show proper error message
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {

        return mesannonce.size();
    }

    public ArrayList<Annonce> getMesannonce() {
        return mesannonce;
    }

    public void setMesannonce(ArrayList<Annonce> output) {
        this.mesannonce.clear();
        this.mesannonce.addAll(output);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CarouselView images;
        private TextView titreAnnonce;
        private TextView statu;
        private TextView dateh;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.img_article);
            titreAnnonce = itemView.findViewById(R.id.titte_annonce);
            statu = itemView.findViewById(R.id.statu);
            dateh = itemView.findViewById(R.id.datH);

        }
    }

    // Filter Class
   /* public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        annonces.clear();
        if (charText.length() == 0) {
            annonces.addAll(mesannonce);
        } else {
            for (Annonce wp : mesannonce) {
                if (wp.getTitreAnnonce().toLowerCase(Locale.getDefault()).contains(charText)) {
                    annonces.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/


}
