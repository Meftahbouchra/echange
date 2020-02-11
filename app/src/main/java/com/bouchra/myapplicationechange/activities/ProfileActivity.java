package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Membre;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView name;
    private CircleImageView profile_img;
    private Button logOut;
    private String pId;
    private DatabaseReference myRef;
    private String facebookUserTd = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_img = findViewById(R.id.profilimG);
        name = findViewById(R.id.name);
        logOut = findViewById(R.id.logout);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //hdo ta3 ysjl f firebase
        pId = firebaseAuth.getUid();
        //hdo ta3 ysjl f firebase
        myRef = FirebaseDatabase.getInstance().getReference("Membre").child(pId);
        name.setText(user.getDisplayName());


        for (UserInfo profile : user.getProviderData()) {

            if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserTd = profile.getUid();


            }

        }


        String photoUrl = "https://graph.facebook.com/" + facebookUserTd + "/picture?height=500";
        Picasso.get().load(photoUrl).into(profile_img);
        //hdo ta3 ysjl f firebase
        Membre model = new Membre();
        model.setIdMembre(pId);
        model.setNomMembre(user.getDisplayName());
        //***************************************************************************
        //model.setImgMembre(photoUrl);
        myRef.setValue(model);
        //htalhna bch ykml ta3 li ywsl ll firebase


        logOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            updateUI();

        });
        //hna bansayi ndir ta3 dialog


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            updateUI();
        }
       /* else {
            startActivity(new Intent(ProfileActivity.this,Acceuil.class));
            finish();

        }
*/

    }

    private void updateUI() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }


    public void goToAcc(View view) {
        Intent intt = new Intent(ProfileActivity.this, Acceuil.class);
        startActivity(intt);
    }
}

