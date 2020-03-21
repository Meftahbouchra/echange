package com.bouchra.myapplicationechange;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.models.Membre;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<Membre> mUsers;


    public userAdapter(Context mcontext, ArrayList<Membre> mUsers) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item, parent, false);

        return new userAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.ViewHolder holder, int position) {

        Membre membre = mUsers.get(position);
        holder.username.setText(membre.getNomMembre());
        Glide.with(mcontext).load(membre.getPhotoUser()).into(holder.userimage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mcontext, MessageActivity.class);
            intent.putExtra("user", membre.getIdMembre());
           mcontext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userimage;
        public TextView username;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.profileomage);
            username = itemView.findViewById(R.id.username);


        }
    }

}