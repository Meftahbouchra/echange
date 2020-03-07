package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.myannonce;
import com.bouchra.myapplicationechange.models.Annonce;

import java.util.ArrayList;
import java.util.Date;

public class mesAnnonce extends Fragment {



    private myannonce myannonce;
    private ArrayList<Annonce> annonces;
    private RecyclerView recyclerView ;


    Date date=new Date();
    public mesAnnonce() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_mesannonces, container, false);
        recyclerView = view.findViewById(R.id.recyle_mesannonces);

        annonces = new ArrayList<>();

        myannonce = new myannonce(getContext(), annonces);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.setAdapter(myannonce);

        return  view;
    }
}
