package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name;
    private CircleImageView profile_img;
    private Button logOut;
    private FirebaseAuth firebaseAuth;
    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//inti
        profile_img = findViewById(R.id.profilimG);
        name = findViewById(R.id.name);
        logOut = findViewById(R.id.logout);
        preferenceUtils = new PreferenceUtils(this);


        logOut.setOnClickListener(v -> {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            updateUI();
            preferenceUtils.Clear();
        });
        name.setText(preferenceUtils.getMember().getNomMembre());
        Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(profile_img);
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

