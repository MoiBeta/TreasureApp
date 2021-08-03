package com.example.mapsapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapsapp.databinding.ActivityMapsBinding;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    protected LocationManager locationManager;
    Location location;
    double latitude, longitude;
    private int treasuresNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mapsapp.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        treasuresNum = intent.getIntExtra("Treasures", 0);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            int MY_PERMISSION_REQUEST_LOCATION = 99;
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }

        locationManager
                .requestLocationUpdates(bestProvider, 0, 0, this);
        location = locationManager.getLastKnownLocation(bestProvider);

        try {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } catch (NullPointerException e) {
            Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currentLoc = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(currentLoc)
                .title("Marker in Sydney")
                .snippet("Right now we are not here")
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );
        for (int i = 0; i < treasuresNum; i++) {
            double randomLat = getRandomNumber(latitude);
            double radomLongt = getRandomNumber(longitude);
            LatLng treasure = new LatLng(randomLat, radomLongt);
            Marker markerTreasure = googleMap.addMarker(new MarkerOptions()
                    .position(treasure)
                    .title("Marker in Sydney")
                    .snippet("Right now we are not here")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasure))
            );

        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style));

            if (!success) {
                Log.e("tag", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("TAG", "Can't find style. Error: ", e);
        }
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
    }


    public double getRandomNumber(double coordinate) {
        Random random = new Random();
        double min = coordinate - 0.01;
        double max = coordinate + 0.01;
        double range = max - min;
        double scaled = random.nextDouble() * range;
        return scaled + min;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(this, "Location changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "Location enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Location disabled", Toast.LENGTH_SHORT).show();
    }
}