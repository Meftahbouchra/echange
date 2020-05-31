package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.ReviewUser;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Historique;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class myhistorique extends RecyclerView.Adapter<myhistorique.ViewHolder> {
    private Context context;
    private ArrayList<Historique> historiques = new ArrayList<>();


    public myhistorique(Context context, ArrayList<Historique> historiques) {
        this.context = context;
        this.historiques = historiques;
    }


    @NonNull
    @Override
    public myhistorique.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.myhistorique_layout, parent, false);
        myhistorique.ViewHolder h = new myhistorique.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull myhistorique.ViewHolder holder, int position) {
        if (historiques.get(position) instanceof Offre) {
            holder.imageView.setVisibility(View.GONE);
            // offre
            Offre offre = (Offre) historiques.get(position);
            holder.titte.setText(offre.getNomOffre());
            holder.desc.setText(offre.getDescriptionOffre());
            holder.ville.setText(offre.getWilaya() + ", ");
            holder.commune.setText(offre.getCommune());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
            String str = simpleDateFormat.format(offre.getDateOffre());
            holder.datH.setText(str);
            holder.statu.setText(offre.getStatu());

            Glide.with(context)
                    .load(offre.getImage())
                    .centerCrop()
                    .into(holder.imge);

            switch (offre.getStatu()) {
                case "DELETEOFFRE":
                    holder.statu.setText("Offre supprimée");
                    break;
                case "REJECTED":
                    holder.statu.setText("Offre rejetée");
                    break;
                case "COMPLETEDOFFRE":
                    holder.statu.setText("Elle a été échangé");
                    break;
                case "CANCEL":
                    holder.statu.setText("l'échange a été annuler");
                    break;
            }


        }
        if (historiques.get(position) instanceof Annonce) {
            //annonce
            holder.imge.setVisibility(View.GONE);
            Annonce annonce = (Annonce) historiques.get(position);
            holder.titte.setText(annonce.getTitreAnnonce());
            holder.desc.setText(annonce.getDescriptionAnnonce());
            holder.ville.setText(annonce.getWilaya() + ", ");
            holder.commune.setText(annonce.getCommune());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
            String str = simpleDateFormat.format(annonce.getDateAnnonce());
            holder.datH.setText(str);

            // get images
            ArrayList<String> images = new ArrayList<>();
            for (String image : annonce.getImages()) {
                images.add(image);
            }
            holder.imageView.setPageCount(images.size());
            holder.imageView.setImageListener(new ImageListener() {
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
                //statu
                case "DELETEDANNONCE":
                    holder.statu.setText("Annonce supprimée");
                    break;
                case "COMPLETEDANNONCE":
                    holder.statu.setText("Elle a été échangé");
                    break;
            }
            holder.itemView.setOnClickListener(v -> {
                // if combleted  pass intent to review activity (on start ila lktha kayan direct tfwtha), li tniryha troh l echange b string li mybynch les btn
                if (annonce.getStatu().equals("COMPLETEDANNONCE")) {
                    Intent intent = new Intent(context, ReviewUser.class);
                    intent.putExtra("annonce", annonce);
                    intent.putExtra("send", "khod3a");
                    context.startActivity(intent);

                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return historiques.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titte;
        private CarouselView imageView;
        private TextView desc;
        private TextView ville;
        private TextView commune;
        private TextView datH;
        private TextView statu;
        private ImageView imge;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titte = itemView.findViewById(R.id.titte);
            imageView = itemView.findViewById(R.id.img_article);
            desc = itemView.findViewById(R.id.desc);
            ville = itemView.findViewById(R.id.ville);
            commune = itemView.findViewById(R.id.commune);
            datH = itemView.findViewById(R.id.datH);
            statu = itemView.findViewById(R.id.statu);
            imge = itemView.findViewById(R.id.imge);


        }
    }


}
