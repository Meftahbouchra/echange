package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.RecycleViewArticleRetour;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ModifierAnnonce extends Fragment {
    private EditText nomAnnonce;
    private ImageView imgAnnonc;
    private EditText desciAnnonce;
    private EditText editText;
    private TextView enregister;
    private TextView textView;
    private Annonce annonce;
    private RecyclerView recyclerView;
    private RecycleViewArticleRetour postAdapter;
    ArrayList<String> posts = new ArrayList<>();


    public ModifierAnnonce() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modifier_annonce, container, false);
        nomAnnonce = view.findViewById(R.id.nom_annonce);
        imgAnnonc = view.findViewById(R.id.img_annonc);
        desciAnnonce = view.findViewById(R.id.desci_annonce);
        editText = view.findViewById(R.id.edittxt_article);
        textView = view.findViewById(R.id.ajout_article);
        enregister = view.findViewById(R.id.enregister);
        recyclerView = view.findViewById(R.id.rec_retour);
        textView.setOnClickListener(v -> {
            ajoutArticle();
        });

        //UNPACK OUR DATA FROM OUR BUNDLE
      /* Bundle b3 = getArguments();
        String name = b3.getString("name");
       String titre = this.getArguments().getString("nomannonce");*/

        annonce = (Annonce) getArguments().getSerializable("annonce");
        nomAnnonce.setText(annonce.getTitreAnnonce());
        desciAnnonce.setText(annonce.getDescriptionAnnonce());

        int j;
        for (j = 0; j < annonce.getArticleEnRetour().size(); j++) {
            String article = annonce.getArticleEnRetour().get(j);
            Log.e("annoce une is", article);
            posts.add(article);
            editText.setText("");
            postAdapter = new RecycleViewArticleRetour(getContext(), posts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(postAdapter);
        }

        Glide.with(this)
                .asBitmap()
                .load(annonce.getImages().get(0))
                .into(imgAnnonc);
        enregister.setOnClickListener(v -> {
            updateAnnonce();
        });
        return view;

    }

    private void updateAnnonce() {
        String titre_Annonce = nomAnnonce.getText().toString();
        String desc_Annonce = desciAnnonce.getText().toString();

        if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty()) {
            ////getting the specified artist reference
            DatabaseReference refannonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
            Annonce ann = new Annonce();
            //updating annonce
            ann.setDescriptionAnnonce(desc_Annonce);//c bn
            ann.setTitreAnnonce(titre_Annonce);// c bn
            ann.setDateAnnonce(annonce.getDateAnnonce());
            ann.setStatu(annonce.getStatu());
            ann.setUserId(annonce.getUserId());
            ann.setIdAnnonce(annonce.getIdAnnonce());
            ann.setImages(annonce.getImages());
            ann.setCommune(annonce.getCommune());////////methode static
            ann.setWilaya(annonce.getWilaya());////////
            ann.setArticleEnRetour(posts);
            refannonce.setValue(ann)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // getOffres(annonce.getIdAnnonce());
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce());
       /* PreferenceUtils preferenceUtils;
        preferenceUtils = new PreferenceUtils(getContext());*/
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot != null) {
                                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                String user = postSnapshot.child("idUser").getValue().toString();
                                                Log.e("userli dar offre ", user);
                                                Toast.makeText(getContext(), "jcp chkyn" + user, Toast.LENGTH_SHORT).show();
// send notification
                                            }

                                        } else Log.e("khataa", "khataa");


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Getting model failed, log a message
                                    }
                                });

                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void getOffres(String idAnnonce) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(idAnnonce);
       /* PreferenceUtils preferenceUtils;
        preferenceUtils = new PreferenceUtils(getContext());*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String user = postSnapshot.child("idUser").getValue().toString();
                    Log.e("userli dar offre ", user);
                    Toast.makeText(getContext(), "jcp chkyn" + user, Toast.LENGTH_SHORT).show();
// send notification
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });
    }

    /* private void senNotification(final String Idreceiver, final String nameSander, final String message) {
         DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
         Query query = allTokens.orderByKey().equalTo(userid);
         query.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     Token token = ds.getValue(Token.class);
                     Data data = new Data(preferenceUtils.getMember().getIdMembre(), name + " : " + message, "Nouveau message", userid, R.drawable.user); // logo of application

                     Sender sender = new Sender(data, token.getToken());
                     apiService.sendNotification(sender)
                             .enqueue(new Callback<Response>() {
                                 @Override
                                 public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                     Toast.makeText(MessageActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();

                                 }

                                 @Override
                                 public void onFailure(Call<Response> call, Throwable t) {

                                 }
                             });
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }*/
    public void ajoutArticle() {
        String x = editText.getText().toString();
        if (posts.size() < 10) {
            if (!x.isEmpty()) {
                posts.add(x);
                editText.setText("");
                postAdapter = new RecycleViewArticleRetour(getContext(), posts);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(postAdapter);


            } else {
                Toast.makeText(getContext(), "remplir le champs", Toast.LENGTH_SHORT).show();
            }


        } else {
            editText.setEnabled(false);
            editText.setText("");
            Toast.makeText(getContext(), "Dèsolè, il n'ya pas pour ajouter plus d'article", Toast.LENGTH_SHORT).show();
        }// vous avez atteint la limite des postes possible

    }

}





