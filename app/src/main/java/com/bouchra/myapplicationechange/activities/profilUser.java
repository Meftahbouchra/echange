package com.bouchra.myapplicationechange.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BottomSheetPhoneNumber;
import com.bouchra.myapplicationechange.adapters.CommentaireAdapter;
import com.bouchra.myapplicationechange.models.Commentaire;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class profilUser extends AppCompatActivity {

    private CommentaireAdapter commentaireAdapter;
    private ArrayList<Commentaire> commentaires;

    private RecyclerView recyclerView;
    private Intent intent;
    private DatabaseReference reference;
    private CircleImageView imageUser;
    private TextView nomUser;
    private TextView mailUser;
    private TextView telUser;
    private TextView adressUser;
    private LinearLayout zoneEmail;
    private LinearLayout zonePhone;
    private static final int CALL_REQUEST_CODE = 100;
    private static final int SMS_REQUEST_CODE = 200;
    String[] callPermission;
    String[] smsPermission;
    Date date = new Date();
    private PreferenceUtils preferenceUtils;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        // get data
        intent = getIntent();
        String userid = intent.getStringExtra("user");
        imageUser = findViewById(R.id.image_user);
        nomUser = findViewById(R.id.nom_user);
        mailUser = findViewById(R.id.mail_user);
        telUser = findViewById(R.id.tel_user);
        adressUser = findViewById(R.id.adress_user);
        zoneEmail = findViewById(R.id.zone_email);
        zonePhone = findViewById(R.id.zone_phone);
        //init permission arrys
        callPermission = new String[]{Manifest.permission.CALL_PHONE};
        smsPermission = new String[]{Manifest.permission.SEND_SMS};
        preferenceUtils = new PreferenceUtils(this);
        // recycle view with out base da donne
        recyclerView = findViewById(R.id.recyle_commentaire);
        commentaires = new ArrayList<>();
        commentaireAdapter = new CommentaireAdapter(this, commentaires);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentaireAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Commentaire").child(userid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.e("data here", postSnapshot.getValue().toString());
                    String idUserSender = postSnapshot.child("idSender").toString();
                            commentaires.add(postSnapshot.getValue(Commentaire.class));
                            commentaireAdapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Membre").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Membre membre = dataSnapshot.getValue(Membre.class);
                Picasso.get().load(membre.getPhotoUser()).into(imageUser);
                nomUser.setText(membre.getNomMembre());
                mailUser.setText(membre.getEmail());


                try {
                    telUser.setText(membre.getNumTel());//jcp min njibh
                    adressUser.setText(membre.getAdresseMembre());///

                } catch (Exception e) {
                    telUser.setText("0123456789");
                    adressUser.setText("Es senia ,Oran,Algerie");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //call or send sms
        zonePhone.setOnClickListener(v -> {
            BottomSheetPhoneNumber bottomSheetPhoneNumber = new BottomSheetPhoneNumber();
            bottomSheetPhoneNumber.show(getSupportFragmentManager(), "bottomSheetPhoneNumber");

        });
        // send email
        zoneEmail.setOnClickListener(v -> {
            String email = mailUser.getText().toString();// email address here
            String subject = "";// subject here
            String body = ""; // body here
            String chooserTitle = "title";// chooser title here

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, chooserTitle));
        });
    }

    //URI: est une chaine de caracteres utilisee pour identifier une ressource
    //uri.parse: methode cree un nv object a partir dun format correct "String"->passee la chaine String
    public void sendSms() {
        //ACTION_VIEW :display thr data in the intent uRL
        if (checkSMSPermissions()) {
            String message = ""; // body here
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + telUser.getText().toString()));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        } else {
            requestSMSPermissions();
        }


    }

    public void Call() {
        //ACTION_DIAL :start a phonr dialer and use preset numbers in the data to dial

        if (checkCallPermissions()) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + telUser.getText().toString()));
            startActivity(intent);


        } else {
            requestCallPermissions();


        }

    }

    private boolean checkCallPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestCallPermissions() {
        ActivityCompat.requestPermissions(
                this, callPermission, CALL_REQUEST_CODE);
        Call();
    }

    private boolean checkSMSPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestSMSPermissions() {
        ActivityCompat.requestPermissions(
                this, smsPermission, SMS_REQUEST_CODE);
        sendSms();
    }


}