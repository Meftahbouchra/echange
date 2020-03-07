package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class myannonce extends RecyclerView.Adapter<myannonce.ViewHolder> {

    private Context context;
    private ArrayList<Annonce> mesannonce;

    public myannonce(Context context, ArrayList<Annonce> commentaires) {
        this.context = context;
        this.mesannonce = commentaires;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(context).inflate(R.layout.myannonce_layout, parent, false);
        myannonce.ViewHolder h = new myannonce.ViewHolder(vi);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Annonce annonce =mesannonce.get(position);
        holder.titreAnnonce.setText(annonce.getTitreAnnonce());
        //  holder.imageView.setImageBitmap(a.getImages());
        //Loading image from Glide library.
        Log.e("Url" ,annonce.getImages().get(0));
        Glide.with(context)
                .load(annonce.getImages().get(0))
                .centerCrop()
                .into(holder.imgAnnonce);
        holder.statu.setText(annonce.getStatu());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        holder.dateh.setText(str);
    }

    @Override
    public int getItemCount() {
        return mesannonce.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
 private ImageView imgAnnonce;
 private TextView titreAnnonce;
 private  TextView statu;
 private  TextView dateh;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnnonce=itemView.findViewById(R.id.img_article);
            titreAnnonce=itemView.findViewById(R.id.titte_annonce);
            statu=itemView.findViewById(R.id.statu);
            dateh=itemView.findViewById(R.id.datH);


        }
    }
}
