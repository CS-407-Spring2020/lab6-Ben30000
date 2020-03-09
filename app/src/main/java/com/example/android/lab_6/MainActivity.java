package com.example.android.lab_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    //Milestone 2
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12; // Could be any value
    private FusedLocationProviderClient mFusedLocationProviderClient; // Saved instance


    // Milestone 1
    // Sydney, Austrailia
    //private final LatLng mDestinationLatLng = new LatLng(-33.8523341,151.2106085);
    // Bascom Hall, Madison, WI
    private final LatLng mDestinationLatLng = new LatLng(43.0752778,-89.4041667);
    private final LatLng mDestinationLatLng2 = new LatLng(-33.8523341,151.2106085);
    private GoogleMap mMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            // code to display marker
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination"));
        });



        // Obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        displayMyLocation();

    }

    private void displayMyLocation() {

        // Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        // If permission not granted, ask for it
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // If permission granted, display marker at current location
        else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this,task -> {
                        Location mLastKnownLocation = task.getResult();
                            if (task.isSuccessful() && mLastKnownLocation != null){
                                // If successful draw polyline
                                mMap.addPolyline(new PolylineOptions().add(
                                        new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),
                                        mDestinationLatLng));

                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

                                mapFragment.getMapAsync(googleMap -> {
                                    mMap = googleMap;
                                    // code to display marker
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()))
                                            .title("Start"));
                                });


                            }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

}
