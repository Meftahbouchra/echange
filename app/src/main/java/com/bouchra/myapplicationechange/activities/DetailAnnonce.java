package com.bouchra.myapplicationechange.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Posts;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAnnonce extends AppCompatActivity {
    private RelativeLayout relativeLayout;//////////////////////////////////////profil
    private TextView tite;
    private TextView desc;
    private Annonce annonce;
    private ImageView img;
    private TextView retour;
    private TextView time;
    private DatabaseReference ref;
    private TextView txtRetout;
    private TextView name_user;
    private CircleImageView imgUser;//img_user
    private Context cont;
    private PreferenceUtils preferenceUtils;
    private Membre membre;
    private Button offre;
    private Dialog MyDialog;
    private TextView sendMsg; //send_Msg
    private TextView shar_publication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_annonce);
        relativeLayout = findViewById(R.id.relative_profie);
        imgUser = findViewById(R.id.img_user);

        offre = findViewById(R.id.offre);
        offre.setOnClickListener(v -> {
            MyDialog();
        });
        // go to prifil
        relativeLayout.setOnClickListener(v -> {
          /*  startActivity(new Intent(DetailAnnonce.this, profilUser.class));
            finish();*/
            Intent profil = new Intent(DetailAnnonce.this, profilUser.class);
            profil.putExtra("user", annonce.getUserId());
            startActivity(profil);
        });

        initViews();

        txtRetout.setOnClickListener(v -> {
            finish();
        });
        //RECEIVE OUR DATA
        getIncomingIntent();
        img.setOnClickListener(v -> {
            //image clicable
            showImage();
            //Toast.makeText(getApplicationContext(), "image clicable", Toast.LENGTH_SHORT).show();

        });
        sendMsg = findViewById(R.id.send_Msg);
        sendMsg.setOnClickListener(v -> {
            Intent intent = new Intent(DetailAnnonce.this, MessageActivity.class);
            intent.putExtra("user", annonce.getUserId());
            startActivity(intent);

        });
        //share Post *FileProvider*
        shar_publication.setOnClickListener(v -> {
            String nom = tite.getText().toString().trim();
            String description = desc.getText().toString().trim();
            //get image from image View
            BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
            //convert image to bitmap
            Bitmap bitmap = bitmapDrawable.getBitmap();

            share(nom, description, bitmap);


        });

    }

    private void share(String nom, String description, Bitmap bitmap) {
        //concatenate title and desc to share
        String shareBody = nom + "\n" + description;
        //first we will save the image  in cache, get the saved image uri
        Uri uri = saveImgeToShare(bitmap);
        //share intent
        ////-------------send image
       /* Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"Partager avec "));*/


//////////////----------------------send text
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sendIntent.setType("text/*");
        startActivity(sendIntent);

        //startActivity(Intent.createChooser(sInent, "Partager avec "));
/*
 private void share(String nom, String description, Bitmap bitmap) {
        //concatenate title and desc to share
        String shareBody = nom + "\n" + description;
        //first we will save the image  in cache, get the saved image uri
        Uri uri = saveImgeToShare(bitmap);
        //share intent
        Intent sInent = new Intent(Intent.ACTION_SEND);//ACTION_SEND -  Cette intention démarrera l'activité d'envoi.
        sInent.putExtra(Intent.EXTRA_STREAM, uri);//Put extra mettra le flux supplémentaire avec le nom de chemin de l'image que nous partageons.
       // sInent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sInent.putExtra(Intent.EXTRA_SUBJECT, shareBody);

        sInent.setType("image/*");// s appele un  MIME;Nous devons définir le type de données d'envoi,
        sInent.setType("text/*");//Nous devons changer le type d'envoi car nous envoyons du texte.
        //sInent.setType("image/png");
        //  sInent.setType("txt/link");
        sInent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sInent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(sInent, "Partager avec "));
        //dans facebook, il ne peut partager l'image que parce que facebook ne permet pas de partager le texte via l'intention
    }
 */

    }

    private Uri saveImgeToShare(Bitmap bitmap) {
        File imageFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdir();//create if not exist
            File file = new File(imageFolder, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.bouchra.myapplicationechange.fileprovider", file);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return uri;
    }


    private void showImage() {

        View view = getLayoutInflater().inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);

        Glide.with(this)
                .asBitmap()
                .load(annonce.getImages().get(0))
                .into(imageView);


        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });

        //full screen
        MyDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        MyDialog.setContentView(view);
        MyDialog.show();// hadi nkd ndirha ghir f fct

    }

    // deb dialog offre
    private void MyDialog() {
        final Dialog MyDialog = new Dialog(DetailAnnonce.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.customdialog);

        Button ajout = MyDialog.findViewById(R.id.ajout);
        Button selec = MyDialog.findViewById(R.id.selec);
        TextView close = MyDialog.findViewById(R.id.close);

        ajout.setEnabled(true);
        selec.setEnabled(true);
        close.setEnabled(true);
        close.setOnClickListener(v -> {
            MyDialog.cancel();
        });
        ajout.setOnClickListener(v -> {
           // String anonceId = annonce.getIdAnnonce().toString();
            //   Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
            Intent ajou = new Intent(DetailAnnonce.this, AjoutOffre.class);
            //ajou.putExtra("anonceId", anonceId); //key* value
            ajou.putExtra("Annonce",annonce);
            startActivity(ajou);
            finish();


        });

      /*  ajout.setOnClickListener(v ->
                startActivity(new Intent(DetailAnnonce.this, AjoutOffre.class));
        finish();
        Toast.makeText(getApplicationContext(), " aller a nouv", Toast.LENGTH_LONG).show());*/
        selec.setOnClickListener(v -> {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction t = manager.beginTransaction();
            final Posts m4 = new Posts();
            Bundle b2 = new Bundle();
            b2.putString("edttext", annonce.getIdAnnonce());
            m4.setArguments(b2);
            t.add(R.id.fragmentposts, m4);
            t.commit();

             /*getSupportFragmentManager().beginTransaction().add(R.id.fragmentposts, new Posts(), "Posts").commit();

            Toast.makeText(getApplicationContext(), "selection bottom navigation", Toast.LENGTH_LONG).show();*/

        });

        MyDialog.show();
    }

