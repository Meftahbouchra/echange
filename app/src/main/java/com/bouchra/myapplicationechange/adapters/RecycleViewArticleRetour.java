package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.annonce.Article_en_retour;

import java.util.ArrayList;

public class RecycleViewArticleRetour extends RecyclerView.Adapter<RecycleViewArticleRetour.ViewHolder> {


    public RecycleViewArticleRetour(Article_en_retour article_en_retour) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView article;
        private Button remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            article = itemView.findViewById(R.id.text_retour);
            remove = itemView.findViewById(R.id.btun_remove);
        }
    }

    private Context context;
    private ArrayList<String> list;

    public RecycleViewArticleRetour(Context c, ArrayList<String> articleRetour) {
        this.context = c;
        list = articleRetour;
    }

    @NonNull
    @Override
    public RecycleViewArticleRetour.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.liste_article_enretour, parent, false);
        RecycleViewArticleRetour.ViewHolder h = new RecycleViewArticleRetour.ViewHolder(view);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        h.article.setText(list.get(position));

        h.remove.setText("-");
        h.remove.setOnClickListener(v -> {

            list.remove(position);
            // adapter hna khas ndirolah refresh
            // ins pour refresh vta3 view
            this.notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



