package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class demandesoffre extends RecyclerView.Adapter<demandesoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> offresdemande=new ArrayList<>();
    private ArrayList<Offre>mesDemandeDoffres;
    private ArrayList<Membre>membres;

    public demandesoffre(Context context, ArrayList<Offre> mesDemandeDoffres, ArrayList<Membre>membres) {
        this.context = context;
        this.mesDemandeDoffres = mesDemandeDoffres;
        this.membres=membres;
    }

    @NonNull
    @Override
    public demandesoffre.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_demandesoffre,parent,false);
        demandesoffre.ViewHolder viewHolder=new demandesoffre.ViewHolder(view);
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
       holder.villeOffre.setText(offre.getWilaya()+",");
       holder.communeOffre.setText(offre.getCommune());

       Membre membre= membres.get(position);
       holder.nameUser.setText(membre.getNomMembre());
       //     Picasso.get().load(photouser).into(imgUser);
      //  holder.imageUser.setImageURI();
        Glide.with(context)
                .load(membre.getPhotoUser())
                .centerCrop()
                .into(holder.imageUser);



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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titreOffre=itemView.findViewById(R.id.titte_offre);
            descriptionOffre=itemView.findViewById(R.id.desc_offre);
            villeOffre=itemView.findViewById(R.id.ville);
            communeOffre=itemView.findViewById(R.id.commune);
            timeOffre=itemView.findViewById(R.id.datH);
            imageOffre=itemView.findViewById(R.id.img_offre);
            imageUser=itemView.findViewById(R.id.img_user);
            nameUser=itemView.findViewById(R.id.nom_user);




        }
    }
}
