package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailAnnonce;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class publicationannonceadapt extends RecyclerView.Adapter<publicationannonceadapt.ViewHolder> {
    private Context context;
    private ArrayList<Annonce> annonce_publ;

    public publicationannonceadapt(Context context, ArrayList<Annonce> annonce_publ) {
        this.context = context;
        this.annonce_publ = annonce_publ;

    }

    public void setMesannonce(ArrayList<Annonce> output) {
        this.annonce_publ.clear();
        this.annonce_publ.addAll(output);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CarouselView imageView;
        private TextView textView;
        private RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_article);
            textView = itemView.findViewById(R.id.titte_annonce);
            relativeLayout = itemView.findViewById(R.id.layout_annonce);


        }
    }

    @NonNull
    @Override
    public publicationannonceadapt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.annoncepublication, parent, false);
        publicationannonceadapt.ViewHolder h = new publicationannonceadapt.ViewHolder(vi);


        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.e("nbr des annonce ", (String.valueOf(annonce_publ.size())));
        Annonce a = annonce_publ.get(position);
        holder.textView.setText(a.getTitreAnnonce());
        //  holder.imageView.setImageBitmap(a.getImages());
        //Loading image from Glide library.
        Log.e("Url", a.getImages().get(0));
        ArrayList<String> images = new ArrayList<>();
        for (String image : a.getImages()) {
            images.add(image);
        }
        holder.imageView.setPageCount(images.size());
        holder.imageView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                //imageView.setImageURI(images.get(position));
                Glide.with(context)
                        .load(images.get(position))
                        .centerCrop()
                        .into(imageView);


            }
        });
        holder.imageView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Intent affiche = new Intent(context, DetailAnnonce.class);
                affiche.putExtra("annonce", a);
                context.startActivity(affiche);

            }
        });
     /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent affiche = new Intent(context, DetailAnnonce.class);
                //  affiche.putExtra("annonce", onBindViewHolder.a);
                context.startActivity(affiche);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return annonce_publ.size();
    }
}
