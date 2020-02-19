package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.RecycleViewAdapter;
import com.bouchra.myapplicationechange.fragments.PosteModelAnnonce;

import java.util.ArrayList;

public class essai extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter postAdapter;
    private ArrayList<PosteModelAnnonce> posts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essai);
        // reyclecie wta3 l xml ta3 activity
        recyclerView = findViewById(R.id.recyclev);
        posts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posts.add(new PosteModelAnnonce("title" + i, "descriuption" + i, " " ));
        }
        // ta3rif l adapter , yadi l contyext = essai activity w l aarykyst li khasa bl post
        postAdapter = new RecycleViewAdapter(this, posts);

        // tahdid lyout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //rabt recyclerView m3a l adapter

        recyclerView.setAdapter(postAdapter);

    }
}
