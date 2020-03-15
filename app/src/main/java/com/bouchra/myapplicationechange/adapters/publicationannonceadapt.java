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
        private ImageView imageView;
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
        Annonce a = annonce_publ.get(position);
        holder.textView.setText(a.getTitreAnnonce());
        //  holder.imageView.setImageBitmap(a.getImages());
        //Loading image from Glide library.
        Log.e("Url", a.getImages().get(0));
        Glide.with(context)
                .load(a.getImages().get(0))
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            Intent affiche = new Intent(context, DetailAnnonce.class);
            affiche.putExtra("annonce", a);
            context.startActivity(affiche);
     /*
                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra("image_url", mImages.get(position));
                intent.putExtra("image_name", mImageNames.get(position));
                mContext.startActivity(intent);*/

        });


    }

    @Override
    public int getItemCount() {
        return annonce_publ.size();
    }
}
