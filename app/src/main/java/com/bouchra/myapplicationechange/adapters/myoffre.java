package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class myoffre extends RecyclerView.Adapter<myoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> mesoffre;
    private ArrayList<Annonce> annonces;


    public myoffre(Context context, ArrayList<Offre> mesoffre, ArrayList<Annonce> annonces) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.annonces = annonces;
    }

    public void setAnnonces(ArrayList<Annonce> annonces) {
        this.annonces = annonces;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.myoffre_layout, parent, false);
        myoffre.ViewHolder h = new myoffre.ViewHolder(vi);
        return h;
    }
    @Override
    public void onBindViewHolder(@NonNull myoffre.ViewHolder holder, int position){
        Offre offre = mesoffre.get(position);
        holder.titte_offre.setText(offre.getNomOffre());
        holder.desc_offre.setText(offre.getDescriptionOffre());
        holder.ville.setText(offre.getWilaya());
        holder.commune.setText(offre.getCommune());

        // hadi ta3 photo mzal mndirha
        /*Glide.with(context)
                .load(annonce.getImages().get(0))
                .centerCrop()
                .into(holder.imgAnnonce);*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(offre.getDateOffre());
        holder.dateh.setText(str);
       /* int i;
        for( i=0; i<=annonces.size();i++){
            Log.e("Data  of annonce here", String.valueOf(annonces.get(i)));
        }*/// hadi drtha bch ntest mkhdmtlich

      // Annonce annonce = annonces.get(position); hhhhhhna drli
        holder.itemView.setOnClickListener(v -> {
           /*Intent affiche = new Intent(context, DetailAnnonce.class);
            affiche.putExtra("annonce", annonce);
            context.startActivity(affiche);*/
           Toast.makeText(context, " hadi ydiha ll annonce li drlha", Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return mesoffre.size() ;


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_offre;
        private TextView titte_offre;
        private TextView desc_offre;
        private TextView dateh;
        private TextView ville;
        private TextView commune;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateh = itemView.findViewById(R.id.datH);
            img_offre = itemView.findViewById(R.id.img_offre);
            titte_offre = itemView.findViewById(R.id.titte_offre);
            desc_offre = itemView.findViewById(R.id.desc_offre);
            ville = itemView.findViewById(R.id.ville);
            commune = itemView.findViewById(R.id.commune);


        }
    }


}
