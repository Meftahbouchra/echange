package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class myImage extends RecyclerView.Adapter<myImage.ViewHolder> {

    private Context mcontext;
    private ArrayList<String> images;

    public myImage(Context mcontext, ArrayList<String> images) {
        this.mcontext = mcontext;
        this.images = images;
    }

    @NonNull
    @Override
    public myImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(mcontext).inflate(R.layout.myimage, parent, false);
        myImage.ViewHolder h = new myImage.ViewHolder(vi);
        return h;
    }


    @Override
    public void onBindViewHolder(@NonNull myImage.ViewHolder holder, int position) {
        String uri = images.get(position);

        holder.suppImage.setOnClickListener(v -> {
            images.remove(position);
            this.notifyDataSetChanged();
        });
        if(URLUtil.isValidUrl(uri)){
            Picasso.get().load(uri).into(holder.imageAnnonce);
        } else{
            File imgFile = new  File(uri);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageAnnonce.setImageBitmap(myBitmap);
            }
        }



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