package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class myannonce extends RecyclerView.Adapter<myannonce.ViewHolder> {

    private Context context;
    private ArrayList<Annonce> mesannonce;
    private ArrayList<Annonce> annonces = new ArrayList<>();
    private String offre;
    Task<Void> databasereference;
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
        //  holder.imageView.setImageBitmap(a.getImages());
        //Loading image from Glide library.
        Log.e("Url", annonce.getImages().get(0));
        Glide.with(context)
                .load(annonce.getImages().get(0))
                .centerCrop()
                .into(holder.imgAnnonce);
        holder.statu.setText(annonce.getStatu());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        holder.dateh.setText(str);

        holder.itemView.setOnClickListener(v -> {
            if (offre == null) {
                Intent affiche = new Intent(context, DetailMesannonce.class);
                affiche.putExtra("annonce", annonce);
                context.startActivity(affiche);
            } else {
                PreferenceUtils preferenceUtils = new PreferenceUtils(context);
                databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(offre);
                Offre offrre = new Offre();
                offrre.setAnnonceId(offre);
                offrre.setDateOffre(new Date());// jc ila ndirah f date ta3 annonce wl    ndir   h bali jdidi
                offrre.setDescriptionOffre(annonce.getDescriptionAnnonce());
                offrre.setIdOffre(String.valueOf(offrre.getDateOffre().hashCode()) + offrre.getAnnonceId().hashCode());
                offrre.setNomOffre(annonce.getTitreAnnonce());
                offrre.setWilaya(annonce.getWilaya());
                offrre.setCommune(annonce.getCommune());
                offrre.setIdUser(preferenceUtils.getMember().getIdMembre());
                offrre.setStatu("Created");
                // offre.setImages();
                // khasni id user li dar l offre

                databaseReference.child(String.valueOf(offrre.getDateOffre().hashCode()) + offrre.getAnnonceId().hashCode()).setValue(offrre).addOnCompleteListener(task2 -> {

                    if (task2.isSuccessful()) {
                        setStatuAnnonce();

                        Toast.makeText(context, "Votre offre a été soumise auec succès ", Toast.LENGTH_LONG).show();
                        Intent an = new Intent(context, debut.class);
                        context.startActivity(an);

                    } else {
                        Toast.makeText(context, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


    }
    private void setStatuAnnonce() {
      databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(offre).child("statu")
                .setValue("attend de confirmation d'offre")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //// Toast.makeText(context, "Un email a ètè envoyè, veuillez consulter votre boite email", Toast.LENGTH_SHORT).show();


                        } else {
                            // Toast.makeText(context, "Échec de l'envoi", Toast.LENGTH_SHORT).show();
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
        private ImageView imgAnnonce;
        private TextView titreAnnonce;
        private TextView statu;
        private TextView dateh;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnnonce = itemView.findViewById(R.id.img_article);
            titreAnnonce = itemView.findViewById(R.id.titte_annonce);
            statu = itemView.findViewById(R.id.statu);
            dateh = itemView.findViewById(R.id.datH);
            relativeLayout = itemView.findViewById(R.id.layout_annonce);

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
