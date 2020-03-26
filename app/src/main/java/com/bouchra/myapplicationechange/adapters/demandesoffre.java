package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.ConfirmeOffre;
import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class demandesoffre extends RecyclerView.Adapter<demandesoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> offresdemande = new ArrayList<>();
    private ArrayList<Offre> mesDemandeDoffres;
    private ArrayList<Membre> membres;
    private  String nomAnnonce;

    public demandesoffre(Context context, ArrayList<Offre> mesDemandeDoffres, ArrayList<Membre> membres,String nomAnnonce) {
        this.mesDemandeDoffres = mesDemandeDoffres;
        this.membres = membres;
        this.context = context;
       this.nomAnnonce=nomAnnonce;
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
        Offre offre = mesDemandeDoffres.get(position);
        holder.titreOffre.setText(offre.getNomOffre());
        //Loading image from Glide library.
        // la tof mazal mndirha
      /*  Log.e("Url", offre.getImages().get(0));
        Glide.with(context)
                .load(offre.getImages().get(0))
                .centerCrop()
                .into(holder.imageOffre);*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(offre.getDateOffre());
        holder.timeOffre.setText(str);
        holder.descriptionOffre.setText(offre.getDescriptionOffre());
        holder.villeOffre.setText(offre.getWilaya() + ",");
        holder.communeOffre.setText(offre.getCommune());

        Membre membre = membres.get(position);
        holder.nameUser.setText(membre.getNomMembre());
        Picasso.get().load(membre.getPhotoUser()).into(holder.imageUser);

       /* Glide.with(context)
                .load(membre.getPhotoUser())
                .centerCrop()
                .into(holder.imageUser);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmeOffre confirmeOffre = new ConfirmeOffre();
               Bundle b2 = new Bundle();
                b2.putString("nomOffre", offre.getNomOffre());
                b2.putString("nomAnnonce", nomAnnonce);
                confirmeOffre.setArguments(b2);
                confirmeOffre.show(((AppCompatActivity) context).getSupportFragmentManager(), "fragment");

            }
        });
holder.relativeLayout.setBackgroundResource(R.drawable.card_offre_accepted);

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
            relativeLayout=itemView.findViewById(R.id.layout_offre);




        }
    }


}
