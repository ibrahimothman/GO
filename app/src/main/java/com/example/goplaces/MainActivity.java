package com.example.goplaces;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 1000;

    @BindView(R.id.init_map_btn)
    Button mInitMapBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(isServiceOk()){
            init();
        }


    }

    private void init() {
        Intent mapIntent = new Intent(this,MapActivity.class);
        startActivity(mapIntent);
    }

    public boolean isServiceOk(){
        Log.d(TAG,"check if google service is ok or not");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(available == ConnectionResult.SUCCESS){
            // every thing is fine and user can make map requests
            Log.d(TAG,"every thing is fine and you can make map requests");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occurred but we can fix it
            Log.d(TAG,"an error occurred but you still can make map requests");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
