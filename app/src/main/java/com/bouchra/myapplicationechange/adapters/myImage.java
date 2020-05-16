package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class myImage extends RecyclerView.Adapter<myImage.ViewHolder> {

    private Context mcontext;
    private ArrayList<Uri> images;

    public myImage(Context mcontext, ArrayList<Uri> images) {
        this.mcontext = mcontext;
        this.images = images;
    }

    @NonNull
    @Override
    public myImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vi = LayoutInflater.from(mcontext).inflate(R.layout.myimage, parent, false);
        return new myImage.ViewHolder(vi);
    }


    @Override
    public void onBindViewHolder(@NonNull myImage.ViewHolder holder, int position) {
        Uri uri = images.get(position);
      //holder.imageAnnonce.setImageURI(uri);//ta3tina Unable to decode stream: java.io.FileNotFoundException , aya ndiro picasso
        holder.suppImage.setOnClickListener(v -> {
            images.remove(position);
            this.notifyDataSetChanged();

        });
       Picasso.get().load(uri).into(holder.imageAnnonce);



    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageAnnonce;
        private ImageButton suppImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAnnonce = itemView.findViewById(R.id.imageannonce);
            suppImage = itemView.findViewById(R.id.suppImage);


        }
    }


}

