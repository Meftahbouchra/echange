package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.PosteModelAnnonce;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

   /* private  String [] mImagesTitle = new String[]{
            "sac","user"
    };*/



    //view holder class
    // hadi li dakhal hadi mla claas  hiya li masoola 3la les compos ta3 laliste
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title , desc;
        CarouselView carouselView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           title = itemView.findViewById(R.id.nom_annonce);
            desc = itemView.findViewById(R.id.desc_annonce);
            carouselView = itemView.findViewById(R.id.image_annonce);
        }
    }

private  Context context;
    private List<PosteModelAnnonce> posts;

    public RecycleViewAdapter(Context c, List<PosteModelAnnonce> posteList) {
      this.context=c;
      posts=posteList;
    }

    @NonNull
    @Override
    // n3arfo  layout_listannonce
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_listannonce, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    // nraslo les donnes bach n3ardoha
    // hadi hiya li recycle view t3ytlha ki l user ytala3 wla yhawad
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // had l holder rah fih  image nomAnnoce w kolch li f  ViewHolder=holder
        PosteModelAnnonce p=posts.get(position); // tjibah f had l pisition
        holder.title.setText(p.getTitle());
        holder.desc.setText(p.getDesciption());
        String [] mImages = new String[]{
                "https://cdn.futura-sciences.com/buildsv6/images/mediumoriginal/6/5/2/652a7adb1b_98148_01-intro-773.jpg"
        };
        holder.carouselView.setPageCount(mImages.length);
        holder.carouselView.setImageListener((pos, imageView) -> Picasso.get().load(mImages[pos]).placeholder(R.color.black).into(imageView));
    }
//la taille de notre liste
    @Override
    public int getItemCount() {
        return posts.size();
    }


}
