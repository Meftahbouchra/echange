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
import com.bouchra.myapplicationechange.activities.MessageActivity;
import com.bouchra.myapplicationechange.models.Membre;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter<messagesAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Membre> mUsers;
    private String lastMsg;
    private String dateH;


    public messagesAdapter(Context mcontext, ArrayList<Membre> mUsers, String lastMsg) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;
        this.lastMsg = lastMsg;
    }


    public void setLastMsg(String lastMsg, String dateH) {
        this.lastMsg = lastMsg;
        this.dateH = dateH;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item, parent, false);

        return new messagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull messagesAdapter.ViewHolder holder, int position) {

        Membre membre = mUsers.get(position);
        holder.username.setText(membre.getNomMembre());
        Picasso.get().load(membre.getPhotoUser()).into(holder.userimage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mcontext, MessageActivity.class);
            intent.putExtra("user", membre.getIdMembre());//id user sender
            mcontext.startActivity(intent);
        });

        holder.dat_heur.setText(dateH);
        holder.msg.setText(lastMsg);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userimage;
        public TextView username;
        private TextView msg;
        private TextView dat_heur;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.profileomage);
            username = itemView.findViewById(R.id.username);
            msg = itemView.findViewById(R.id.msg);
            dat_heur = itemView.findViewById(R.id.dat_heur);


        }
    }

}