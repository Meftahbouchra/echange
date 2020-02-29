package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class publicationannonceadapt extends RecyclerView.Adapter<publicationannonceadapt.ViewHolder> {
    private Context context;
    private ArrayList<Annonce> annonce_publ;

    public publicationannonceadapt(Context context , ArrayList<Annonce> annonce_publ){
        this.context=context;
        this.annonce_publ=annonce_publ;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView ;
        private TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_article);
            textView=itemView.findViewById(R.id.titte_annonce);

        }
    }

    @NonNull
    @Override
    public publicationannonceadapt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi= LayoutInflater.from(context).inflate(R.layout.annoncepublication, parent, false);
        publicationannonceadapt.ViewHolder h = new publicationannonceadapt.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Annonce a=annonce_publ.get(position);
        holder.textView.setText(a.getTitreAnnonce());
      //  holder.imageView.setImageBitmap(a.getImages());
        //Loading image from Glide library.
        Log.e("Url" ,a.getImages().get(0));
        Glide.with(context)
                .load(a.getImages().get(0))
                .centerCrop()
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return annonce_publ.size();
    }
}
