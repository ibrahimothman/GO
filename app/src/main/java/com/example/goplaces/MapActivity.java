package com.example.goplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.security.Permission;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "MapActivityLogs";
    public static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOACTION_REQUEST_CODE = 1001;

    private boolean mIsLocationPermissionsGranted = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        checkLocationPermissions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,"google maps is ready");
        mMap = googleMap;
    }


    public void initMap(){
        Log.d(TAG,"initializing google maps");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void checkLocationPermissions(){
        Log.d(TAG,"check location permissions");
        if(ContextCompat.checkSelfPermission(this,
                FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,
                    COARSE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED ){
                mIsLocationPermissionsGranted = true;
            }else{

                ActivityCompat.requestPermissions(this,
                        new String[]{COARSE_LOCATION_PERMISSION},LOACTION_REQUEST_CODE);
            }
        }else{

            ActivityCompat.requestPermissions(this,
                    new String[]{FINE_LOCATION_PERMISSION},LOACTION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mIsLocationPermissionsGranted = false;
        switch (requestCode){
            case LOACTION_REQUEST_CODE :
                if(grantResults.length > 0){
                    for (int i = 0 ; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG,"location permission is failed");
                            mIsLocationPermissionsGranted = false;
                            return;
                        }
                    }

                    Log.d(TAG,"location permissions is granted");
                    mIsLocationPermissionsGranted = true;
                    // initialize google map
                    initMap();
                }
        }
    }


}
