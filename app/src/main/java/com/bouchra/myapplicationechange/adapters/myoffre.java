package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.ConfirmEchange;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class myoffre extends RecyclerView.Adapter<myoffre.ViewHolder> {

    private Context context;
    private ArrayList<Offre> mesoffre;
    private Annonce annonce;
    private String idAnnonce;
    private String Offre;
    private String statuOffre;
    private Task<Void> databasereference;

    public myoffre(Context context, ArrayList<Offre> mesoffre, String idAnnonce, String statuOffre) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.idAnnonce = idAnnonce;
        this.statuOffre = statuOffre;
    }

    public void setStatuOffre(String statuOffre) {
        this.statuOffre = statuOffre;
        notifyDataSetChanged();
    }

    public myoffre(Context context, ArrayList<Offre> mesoffre, String idAnnonce, String offre, String statuOffre) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.idAnnonce = idAnnonce;
        this.Offre = offre;
        this.statuOffre = statuOffre;
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

        // Log.e("statu oofre ", statuOffre);
        // hna njib annonce
        //Log.e("ID annonce here", offre.getAnnonceId());
       // Log.e("statu oofre ", statuOffre);
     /*   switch (statuOffre) {
            case "CREATED":
                holder.statu.setText("Nouvaux");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
                break;
            case "NEED_To_Be_CONFIRM":
                holder.statu.setText("attend de confirmation d 'change");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
                break;
            default:

        }*/
        Log.e("statu oofre ", statuOffre);
        if(statuOffre.equals("CREATED")){
            holder.statu.setText("Nouvaux");
            holder.statu.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
        }else {
            holder.statu.setText("attend de confirmation d 'change");
            holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
        }

        holder.itemView.setOnClickListener(v -> {
            if (Offre == null) {

                switch (statuOffre) {
                    case "CREATED":
                        annonce = new Annonce();
                        final FirebaseDatabase databas = FirebaseDatabase.getInstance();
                        DatabaseReference df = databas.getReference("Annonce").child(offre.getAnnonceId());
                        df.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                annonce = dataSnapshot3.getValue(Annonce.class);
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

               /* Annonce ann = dataSnapshot3.getValue(Annonce.class);
                annonces.add(ann);
                setAnnonces(annonces);
                annonce.add(dataSnapshot3.getValue(Annonce.class));

                setAnnonces(annonces);*/
                        break;
                    case "NEED_To_Be_CONFIRM":
                        Intent affiche = new Intent(context, ConfirmEchange.class);
                        affiche.putExtra("offre", "b");
                        context.startActivity(affiche);

                        break;


                }


            } else {

                PreferenceUtils preferenceUtils = new PreferenceUtils(context);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(Offre);// khasni nadi id ta3 aanonce machi ta3 id ta3 offre
                Offre offree = new Offre();
                offree.setAnnonceId(Offre);
                offree.setDateOffre(new Date());// jc ila ndirah f date ta3 annonce wl    ndir   h bali jdidi
                offree.setDescriptionOffre(offre.getDescriptionOffre());
                offree.setIdOffre(String.valueOf(offree.getDateOffre().hashCode()) + offree.getAnnonceId().hashCode());
                offree.setNomOffre(offre.getNomOffre());
                offree.setWilaya(offre.getWilaya());
                offree.setCommune(offre.getCommune());
                offree.setIdUser(preferenceUtils.getMember().getIdMembre());
                offree.setStatu("CREATED");
                // offre.setImages();
                // khasni id user li dar l offre

                databaseReference.child(String.valueOf(offree.getDateOffre().hashCode()) + offree.getAnnonceId().hashCode()).setValue(offree).addOnCompleteListener(task2 -> {

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
        databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(Offre).child("statu")
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
        return mesoffre.size();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_offre;
        private TextView titte_offre;
        private TextView desc_offre;
        private TextView dateh;
        private TextView ville;
        private TextView commune;
        private TextView statu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateh = itemView.findViewById(R.id.datH);
            img_offre = itemView.findViewById(R.id.img_offre);
            titte_offre = itemView.findViewById(R.id.titte_offre);
            desc_offre = itemView.findViewById(R.id.desc_offre);
            ville = itemView.findViewById(R.id.ville);
            commune = itemView.findViewById(R.id.commune);
            statu = itemView.findViewById(R.id.statu);


        }
    }


}
