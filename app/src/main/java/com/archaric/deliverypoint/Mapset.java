package com.archaric.deliverypoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.archaric.deliverypoint.Fragments.LocationPicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Mapset extends AppCompatActivity implements  com.google.android.gms.location.LocationListener  {

    public static final String ZONE_ID = "zoneId" ;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    private static final String Fine_Location = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String Coarse_Location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int Location_Request_Code = 1234;
    private static final int REQUEST_CHECK_CODE = 8989;

    boolean mLocationPermissionGranted = false;

    LocationSettingsRequest.Builder builder;
    String zoneId;
    Button  btnPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapset);

       FrameLayout mapSet = findViewById(R.id.mapSet);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationManager manager;
            manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestLocationPermission();

                }else {
                    getLocationPermission();
                }
            }
        }




    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, this application want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                       gps();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void gps(){

            LocationRequest locationRequest = new LocationRequest()
                    .setInterval(3000)
                    .setFastestInterval(1500)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            Task<LocationSettingsResponse> responseTask =
                    LocationServices.getSettingsClient(Mapset.this).checkLocationSettings(builder.build());

            responseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                @Override
                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                    try {

                        task.getResult(ApiException.class);
                    } catch (ApiException e) {
                        e.printStackTrace();
                        switch (e.getStatusCode()){
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {

                                    ResolvableApiException resolvableApiException
                                            = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(Mapset.this, REQUEST_CHECK_CODE);

                                } catch (IntentSender.SendIntentException sendIntentException) {
                                    sendIntentException.printStackTrace();
                                }catch (ClassCastException exception){

                                }
                                break;
                            case  LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            {}
                            break;
                        }
                    }

                }
            });

        }


    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(Mapset.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
           handleLocationUpdates();
        }

    }


    private void showSnackBar(){
        RelativeLayout constraintLayout = findViewById(R.id.parentRelative);
        Snackbar.make(constraintLayout,"This Application needs Location Permission",Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent,101);
                    }
                }).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        int permissionLocation = ContextCompat.checkSelfPermission(Mapset.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            handleLocationUpdates();
        }else {
            showSnackBar();
        }

    }

    private void handleForegroundLocationUpdates() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mapSet,new LocationPicker("GotoMain"));
            transaction.commit();
        }


    public static final int REQUEST_CODE_PERMISSIONS = 101;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (background){
                handleLocationUpdates();
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void handleLocationUpdates() {

//        if (checkLocationPermission()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mapSet,new LocationPicker("GotoMain"));
            transaction.commit();
//        }
        //foreground and background
       // Toast.makeText(getApplicationContext(),"Start Foreground and Background Location Updates",Toast.LENGTH_SHORT).show();
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Fine_Location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Coarse_Location) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(this, permission, Location_Request_Code);
            }

        } else {
            ActivityCompat.requestPermissions(this, permission, Location_Request_Code);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        handleLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}