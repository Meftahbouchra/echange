package com.bouchra.myapplicationechange.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bouchra.myapplicationechange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class profilUser extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView t1, t2, t3, t4, nbr1, nbr2;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        t1 = findViewById(R.id.n1);
        t2 = findViewById(R.id.n2);
        t3 = findViewById(R.id.n3);
        t4 = findViewById(R.id.n4);
        circleImageView = findViewById(R.id.imgn);
        // pour le nbr d' annonce et offre
        nbr1 = findViewById(R.id.nbr1);
        nbr2 = findViewById(R.id.nbr2);
        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //checkc until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // get data li bin kwsyn homa ism li jy f fire base
                    String name = "" + ds.child("nom_Membre").getValue();
                    String email = "" + ds.child("email_Memebre").getValue();
                    String photo = "" + ds.child("img_Membre").getValue();
                    //  String addresse = ""+ds.child("adresse_Membre").getValue();
                    //  String phone = ""+ds.child("tel_Mrmbre").getValue();
                    // set data
                    t1.setText(name);
                    t2.setText(email);
                    // t3.setText(addresse);
                    //  t4.setText(phone);
                    try {
// if image is received then set
                        Picasso.get().load(photo).into(circleImageView);
                    } catch (Exception e) {
                        // if there is any exception whilegetting then set default
                        Picasso.get().load(R.drawable.user).into(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
