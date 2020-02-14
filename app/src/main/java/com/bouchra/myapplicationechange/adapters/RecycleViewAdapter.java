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

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

   /* private  String [] mImagesTitle = new String[]{
            "sac","user"
    };*/



    //view holder class
    // hadi li dakhal hadi mla claas  hiya li masoola 3la les compos ta3 laliste
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title , desc;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           title = itemView.findViewById(R.id.nom_annonce);
            desc = itemView.findViewById(R.id.desc_annonce);
            //carouselView = itemView.findViewById(R.id.image_annonce);
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



    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


}
