package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.fragments.Acceuil;
import com.bouchra.myapplicationechange.fragments.Posts;
import com.bouchra.myapplicationechange.fragments.plus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class debut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut);

        BottomNavigationView botoomNav = findViewById(R.id.bottom_navigation);
        botoomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container, new Acceuil()).commit();
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){



                   case R.id.nav_home :
                        selectedFragment = new Acceuil();
                        break;
                    case R.id.nav_posts :
                       selectedFragment = new Posts();
                        break;
                   case R.id.nav_plus :
                        selectedFragment = new plus();
                        break;


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,selectedFragment).commit();
                return  true;
            };
}
