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
import com.bouchra.myapplicationechange.adapters.CommentaireAdapter;
import com.bouchra.myapplicationechange.models.Commentaire;

import java.util.ArrayList;
import java.util.Date;

public class profilUser extends Fragment {
    private RecyclerView recyclerView;
    private CommentaireAdapter commentaireAdapter;
    private ArrayList<Commentaire> commentaires;


    Date date=new Date();
    public  profilUser(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_profil_user, container, false);
        recyclerView = view.findViewById(R.id.recyle_commentaire);

        commentaires = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            commentaires.add(new Commentaire("nomUser" + i, date , " etoiles"+i ,"date"+i,3));
        }
        commentaireAdapter = new CommentaireAdapter(getContext(), commentaires);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.setAdapter(commentaireAdapter);
        return view;
    }
}