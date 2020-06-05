package com.bouchra.myapplicationechange.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.profilUser;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class demandesoffre extends RecyclerView.Adapter<demandesoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> mesDemandeDoffres;
    private ArrayList<Membre> membres;
    private String nomAnnonce;
    private String annonce;// id offre selected
    private Dialog MyDialog;
    private final static String AddressUSer = "35.697,-0.641";

    public demandesoffre(Context context, ArrayList<Offre> mesDemandeDoffres, ArrayList<Membre> membres, String nomAnnonce, String annonce) {
        this.context = context;

        this.mesDemandeDoffres = mesDemandeDoffres;
        this.membres = membres;
        this.nomAnnonce = nomAnnonce;
        this.annonce = annonce;

    }


    public void setAnnonce(String annonce) {
        this.annonce = annonce;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public demandesoffre.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_demandesoffre, parent, false);
        demandesoffre.ViewHolder viewHolder = new demandesoffre.ViewHolder(view);
        return (viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull demandesoffre.ViewHolder holder, int position) {
        //////////********************************************************  offre
        Offre offre = mesDemandeDoffres.get(position);
        holder.titreOffre.setText(offre.getNomOffre());
        Glide.with(context)
                .load(offre.getImage())
                .centerCrop()
                .into(holder.imageOffre);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  kk:mm ");
        String str = simpleDateFormat.format(offre.getDateOffre());
        holder.timeOffre.setText(str);
        holder.descriptionOffre.setText(offre.getDescriptionOffre());
        holder.villeOffre.setText(offre.getWilaya() + ",");
        holder.communeOffre.setText(offre.getCommune());
/////////////*****************************************************membre
        Membre membre = membres.get(position);
        holder.nameUser.setText(membre.getNomMembre());
        if(membre.getPhotoUser() != null) Picasso.get().load(membre.getPhotoUser()).into(holder.imageUser);
        if (annonce != null) {

        } else {
            annonce = " ";
        }
        Log.e("id offre selected is", annonce);
        if (offre.getIdOffre().equals(annonce)) {
            holder.itemView.setBackgroundResource(R.drawable.card_offre_accepted);

        } else {

            holder.itemView.setBackgroundResource(R.drawable.card_offre_refuse);
        }

        holder.relative_profie.setOnClickListener(v -> {
            Intent profil = new Intent(context, profilUser.class);
            profil.putExtra("user", membre.getIdMembre());
            context.startActivity(profil);
        });


       /* holder.itemView.setOnClickListener(v -> {
            ConfirmeOffre confirmeOffre = new ConfirmeOffre();
            Bundle b2 = new Bundle();
            b2.putSerializable("offre", offre);
            b2.putString("nomAnnonce", nomAnnonce);
            confirmeOffre.setArguments(b2);
            confirmeOffre.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");


        });*/
        holder.position.setOnClickListener(v -> Locatiion());
        holder.imageOffre.setOnClickListener(v -> showImage(offre.getImage()));
        holder.itemView.setOnLongClickListener(v -> {
            ConfirmeOffre confirmeOffre = new ConfirmeOffre();
            Bundle b2 = new Bundle();
            b2.putSerializable("offre", offre);
            b2.putString("nomAnnonce", nomAnnonce);
            confirmeOffre.setArguments(b2);
            confirmeOffre.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");
            return true;
            /*
            return truesignifie que l'événement est consommé. Il est géré. Aucun autre événement de clic ne sera notifié.
return falsesignifie que l'événement n'est pas consommé. Tout autre événement de clic continuera de recevoir des notifications.
Donc, si vous ne voulez onClickpas également être déclenché après un événement onLongClick, vous devriez le faire à return truepartir de l' onLongClickévénement.
             */
        });


    }

    @Override
    public int getItemCount() {
        return mesDemandeDoffres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titreOffre;
        private TextView descriptionOffre;
        private TextView villeOffre;
        private TextView communeOffre;
        private TextView timeOffre;
        private ImageView imageOffre;
        private CircleImageView imageUser;
        private TextView nameUser;
        private RelativeLayout relative_profie;
        private LinearLayout position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titreOffre = itemView.findViewById(R.id.titte_offre);
            descriptionOffre = itemView.findViewById(R.id.desc_offre);
            villeOffre = itemView.findViewById(R.id.ville);
            communeOffre = itemView.findViewById(R.id.commune);
            timeOffre = itemView.findViewById(R.id.datH);
            imageOffre = itemView.findViewById(R.id.img_offre);
            imageUser = itemView.findViewById(R.id.img_user);
            nameUser = itemView.findViewById(R.id.nom_user);
            relative_profie = itemView.findViewById(R.id.relative_profie);
            position = itemView.findViewById(R.id.position);


        }


    }

    public void selectedoffre(String idOffre, Annonce annonce) {

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
        Annonce annonce1 = new Annonce();
        annonce.setArticleEnRetour(annonce.getArticleEnRetour());
        annonce.setImages(annonce.getImages());
        annonce.setWilaya(annonce.getWilaya());
        annonce.setCommune(annonce.getCommune());
        annonce.setIdAnnonce(annonce.getIdAnnonce());
        annonce.setUserId(annonce.getUserId());
        annonce.setStatu("ASSINED");
        annonce.setTitreAnnonce(annonce.getTitreAnnonce());
        annonce.setDescriptionAnnonce(annonce.getDescriptionAnnonce());
        annonce.setDateAnnonce(annonce.getDateAnnonce());

        annonce.setIdOffreSelected(idOffre);

        databaseReference.setValue(annonce1).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                etatConfirmOffre(idOffre, annonce1.getIdAnnonce());

            }
        });


    }

    private void Locatiion() {
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + AddressUSer)); sans marqueur
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + AddressUSer));
        intent.setPackage("com.google.android.apps.maps");// seul app map
        context.startActivity(intent);

    }

    private void etatConfirmOffre(String idOffre, String idAnnonce) {

        Task<Void> databasereference;
        databasereference = FirebaseDatabase.getInstance().getReference("Offre").child(idAnnonce).child(idOffre).child("statu")
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

    private void showImage(String position) {
        View view = LayoutInflater.from(context).inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);
        Glide.with(context)
                .asBitmap()
                .load(position)
                .into(imageView);

        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });
        //full screen
        MyDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MyDialog.setContentView(view);
        MyDialog.show();

    }


}
