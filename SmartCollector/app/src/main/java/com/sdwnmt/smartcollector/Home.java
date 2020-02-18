package com.sdwnmt.smartcollector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sdwnmt.smartcollector.Modal.ACK.locACK;
import com.sdwnmt.smartcollector.Modal.PlotList;
import com.sdwnmt.smartcollector.Modal.Result;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    public static ViewPagerCustomDuration mPager ;
    List<PlotList> plotLists;
    private CardStackAdapter mAdapter ;
    private  UserSes userSes;
    private long backPressedTime;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView,notifyrecyclerView;
    private DataAdapter dataAdapter;

    //location views
    private String mLastUpdateTime;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 600000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userSes = new UserSes(Home.this);
        mPager =  findViewById(R.id.view_pager);

        Gson gson = new Gson();
        String json = userSes.getJson();
        Type type = new TypeToken<List<PlotList>>() {
        }.getType();
        plotLists = gson.fromJson(json, type);

        mAdapter = new CardStackAdapter(getSupportFragmentManager(),Home.this,plotLists);
        mPager.setPageTransformer(true, new CubeOutScalingTransformation());
        mPager.setAdapter(mAdapter);
        mPager.setScrollDurationFactor(5);
        mPager.setPagingEnabled(false);
        initRecycler();
        init();
        startLocationButtonClick();
        FileOutputStream fop = null;
        try {
            fop = this.openFileOutput("database.txt",MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecycler(){
        recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int id = R.anim.recycler;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(Home.this, id);
        recyclerView.setLayoutAnimation(animationController);
        dataAdapter = new DataAdapter(Home.this, plotLists);
        recyclerView.setAdapter(dataAdapter);
    }

    public class CubeOutScalingTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {

            if (position < -1){    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(-90 * Math.abs(position));

            }
            else if (position <= 1){    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(90 * Math.abs(position));
            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);
            }
            if (Math.abs(position) <= 0.5){
                page.setScaleY(Math.max(0.4f,1-Math.abs(position)));
            }
            else if (Math.abs(position) <= 1){
                page.setScaleY(Math.max(0.4f,Math.abs(position)));
            }
        }

    }
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Home.this);
        mSettingsClient = LocationServices.getSettingsClient(Home.this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            double Lat = mCurrentLocation.getLatitude();
            double Lng = mCurrentLocation.getLongitude();

            sendLocation(userSes.getWorkerid(), String.valueOf(Lat), String.valueOf(Lng), userSes.getToken());
        }
    }

    public void sendLocation(String id, String Lat, String Lng, String toke) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<locACK> call = apiInterface.sendLocation(id, Lat, Lng, toke);
        call.enqueue(new Callback<locACK>() {
            @Override
            public void onResponse(Call<locACK> call, Response<locACK> response) {
                Log.e("Location", "Success");
            }

            @Override
            public void onFailure(Call<locACK> call, Throwable t) {
//                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Tag", t.toString());
//                Toast.makeText(getContext(), "Please Check your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(Home.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


//                        Toast.makeText(getContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(Home.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
//                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
//                                Log.e(TAG, errorMessage);

                                Toast.makeText(Home.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    //@OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(Home.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(Home.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  Toast.makeText(getActivity(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                        //  toggleButtons();
                    }
                });
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Hit Back Again To Exit !", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Tag", "Fragment Resumed");
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(Home.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Tag", "Fragment paused");
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    @Override
    public void onStart() {
        Log.e("Tag", "Fragment started");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Tag", "Fragment stopped");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
//            dialog.dismiss();
        } catch (Exception e) {
        }
        Log.e("Tag", "Destroyed!!");
    }
    public  void warning(){
    }
}



