package com.example.collegebustracking.view;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.collegebustracking.R;

import com.example.collegebustracking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String userlocation="";
    String buslocation="";

    MarkerOptions origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        userlocation=savedInstanceState.getString("userlocation");
        buslocation=savedInstanceState.getString("buslocation");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final String[] userLatLongs=userlocation.split(",");
        final String[] busLatLongs=buslocation.split(",");

        Log.v("user Location",userlocation);
        Log.v("Bus Location",buslocation);

        // Add a marker in Sydney and move the camera
        LatLng user = new LatLng(Double.parseDouble(userLatLongs[0]), Double.parseDouble(userLatLongs[1]));
        LatLng bus = new LatLng(Double.parseDouble(busLatLongs[0]), Double.parseDouble(busLatLongs[1]));

        mMap.addMarker(new MarkerOptions().position(user).title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(bus).title("Bus Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(bus));
    }
}