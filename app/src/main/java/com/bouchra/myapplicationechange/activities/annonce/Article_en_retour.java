package com.bouchra.myapplicationechange.activities.annonce;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.RecycleViewArticleRetour;
import com.bouchra.myapplicationechange.models.Annonce;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Article_en_retour extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecycleViewArticleRetour postAdapter;


    private EditText editText;
    private TextView textView;
    private Annonce annonce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_en_retour);
        editText = findViewById(R.id.edittxt_article);
        textView = findViewById(R.id.ajout_article);
        recyclerView = findViewById(R.id.rec_retour);
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        ArrayList<String> posts = new ArrayList<>();
        //isEmpty ewt vide    isEmpty()

        textView.setOnClickListener(v -> {
            String x = editText.getText().toString();
            if( !x.isEmpty()){
                if (posts.size() < 10) {
                    posts.add(x);
                    editText.setText("");
                    postAdapter = new RecycleViewArticleRetour(this, posts);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(postAdapter);

                } else {
                    editText.setEnabled(false);
                    editText.setText("");
                    Toast.makeText(this, "Dèsolè, il n'ya pas pour ajouter plus d'article", Toast.LENGTH_SHORT).show();


                }// vous avez atteint la limite des postes possible


            }else{
                Toast.makeText(this, "remplir le champs", Toast.LENGTH_SHORT).show();
            }

        });


        findViewById(R.id.ok).setOnClickListener(v -> {
            annonce.getArticleEnRetour().addAll(posts);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Annonce");
            databaseReference.setValue(annonce).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                }
            });

        });


    }
}
