package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Commentaire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Commentaire> commentaires;
    private String idUSer;
    private DatabaseReference reference;

    // seet idUser sender
    public void setIdUSer(String idUSer) {
        this.idUSer = idUSer;
        notifyDataSetChanged();

    }

    public CommentaireAdapter(Context context, ArrayList<Commentaire> commentaires, String idUSer) {
        this.context = context;
        this.commentaires = commentaires;
        this.idUSer = idUSer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.commentaire_layout, parent, false);
        CommentaireAdapter.ViewHolder h = new CommentaireAdapter.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commentaire commentaire = commentaires.get(position);
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        holder.date.setText(simpleDateFormat.format(commentaire.getDateCommentaire()));
        holder.commentaire.setText(commentaire.getContenuCommentaire());
        holder.etoiles.setRating(commentaire.getRepos());

        Log.e("id user is", idUSer);
        // get user sender
        reference = FirebaseDatabase.getInstance().getReference("Membre").child(idUSer);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String NAMEUSER = dataSnapshot.child("nomMembre").getValue().toString();
                String IMAGUSER = dataSnapshot.child("photoUser").getValue().toString();
                Picasso.get().load(IMAGUSER).into(holder.img_user);
                holder.nomUser.setText(NAMEUSER);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return commentaires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nomUser, date, commentaire;
        private RatingBar etoiles;
        private CircleImageView img_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomUser = itemView.findViewById(R.id.nom_user);
            date = itemView.findViewById(R.id.date_h);
            commentaire = itemView.findViewById(R.id.commentaire);
            etoiles = itemView.findViewById(R.id.etoiles_user);
            img_user = itemView.findViewById(R.id.img_user);
        }
    }
}
