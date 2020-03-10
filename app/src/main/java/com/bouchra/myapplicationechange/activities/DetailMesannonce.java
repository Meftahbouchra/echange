package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;

public class DetailMesannonce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debut);

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater menu
        inflater.inflate(R.menu.setting_annonce,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handel menu item clicks
        int id= item.getItemId();
        if(id == R.id.modifier){
            Toast.makeText(getActivity(), "modifier", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.Supprimer){
            Toast.makeText(getActivity(), "Supprimer", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }*/
    }
}