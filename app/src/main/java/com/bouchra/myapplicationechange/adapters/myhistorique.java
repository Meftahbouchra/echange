package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.confirmEchangeAnnonce;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class myhistorique extends RecyclerView.Adapter<myhistorique.ViewHolder> {
    private Context context;
    private ArrayList<Annonce> mesannonce;
    private ArrayList<Offre> offres;
    private ArrayList<String> all = new ArrayList<>();


    public myhistorique(Context context, ArrayList<Annonce> mesannonce, ArrayList<Offre> offres) {
        this.context = context;
        this.mesannonce = mesannonce;
        this.offres = offres;
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
        if (offres.size() != 0) {
            // offre

            Offre offre = offres.get(position);
            holder.titte.setText(offre.getNomOffre());
            holder.desc.setText(offre.getDescriptionOffre());
            holder.ville.setText(offre.getWilaya());
            holder.commune.setText(offre.getCommune());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
            String str = simpleDateFormat.format(offre.getDateOffre());
            holder.datH.setText(str);
            holder.statu.setText(offre.getStatu());
           /* all.add(str);
            all.add(offre.getNomOffre());
            all.add(offre.getDescriptionOffre());
            all.add(offre.getWilaya());
            all.add(offre.getCommune());
            all.add(offre.getImages().get(0));
            all.add(offre.getStatu());*/


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
            }


        }
        if (mesannonce.size() != 0) {
            //annonce
            Log.e("data", mesannonce.toString());
            Annonce annonce = mesannonce.get(position);
            holder.titte.setText(annonce.getTitreAnnonce());
            holder.desc.setText(annonce.getDescriptionAnnonce());
            holder.ville.setText(annonce.getWilaya());
            holder.commune.setText(annonce.getCommune());
            Glide.with(context)
                    .load(annonce.getImages().get(0))
                    .centerCrop()
                    .into(holder.img);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
            String str = simpleDateFormat.format(annonce.getDateAnnonce());
            holder.datH.setText(str);
           /* all.add(str);
            all.add(annonce.getTitreAnnonce());
            all.add(annonce.getDescriptionAnnonce());
            all.add(annonce.getWilaya());
            all.add(annonce.getCommune());
            all.add(annonce.getImages().get(0));
            all.add(annonce.getStatu());*/
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
                        if (annonce.getStatu().equals("COMPLETEDANNONCE")) {
                            String msg = "nonButton";
                            FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
                            FragmentTransaction t = manager.beginTransaction();
                            final confirmEchangeAnnonce m4 = new confirmEchangeAnnonce();
                            Bundle b2 = new Bundle();
                            b2.putSerializable("annonce", annonce);
                            b2.putString("fromReview", msg);
                            m4.setArguments(b2);
                            t.add(R.id.fragment, m4);
                            t.commit();

                        }
            });

        }


    }


    @Override
    public int getItemCount() {
        if (mesannonce.size() == 0 && offres.size() == 0) {
            return 0;

        } else {
            if (mesannonce.size() != 0 && offres.size() == 0) {
                return mesannonce.size();
            } else {
                if (mesannonce.size() == 0 && offres.size() != 0) {
                    return offres.size();
                } else {

                    return 1;
                }
            }

        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titte;
        private ImageView img;
        private TextView desc;
        private TextView ville;
        private TextView commune;
        private TextView datH;
        private TextView statu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titte = itemView.findViewById(R.id.titte);
            img = itemView.findViewById(R.id.img);
            desc = itemView.findViewById(R.id.desc);
            ville = itemView.findViewById(R.id.ville);
            commune = itemView.findViewById(R.id.commune);
            datH = itemView.findViewById(R.id.datH);
            statu = itemView.findViewById(R.id.statu);


        }
    }


}
