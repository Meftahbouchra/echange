package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class demandesoffre extends RecyclerView.Adapter<demandesoffre.ViewHolder> {

    private Context context;
    // private ArrayList<Offre> offresdemande = new ArrayList<>();
    private ArrayList<Offre> mesDemandeDoffres;
    private ArrayList<Membre> membres;
    private String nomAnnonce;
    // private  ArrayList<Annonce>annonces;/////////////////////////////////////////////mna njbd id offre li selecterd
    private String annonce;

    public demandesoffre(Context context, ArrayList<Offre> mesDemandeDoffres, ArrayList<Membre> membres, String nomAnnonce, String annonce) {
        this.context = context;

        this.mesDemandeDoffres = mesDemandeDoffres;
        this.membres = membres;
        this.nomAnnonce = nomAnnonce;
        this.annonce = annonce;

    }


    public void setAnnonce(String annonce){
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
        //Loading image from Glide library.
        // la tof mazal mndirha

        Glide.with(context)
                .load(offre.getImage())
                .centerCrop()
                .into(holder.imageOffre);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(offre.getDateOffre());
        holder.timeOffre.setText(str);
        holder.descriptionOffre.setText(offre.getDescriptionOffre());
        holder.villeOffre.setText(offre.getWilaya() + ",");
        holder.communeOffre.setText(offre.getCommune());
/////////////**************************membre
        Membre membre = membres.get(position);
        holder.nameUser.setText(membre.getNomMembre());
        Picasso.get().load(membre.getPhotoUser()).into(holder.imageUser);
// hadi nrmlm sayi psq 3mrnha f lwl drnaha vide wla nkado ndiroha f win ytjibha m fire base
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

       /*if (!annonce.equals(null)) {


           holder.itemView.setBackgroundResource(R.drawable.card_offre_accepted);

        } else {

           holder.itemView.setBackgroundResource(R.drawable.card_offre_refuse);

        }*/
        Log.e("eeeeeeeed offre is", offre.getIdOffre());



       /* Glide.with(context)
                .load(membre.getPhotoUser())
                .centerCrop()
                .into(holder.imageUser);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmeOffre confirmeOffre = new ConfirmeOffre();
                Bundle b2 = new Bundle();
                b2.putSerializable("offre",offre);
                b2.putString("nomAnnonce", nomAnnonce);
                confirmeOffre.setArguments(b2);
                confirmeOffre.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");

            }
        });
//holder.relativeLayout.setBackgroundResource(R.drawable.card_offre_accepted);

    }

    @Override
    public int getItemCount() {
        return mesDemandeDoffres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titreOffre;////////
        private TextView descriptionOffre;
        private TextView villeOffre;
        private TextView communeOffre;
        private TextView timeOffre;////////////
        private ImageView imageOffre;///////////
        private CircleImageView imageUser;//img_user
        private TextView nameUser;//nom_user
        private RelativeLayout relativeLayout;


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
            relativeLayout = itemView.findViewById(R.id.layout_offre);


        }


    }


}
