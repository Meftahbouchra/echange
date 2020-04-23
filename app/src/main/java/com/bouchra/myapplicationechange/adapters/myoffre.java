package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class myoffre extends RecyclerView.Adapter<myoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> mesoffre;
    private Annonce  annonce;
    private String idAnnonce;

    public myoffre(Context context, ArrayList<Offre> mesoffre, String idAnnonce) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.idAnnonce = idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
        notifyDataSetChanged();
    }


  /*  public void setAnnonces(ArrayList<Annonce> annonces) {
        this.annonces = annonces;
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.myoffre_layout, parent, false);
        myoffre.ViewHolder h = new myoffre.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull myoffre.ViewHolder holder, int position) {
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


        // hna njib annonce
        Log.e("ID annonce here", idAnnonce);



        holder.itemView.setOnClickListener(v -> {
            annonce=new Annonce();

            final FirebaseDatabase databas = FirebaseDatabase.getInstance();
            DatabaseReference df = databas.getReference("Annonce").child(idAnnonce);
            df.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

               /* Annonce ann = dataSnapshot3.getValue(Annonce.class);

                annonces.add(ann);
                setAnnonces(annonces);*/
                    // annonce.add(dataSnapshot3.getValue(Annonce.class));

                    // setAnnonces(annonces);
                    annonce=dataSnapshot3.getValue(Annonce.class);
                    Log.e("Data  of annonce here", annonce.getUserId());
                    //intent
                    Intent affiche = new Intent(context, DetailAnnonce.class);
                    affiche.putExtra("annonce", annonce);
                    context.startActivity(affiche);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });


    }

    @Override
    public int getItemCount() {
        return mesoffre.size();


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
