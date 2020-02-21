package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.CommentaireAdapter;
import com.bouchra.myapplicationechange.models.Commentaire;

import java.util.ArrayList;
import java.util.Date;

public class profilUser extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentaireAdapter commentaireAdapter;
    private ArrayList<Commentaire> commentaires;


  /*  DateFormat df= new SimpleDateFormat("EEE,d MMM yyyy, HH:mm");
    String date = df.format(Calendar.getInstance().getTime());*/
    Date date=new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        recyclerView = findViewById(R.id.recyle_commentaire);
        commentaires = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            commentaires.add(new Commentaire("nomUser" + i, date , " etoiles"+i ,"date"+i,3));
        }
        commentaireAdapter = new CommentaireAdapter(this, commentaires);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(commentaireAdapter);
    }
}