///////////// fin de dalog offre

    public interface affichage {

        void getIncomingIntent();
        // void  setImage(String imageUrl, String imageName);

    }

    private void setImage(String imageUrl, String imageName) {


        tite.setText(imageName);


        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(img);
    }

    private void initViews() {

        txtRetout = findViewById(R.id.retour);
        time = findViewById(R.id.date_h);
        tite = findViewById(R.id.titte_annonce);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img_annonc);
        retour = findViewById(R.id.article_retour);
        name_user = findViewById(R.id.nom_user);
        imgUser = findViewById(R.id.img_user);
        shar_publication = findViewById(R.id.shar_publication);

    }

    public void getIncomingIntent() {
        //obtenir la référence de la base de données
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        Log.e("User is :", FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).toString());
        FirebaseDatabase.getInstance().getReference("Membre").child(annonce.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            name_user.setText(dataSnapshot.child("nomMembre").getValue().toString());
                            //imgUser.setImageURI(dataSnapshot.child("photoUser").);
                            String photouser = dataSnapshot.child("photoUser").getValue().toString();
                            Picasso.get().load(photouser).into(imgUser);

                           /* Glide.with(DetailAnnonce.this)
                                    .asBitmap()
                                    .load(photouser)
                                    .into(imgUser);*/
                            Log.e("User is :", dataSnapshot.getValue().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tite.setText(annonce.getTitreAnnonce());
        desc.setText(annonce.getDescriptionAnnonce());


        Date d = new Date(new Date().getTime() + 28800000);
      /*  String s=new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(d);
         String str = s.format(annonce.getDateAnnonce());*/
        //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // +heur
        // String str = simpleDateFormat.format(annonce.getDateAnnonce());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm "); // +heur
        String str = simpleDateFormat.format(annonce.getDateAnnonce());
        time.setText(str);
        setImage(annonce.getImages().get(0), annonce.getTitreAnnonce());
        for (int i = 0; i < annonce.getArticleEnRetour().size(); i++) {
            retour.setText(retour.getText() + "\n" + annonce.getArticleEnRetour().get(i));
        }

    }


}
