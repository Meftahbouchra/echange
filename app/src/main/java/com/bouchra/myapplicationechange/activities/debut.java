package com.bouchra.myapplicationechange.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Acceuil;
import com.bouchra.myapplicationechange.fragments.Posts;
import com.bouchra.myapplicationechange.fragments.plus;
import com.bouchra.myapplicationechange.notification.Token;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class debut extends AppCompatActivity {

    private PreferenceUtils preferenceUtils;
    private BottomNavigationView botoomNav;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut);

        preferenceUtils = new PreferenceUtils(this);
        botoomNav = findViewById(R.id.bottom_navigation);
        botoomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container, new Acceuil()).commit();
        //update toke for notification
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {


                    case R.id.nav_home:
                        selectedFragment = new Acceuil();
                        break;
                    case R.id.nav_posts:
                        selectedFragment = new Posts();
                        break;
                    case R.id.nav_plus:
                        selectedFragment = new plus();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container, selectedFragment).commit();
                return true;
            };


    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token();
        mToken.setToken(token);
        ref.child(preferenceUtils.getMember().getIdMembre()).setValue(mToken);

    }

    private void updateUI() {
        startActivity(new Intent(debut.this, MainActivity.class));
        finish();
    }

    private void checkUserStatus() {
        // Check if user is signed in (non-null) and update UI accordingly.
        // get current user
        FirebaseUser currentUser = firebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            //user is signed in saty here

        } else {
            // user not signed in , go to main activity
            updateUI();
        }

    }


}
