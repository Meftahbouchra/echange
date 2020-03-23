package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Acceuil;
import com.bouchra.myapplicationechange.fragments.Posts;
import com.bouchra.myapplicationechange.fragments.plus;
import com.bouchra.myapplicationechange.notification.Token;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class debut extends AppCompatActivity {

    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut);
        preferenceUtils = new PreferenceUtils(this);
        Log.e("Here Error", "Error");
        Log.e("Here pref ", preferenceUtils.getMember().getIdMembre());
        BottomNavigationView botoomNav = findViewById(R.id.bottom_navigation);
        botoomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container, new Acceuil()).commit();
        //update toke
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_search, menu);
        return true;
    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token();
        mToken.setToken(token);
        ref.child(preferenceUtils.getMember().getIdMembre()).setValue(mToken);

    }
}
