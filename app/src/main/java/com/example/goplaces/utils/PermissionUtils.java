package com.example.goplaces.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.goplaces.R;

public class PermissionUtils {

   private static boolean mLocationpermissionGranetd = false;

    /**
     * Requests the fine location permission. If a rationale with an additional explanation should
     * be shown to the user, displays a dialog that triggers the request.
     */

    public static void requestPermission(AppCompatActivity activity, String permission,
                                         int requestCode, boolean finishActivity){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
            PermissionUtils.RationaleDialog.newInstance(requestCode,finishActivity)
                    .show(activity.getSupportFragmentManager(),"permission_dialog");
        }else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
        }
    }


    // check if the permission is granted or not
    public static boolean isPermissionGranted(String[] permissions, int[] PermissionResults,
                String permission){

        for(int i = 0; i < permissions.length; i++){
            if(permission.equals(permissions[i])){
                mLocationpermissionGranetd = true;
                return PermissionResults[i]  == PackageManager.PERMISSION_GRANTED;
            }
        }

        return false;
    }

    public static boolean isLocationpermissionGranetd(){
        return mLocationpermissionGranetd;
    }



    public static class PermissionDialogDenied extends DialogFragment{


        private static final String ARGUMENT_FINISH_ACTIVITY = "finsihActivity";
        private boolean mFinishActivity = false;

        public static PermissionDialogDenied newInstance(boolean finishActivity){
            Bundle bundle = new Bundle();
            bundle.putBoolean(ARGUMENT_FINISH_ACTIVITY,finishActivity);
            PermissionDialogDenied dialog = new PermissionDialogDenied();
            dialog.setArguments(bundle);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            mFinishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getContext())
                    .setMessage("This app requires location permission to enable the \'my location\' layer. Please try again and grant access to use the location")
                    .setPositiveButton("OK",null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if(mFinishActivity){
                Toast.makeText(getActivity(),
                        R.string.permission_required_toast,
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    public static class RationaleDialog extends DialogFragment{

        private static final String ARGUMENT_REQUEST_CODE = "requestCode";
        private static final String ARGUMENT_FINISH_ACTIVITY = "finishActivity";

        boolean mFinishActivity = false;

        public static RationaleDialog newInstance(int requestCode, boolean finishActivity){
            Bundle bundle = new Bundle();
            bundle.putInt(ARGUMENT_REQUEST_CODE,requestCode);
            bundle.putBoolean(ARGUMENT_FINISH_ACTIVITY,finishActivity);
            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            final int mRequestCode = bundle.getInt(ARGUMENT_REQUEST_CODE);
            mFinishActivity = bundle.getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.permission_rotionale_location)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // after clicking OK, request the permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                    ,mRequestCode);

                            // don't finish activity while requesting the permission
                            mFinishActivity = false;
                        }
                    })
                    .setNegativeButton("CANCEL",null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if(mFinishActivity){
                Toast.makeText(getActivity(),
                        R.string.permission_required_toast,
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
