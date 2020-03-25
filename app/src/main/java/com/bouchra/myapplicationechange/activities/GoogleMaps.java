package com.bouchra.myapplicationechange.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.bouchra.myapplicationechange.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
// afficher vos cartes en mode StreetView (vue panoramique à 360°) : implémenter l’interface OnStreetViewPanoramaReadyCallback
public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mapApi;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapApi);
        mapFragment.getMapAsync(this);

        //getSupportFragmentManager().beginTransaction().add(R.id.fragment,new TestFragment(),"test").commit();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapApi=googleMap;
        //position : LatLng
        LatLng oran = new LatLng(35.6361224, -0.618167);
        //afficher un marqueur et titre: le text de lz fenetre affiche quand on clique dessus
        mapApi.addMarker(new MarkerOptions().position(oran).title("oran"));
       // centrer la caméra de la carte sur la position
        //passe en paramètres les coordonnées géographiques du  position
        mapApi.moveCamera(CameraUpdateFactory.newLatLng(oran));


    }
}
