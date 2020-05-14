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
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.notification.APIService;
import com.bouchra.myapplicationechange.notification.Client;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
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
import java.util.Date;


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
    private boolean notify = false;
    private APIService apiService;
    private DatabaseReference data;

    PreferenceUtils preferenceUtils;

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
        preferenceUtils = new PreferenceUtils(getContext());
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
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
                                if (!annonce.getStatu().equals("NEED_To_Be_CONFIRM") && !annonce.getStatu().equals("NEED_REVIEW") && !annonce.getStatu().equals("COMPLETED")) {
                                    getOffres(annonce);

                                }

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

    private void getOffres(Annonce annonce) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce());
       /* PreferenceUtils preferenceUtils;
        preferenceUtils = new PreferenceUtils(getContext());*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Offre offre = postSnapshot.getValue(Offre.class);

                    addNotification(annonce, offre);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });
    }

    // nrsl l mol l offre balo badal f annonce
    private void addNotification(Annonce annonce, Offre offre) {
        data = FirebaseDatabase.getInstance().getReference("Notification").child(offre.getIdUser());
        Notification notification = new Notification();
        notification.setIdsender(annonce.getUserId());
        notification.setIdreceiver(offre.getIdUser());
        notification.setDateNotification(new Date());
        notification.setContenuNotification("updateAnnonce");
        notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
        data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {


            }
        });


    }


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





