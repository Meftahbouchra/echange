package com.bouchra.myapplicationechange.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bouchra.myapplicationechange.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Posts extends Fragment {
    private String offreSelected;

    public Posts() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            // get arguments
            offreSelected = getArguments().getString("edttext");
            Log.e("Text is :", offreSelected);
        } catch (Exception e) {
            offreSelected = null;
        }

        View view = inflater.inflate(R.layout.activity_posts, container, false);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.post_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(nouvList);
        //offreSelected = getArguments().getString("offre");


//par defaut
        if (offreSelected == null) {
            getChildFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, new mesAnnonce()).commit();
        } else {
            getChildFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, new mesAnnonce(offreSelected)).commit();
        }


        //  getFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, new mesAnnonce()).commit();
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nouvList =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {


                    case R.id.nav_offre:
                        if (offreSelected == null) {
                            selectedFragment = new mesOffres();
                        } else {
                            selectedFragment = new mesOffres(offreSelected);
                        }

                        break;

                    case R.id.nav_annonce:
                        if (offreSelected == null) {
                            selectedFragment = new mesAnnonce();
                        } else {
                            selectedFragment = new mesAnnonce(offreSelected);
                        }

                        break;


                }

                getChildFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment, selectedFragment).commit();
                //  getFragmentManager().beginTransaction().replace(R.id.post_navigation_fragment,selectedFragment).commit();
                return true;
            };


}
