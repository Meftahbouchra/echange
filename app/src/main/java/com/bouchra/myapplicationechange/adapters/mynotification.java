package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.ConfirmEchange;
import com.bouchra.myapplicationechange.activities.DemandesOffre;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class mynotification extends RecyclerView.Adapter<mynotification.ViewHolder> {
    private Context context;
    private ArrayList<Notification> notifications;

    public mynotification(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.mynotification_layout, parent, false);
        mynotification.ViewHolder h = new mynotification.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull mynotification.ViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(notification.getDateNotification());
        holder.datH.setText(str);


        holder.itemView.setOnClickListener(v -> {
            if (notification.getContenuNotification().equals("acceptOffre")) {
                //get offre accepted, go to cinfirm ecghng
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Offre");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot off : dataSnapshot.getChildren()) {
                                String userId = off.child("idUser").getValue().toString();
                                if (notification.getIdreceiver().equals(userId)) {
                                    Offre offre = off.getValue(Offre.class);
                                    if (offre.getStatu().equals("NEED_To_Be_CONFIRM")) {
                                        Intent affiche = new Intent(context, ConfirmEchange.class);
                                        affiche.putExtra("offre", offre);
                                        context.startActivity(affiche);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                if (notification.getContenuNotification().equals("updateAnnonce")) {
                    // notofocation l annonce a ete modifier, go to this anonce
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Annonce");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                String userId = dataSnapshot.child("userId").getValue().toString();
                                if (notification.getIdsender().equals(userId)) {
                                    Annonce annonce = dataSnapshot.getValue(Annonce.class);
                                    if (annonce.getStatu().equals("ATTEND_DE_CONFIRMATION_D_OFFRE") || annonce.getStatu().equals("ASSINED")) {
                                        Intent affiche = new Intent(context, DetailAnnonce.class);
                                        affiche.putExtra("annonce", annonce);
                                        context.startActivity(affiche);
                                    }

                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    // si offre exist(!supp)
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Offre");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot offre : dataSnapshot.getChildren()) {

                                    String userIdSender = offre.child("idUser").getValue().toString();
                                    if (notification.getIdsender().equals(userIdSender)) {
                                        final FirebaseDatabase data = FirebaseDatabase.getInstance();
                                        DatabaseReference refdrec = data.getReference("Annonce");
// nov orfre , go to accept offre
                                        refdrec.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                    String userId = dataSnapshot.child("userId").getValue().toString();
                                                    if (notification.getIdreceiver().equals(userId)) {
                                                        Annonce annonce = dataSnapshot.getValue(Annonce.class);
                                                        if (annonce.getStatu().equals("ATTEND_DE_CONFIRMATION_D_OFFRE") || annonce.getStatu().equals("ASSINED")) {
                                                            Intent ajou = new Intent(context, DemandesOffre.class);
                                                            ajou.putExtra("annonce", annonce);
                                                            context.startActivity(ajou);
                                                        }

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
// informatios sender
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Membre").child(notification.getIdsender());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String nameUser = snapshot.child("nomMembre").getValue().toString();
                String PicUser = snapshot.child("photoUser").getValue().toString();
                Picasso.get().load(PicUser).into(holder.profile_image);
                // contenu of notification
                String contenu = notification.getContenuNotification();
                String smgaccept = " a accepté votre offre.";
                String msgenvoyer = " a envoyer une offre pour votre annonce.";
                String mgdUpdate = " a modifier une annonce .";

                if (contenu.equals("acceptOffre")) {
                    String MSG = "<b><font color=#000>" + nameUser + "</font></b>" + smgaccept;
                    holder.showNotification.setText(Html.fromHtml(MSG));

                } else {
                    if (notification.getContenuNotification().equals("updateAnnonce")) {
                        String MSG = "<b><font color=#000>" + nameUser + "</font></b>" + mgdUpdate;
                        holder.showNotification.setText(Html.fromHtml(MSG));

                    } else {
                        String MSG = "<b><font color=#000>" + nameUser + "</font></b>" + msgenvoyer;
                        holder.showNotification.setText(Html.fromHtml(MSG));
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profile_image;
        private TextView showNotification;
        private TextView datH;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            showNotification = itemView.findViewById(R.id.showNotification);
            datH = itemView.findViewById(R.id.datH);


        }
    }
}
