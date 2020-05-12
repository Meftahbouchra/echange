package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.ConfirmEchange;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.activities.ReviewUser;
import com.bouchra.myapplicationechange.activities.debut;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Notification;
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
    private Task<Void> databasereference;

    public myoffre(Context context, ArrayList<Offre> mesoffre, String idAnnonce) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.idAnnonce = idAnnonce;

    }


    public myoffre(Context context, ArrayList<Offre> mesoffre, String idAnnonce, String offre) {
        this.context = context;
        this.mesoffre = mesoffre;
        this.idAnnonce = idAnnonce;
        this.Offre = offre;

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
        switch (offre.getStatu()) {
            case "CREATED":
                holder.statu.setText("Nouvaux");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
                holder.select_manipulation.setVisibility(View.VISIBLE);
                holder.select_manipulation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomsheetManipAnnonceOffre bottomsheet = new BottomsheetManipAnnonceOffre();
                        Bundle b2 = new Bundle();
                        b2.putString("fromOffre", offre.getNomOffre());
                        b2.putSerializable("objectOffre", offre);
                        bottomsheet.setArguments(b2);
                        bottomsheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomsheet.getTag());

                    }
                });
                break;
            case "NEED_To_Be_CONFIRM":
                holder.statu.setText("attend de confirmation d 'change");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
                break;
            case "NEED_REVIEW":
                holder.statu.setText("attend de commentaire");
                holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
                break;
            default:

        }
//NEED_REVIEW
     /*   if (offre.getStatu().equals("CREATED")) {
            holder.statu.setText("Nouvaux");
            holder.statu.setTextColor(ContextCompat.getColor(context, R.color.forest_green));
        } else {
            holder.statu.setText("attend de confirmation d 'change");
            holder.statu.setTextColor(ContextCompat.getColor(context, R.color.rouge));
        }*/

        holder.itemView.setOnClickListener(v -> {
            if (Offre == null) {

                switch (offre.getStatu()) {
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
                        affiche.putExtra("offre", offre);
                        context.startActivity(affiche);


                        break;
                    case "NEED_REVIEW":
                        final FirebaseDatabase databasee = FirebaseDatabase.getInstance();
                        DatabaseReference dff = databasee.getReference("Annonce").child(offre.getAnnonceId());
                        dff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                if (snapshot.getValue() != null) {
                                    String StatuAnnoncE = snapshot.child("statu").getValue().toString();
                                    if (StatuAnnoncE.equals("NEED_To_Be_CONFIRM")) {
                                        Intent review = new Intent(context, ReviewUser.class);
                                        review.putExtra("Statu", "wait");
                                        review.putExtra("offre", offre);//offre
                                        context.startActivity(review);
                                    } else {
                                        Intent review = new Intent(context, ReviewUser.class);
                                        review.putExtra("offre", offre);//offre
                                        context.startActivity(review);
                                    }

                                } else {
                                    Log.e("TAG", " it's null.");

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        break;


                }


            } else {
                if (offre.getStatu().equals("NEED_To_Be_CONFIRM")) {
                    Toast.makeText(context, "Vous ne peux pas attribuée ce offre,il attend d'etre confirme !", Toast.LENGTH_SHORT).show();


                } else {
                    if (offre.getStatu().equals("NEED_REVIEW")) {
                        Toast.makeText(context, "Vous ne peux pas attribuée ce offre,il a été déja échngé !", Toast.LENGTH_SHORT).show();


                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle("Echanger votre object")
                                .setMessage("Souhaitez vous vraiment envoyer cette" + offre.getNomOffre() + "comme un offre ?")
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with send operation

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
                                                addNotification(Offre, offree);
                                                Toast.makeText(context, "Votre offre a été soumise auec succès ", Toast.LENGTH_LONG).show();
                                                Intent an = new Intent(context, debut.class);
                                                context.startActivity(an);

                                            } else {
                                                Toast.makeText(context, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("Annuler", null)
                                //  .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    }

                }

            }


        });


    }

    private void addNotification(String iDannonce, Offre offre) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Annonce").child(iDannonce);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Annonce annonce = snapshot.getValue(Annonce.class);
                DatabaseReference data = FirebaseDatabase.getInstance().getReference("Notification").child(annonce.getUserId());
                Notification notification = new Notification();
                notification.setIdsender(offre.getIdUser());
                notification.setIdreceiver(annonce.getUserId());
                notification.setDateNotification(new Date());
                notification.setContenuNotification("sendOffre");
                notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
                data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });


    }


    private void setStatuAnnonce() {
        databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(Offre).child("statu")
                .setValue("ATTEND_DE_CONFIRMATION_D_OFFRE")
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
        private TextView select_manipulation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateh = itemView.findViewById(R.id.datH);
            img_offre = itemView.findViewById(R.id.img_offre);
            titte_offre = itemView.findViewById(R.id.titte_offre);
            desc_offre = itemView.findViewById(R.id.desc_offre);
            ville = itemView.findViewById(R.id.ville);
            commune = itemView.findViewById(R.id.commune);
            statu = itemView.findViewById(R.id.statu);
            select_manipulation = itemView.findViewById(R.id.select_manipulation);


        }
    }


}
