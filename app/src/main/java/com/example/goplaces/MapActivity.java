package com.example.goplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.goplaces.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivityLogs";
    public static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int FINE_PERMISSION_REQUEST_CODE = 1001;
    private static final float DEFAULT_ZOOM = 20f;

    private boolean mIsLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        PermissionUtils.requestPermission(this, FINE_LOCATION_PERMISSION,
                FINE_PERMISSION_REQUEST_CODE, true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "google maps is ready");
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    public void initMap() {
        Log.d(TAG, "initializing google maps");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting your current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getDeviceLocation: permissions are not granted");
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    Log.d(TAG, "onSuccess: successfully getting your location");
                    moveCamera(new LatLng(location.getLatitude(),location.getLongitude()),
                            DEFAULT_ZOOM);
                }else{
                    Log.d(TAG, "onSuccess: cannot getting location details");
                }
            }
        });
    }

    public void moveCamera(LatLng latLng,float zoom){
        Log.d(TAG, "moveCamera: moving camer to your current location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(PermissionUtils.isPermissionGranted(permissions,grantResults,FINE_LOCATION_PERMISSION)){
            Log.d(TAG,"location permission is granted");
            initMap();
        }else{
            Log.d(TAG,"location permission is denied");
            PermissionUtils.PermissionDialogDenied.newInstance(true)
                    .show(getSupportFragmentManager(),"denied_dialog");
        }
    }


}
