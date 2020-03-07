package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Posts extends Fragment {
    public Posts() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_posts, container, false);
        BottomNavigationView bottomNavigationView =view.findViewById(R.id.post_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(nouvList);


//par defaut

    getChildFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, new mesAnnonce()).commit();
     //  getFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, new mesAnnonce()).commit();
        return view;}
    private  BottomNavigationView.OnNavigationItemSelectedListener nouvList =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()){



                    case R.id.nav_offre :
                        selectedFragment = new mesOffres();
                        break;

                    case R.id.nav_annonce :
                        selectedFragment = new mesAnnonce();
                        break;


                }

              getChildFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment,selectedFragment).commit();
              //  getFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment,selectedFragment).commit();
                return  true;
            };


}
