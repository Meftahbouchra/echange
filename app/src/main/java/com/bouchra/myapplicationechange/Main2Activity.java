package com.bouchra.myapplicationechange;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        BottomNavigationView botoomNav = findViewById(R.id.bottom);
        botoomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container, new userFragment()).commit();
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){



                    case R.id.nav_offre :
                        selectedFragment = new chatFragment();
                        break;

                    case R.id.nav_annonce :
                        selectedFragment = new userFragment();
                        break;



                }
                getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,selectedFragment).commit();
                return  true;
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_search,menu);
        return true;
    }
}
