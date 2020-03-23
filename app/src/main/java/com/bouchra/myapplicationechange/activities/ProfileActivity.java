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
    String mUID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//inti
        profile_img = findViewById(R.id.profilimG);
        name = findViewById(R.id.name);
        logOut = findViewById(R.id.logout);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();//preferenceUtils==user
        //hdo ta3 ysjl f firebase
        pId = firebaseAuth.getUid();

        //hdo ta3 ysjl f firebase
        myRef = FirebaseDatabase.getInstance().getReference("Membre").child(pId);

     //   name.setText(user.getDisplayName());
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
        //model.setImgMembre(photoUrl);
        myRef.setValue(model);
        //htalhna bch ykml ta3 li ywsl ll firebase
        String nom = name.getText().toString();


        logOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            updateUI();
             //preferenceUtils.Clear();
        });

//////////////////////////////////////////////update  token
       // updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    @Override
    protected void onResume() {
        check();
        super.onResume();
    }

    private void check() {
        // Check if user is signed in (non-null) and update UI accordingly.
//get current user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
      if (currentUser != null) {
          //  if (preferenceUtils != null) {
            // updateUI();
            //user is signed in saty here
          // mUID = currentUser.getUid();
            //save uid of currently signed in user in shared preferences
            /*SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID", mUID);
            editor.apply();*/
        } else {
// user not signed in , go to main activity
            updateUI();
        }

    }


    //logout
    private void updateUI() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }


    public void goToAcc(View view) {
        Intent intt = new Intent(ProfileActivity.this, debut.class);
        startActivity(intt);
    }

   /* public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token();
        mToken.setToken(token);
        ref.child(mUID).setValue(mToken);

    }*/
}

