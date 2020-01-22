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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.sdwnmt.smartcollector.Modal.ACK.locACK;
import com.sdwnmt.smartcollector.Modal.SendPlayerId;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements OSSubscriptionObserver {

    private TextView Wname,zone,route,vehicle,email;
    private Button btn1,btn2;
    public UserSes userSes;
    private String player_id,title,body;
    private FloatingActionButton notify;

    //Location Related Vars
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
        setContentView(R.layout.activity_profile);

        notify = findViewById(R.id.floatingActionButton);
        btn1 = findViewById(R.id.start_trip);
        btn2 = findViewById(R.id.logout);
        Wname = findViewById(R.id.Wname);
        zone = findViewById(R.id.zone);
        route = findViewById(R.id.route);
        vehicle = findViewById(R.id.vehicle);
        email = findViewById(R.id.email);

        OneSignal.addSubscriptionObserver(Profile.this);
        OneSignal.startInit(Profile.this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new Profile.ExampleNotificationOpenedHandler())
                .init();
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        status.getSubscriptionStatus().getSubscribed();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        player_id = status.getSubscriptionStatus().getUserId();
        status.getSubscriptionStatus().getPushToken();
        OneSignal.sendTag("user_id", "1111");
//        Log.e("Playerid",player_id);
//        Log.e("abc", "onCreate");
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Notification pop", Toast.LENGTH_SHORT).show();
            }
        });
        try{
            userSes = new UserSes(Profile.this);
        }catch (NullPointerException npe){
            Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        makeCall();
        
        //Location start
        init();
        startLocationButtonClick();

        setViewData();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Home.class);
                startActivity(i);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSes.removeUser();
                Intent i = new Intent(Profile.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            new AlertDialog.Builder(Profile.this)

                    .setMessage("You've successfully subscribed to push notifications!")
                    .show();
            // get player ID
            stateChanges.getTo().getUserId();
            Log.e("Playerid",player_id);

        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    private void makeCall() {
//        Toast.makeText(getContext(), player_id, Toast.LENGTH_SHORT).show();
//        Log.e("makeCall",player_id);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SendPlayerId> call = apiInterface.sendPlayerID(player_id, userSes.getWorkerid());
        call.enqueue(new Callback<SendPlayerId>() {
            @Override
            public void onResponse(Call<SendPlayerId> call, Response<SendPlayerId> response) {
                Toast.makeText(Profile.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendPlayerId> call, Throwable t) {
//                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            title = result.notification.payload.title;
            body = result.notification.payload.body;

            String customKey;
//            writeToFile(body);
            Log.e("abc", "notificationOpened");

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
                Log.e("notificationOpened",data.toString());
//                Toast.makeText(getContext(), body, Toast.LENGTH_SHORT).show();
//                t1.setText(title);
//                t2.setText(body);
            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        }
    }

//    public void showPop() {
//        AlertDialog.Builder malert = new AlertDialog.Builder(Profile.this);
//        final View v1 = getLayoutInflater().inflate(R.layout.notifymodal, null);
//        t1 = v1.findViewById(R.id.title);
//        t2 = v1.findViewById(R.id.button);
////        clear = v1.findViewById(R.id.clear);
//
//
//
//        notifyrecyclerView = v1.findViewById(R.id.notify);
//
//        notifyrecyclerView.setHasFixedSize(true);
//        notifyrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        Collections.reverse(notifyList);
//        notificationAdapter = new NotificationAdapter(getContext(),notifyList);
//
//        notifyrecyclerView.setAdapter(notificationAdapter);
//
//        Log.e("abc", "pop");
//        malert.setView(v1);
//
//
//        final AlertDialog dialog = malert.create();
//        dialog.setCanceledOnTouchOutside(false);
//
//        dialog.show();
//        t2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//    }

//    private void readFromFile(){
//        try {
////            new FileOutputStream("Notification.txt").close();
//            FileInputStream fileInputStream = this.openFileInput("Notification.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String NewNote;
//
//            while ((NewNote = bufferedReader.readLine()) != null){
//                if(!NewNote.equals(""))
//                    notifyList.add(NewNote);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void writeToFile(String body){
//        FileOutputStream fileoutputStream = null;
//        try {
//            fileoutputStream = this.openFileOutput("Notification.txt", Context.MODE_APPEND);
//            fileoutputStream.write(("\n"+body).getBytes());
//            fileoutputStream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void setViewData(){
        try {
            Wname.setText(userSes.getFullname());
            zone.setText(userSes.getZoneid());
            route.setText(userSes.getRoute());
            email.setText(userSes.getEmail());
            vehicle.setText(userSes.getVehicle());
        }catch (Exception e){}
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Profile.this);
        mSettingsClient = LocationServices.getSettingsClient(Profile.this);

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

            // location last updated time
            // Toast.makeText(getContext(), "Hello"+String.valueOf(Lat)+" "+String.valueOf(Lng), Toast.LENGTH_SHORT).show();

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
                .addOnSuccessListener(Profile.this, new OnSuccessListener<LocationSettingsResponse>() {
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
                .addOnFailureListener(Profile.this, new OnFailureListener() {
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
                                    rae.startResolutionForResult(Profile.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
//                                Log.e(TAG, errorMessage);

                                Toast.makeText(Profile.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    //@OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(Profile.this)
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
                .addOnCompleteListener(Profile.this, new OnCompleteListener<Void>() {
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
        int permissionState = ActivityCompat.checkSelfPermission(Profile.this,
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

}
