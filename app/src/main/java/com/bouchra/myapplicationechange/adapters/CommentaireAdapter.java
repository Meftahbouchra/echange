package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Commentaire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentaireAdapter extends RecyclerView.Adapter<CommentaireAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Commentaire> commentaires;

    public CommentaireAdapter(Context context, ArrayList<Commentaire> commentaires) {
        this.context = context;
        this.commentaires = commentaires;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     //   return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.commentaire_layout, parent));
        View vi= LayoutInflater.from(context).inflate(R.layout.commentaire_layout, parent, false);
        CommentaireAdapter.ViewHolder h = new CommentaireAdapter.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commentaire p = commentaires.get(position);
        holder.nomUser.setText(p.getNameUser());
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        holder.date.setText(simpleDateFormat.format(p.getDateCommentaire()));
        holder.commentaire.setText(p.getContenuCommentaire());
        holder.etoiles.setRating(p.getRepos());
    }

    @Override
    public int getItemCount() {
        return commentaires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nomUser, date, commentaire;
        private RatingBar etoiles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomUser = itemView.findViewById(R.id.nom_user);
            date = itemView.findViewById(R.id.date_h);
            commentaire = itemView.findViewById(R.id.commentaire);
            etoiles = itemView.findViewById(R.id.etoiles_user);
        }
    }
}
