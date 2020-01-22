package com.sdwnmt.smartcollector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.ACK.endTripAck;
import com.sdwnmt.smartcollector.Modal.ACK.locACK;
import com.sdwnmt.smartcollector.Modal.PlotList;
import com.sdwnmt.smartcollector.Modal.Result;
import com.sdwnmt.smartcollector.Modal.SendPlayerId;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    
    private AlertDialog dialog;
    private Button b1,b2,clear;
    int count;
    public FrameLayout notification;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView,notifyrecyclerView;
    private HashSet<String> pids;
    private HashSet<String> uncollected;
    private List<PlotList> plotLists;

    private DataAdapter dataAdapter;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        btn = view.findViewById(R.id.btn1);
//        RL1 = view.findViewById(R.id.hidescan);
//        b1 = view.findViewById(R.id.pop);
//        tv1 = view.findViewById(R.id.textView4);
//        tv2 = view.findViewById(R.id.cart_badge);
//        notification = view.findViewById(R.id.houseCount);
//        userNotify = new UserNotify(getContext());


//        notifyList = new ArrayList<>();
//
////        readFromFile();
//
//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                showPop();
//            }
//        });
//
////        userSes = new UserSes(getContext());
//        //setupBadg
//        pop();
////        init();
//        //toggleScreen();
//        initViews(view);
//        Log.e("OnCreate",title);
//        Log.e("OnCreate",body);
//        Log.e("Tag", " Oncreate view called");
        return view;
    }
    
//    private void pop() {
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Button b1;
//                final TextView dry, wet;
////                ModalPop modalPop = new ModalPop();
////                modalPop.show(getFragmentManager(),"ModalPop
//                AlertDialog.Builder malert = new AlertDialog.Builder(getContext());
//                final View v1 = getLayoutInflater().inflate(R.layout.modal, null);
//                b1 = v1.findViewById(R.id.clear);
//                b2 = v1.findViewById(R.id.cancel);
//                dry = v1.findViewById(R.id.DryG);
//                wet = v1.findViewById(R.id.wetG);
//                malert.setView(v1);
//                dialog = malert.create();
//                dialog.setCanceledOnTouchOutside(false);
////                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.show();
//                b2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                b1.setOnClickListener(new View.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("pop", String.valueOf(pids.size()));
//                        for (int i = 0; i < plotLists.size(); i++) {
//                            if (!pids.contains(plotLists.get(i).getId())) {
//                                uncollected.add(plotLists.get(i).getId());
//                            }
//                        }
//                        Log.e("pop", uncollected.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
//                        final String NotCollected = uncollected.toString().replaceAll("\\[", "").replaceAll("\\]", "");
//                        Date date = new Date();
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
//                        final String d = simpleDateFormat.format(date);
//                        Log.e("pop", d);
//                        //Toast.makeText(getContext(), dry.getText().toString(), Toast.LENGTH_SHORT).show();
//                        if ((dry.getText().toString().equals("")) || (wet.getText().toString().equals(""))) {
//                            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//                        } else {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//                            alertDialog.setTitle("Confirmation");
//                            alertDialog.setMessage("Are you sure you want to Submit? \n You will be logged out after submitting.");
//                            alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    endTrip(dry.getText().toString(), wet.getText().toString(), d, NotCollected);
////
//                                }
//                            });
//                            alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//
//                            final AlertDialog dialog1 = alertDialog.create();
//                            dialog1.show();
////
//                        }
//
//                    }
//                });
//            }
//
//        });
//    }
//
//    private void endTrip(String dry, String wet, String date, String list) {
//
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//
////        list = "-1,-2";
////        Log.e("Is empty",list);
//        if (list.equals("")){
//            list = "-1,-2";
//            Log.e("Null",list);
//        }
//        Call<endTripAck> call = apiInterface.endTripData(userSes.getToken(), userSes.getWorkerid(), dry, wet, date, userSes.getWard(), list);
//        call.enqueue(new Callback<endTripAck>() {
//            @Override
//            public void onResponse(Call<endTripAck> call, Response<endTripAck> response) {
//
//                try {
//                    Log.e("endTrip", response.body().getStat());
//
//                    if (response.body().getStat().equals("1")) {
//                        Toast.makeText(getContext(), "Thank You for your today's work!", Toast.LENGTH_SHORT).show();
//                        userSes.removeUser();
//                        Intent i = new Intent(getActivity(), MainActivity.class);
//                        startActivity(i);
//                        try{
//                            getActivity().finish();
//                            getActivity().overridePendingTransition(0, 0);
//                        }catch (NullPointerException ntp){}
//
//
//
//                    } else {
//                        Toast.makeText(getContext(), "something went wrong... retry!", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<endTripAck> call, Throwable t) {
//                Log.e("endTrip", t.toString());
//                Toast.makeText(getContext(), "Please Check your Internet Connection and try again...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void initViews(View view) {
//        try {
//            recyclerView = view.findViewById(R.id.NextHouses);
//            recyclerView.setHasFixedSize(true);
//            layoutManager = new LinearLayoutManager(getContext());
//            recyclerView.setLayoutManager(layoutManager);
//            pids = new HashSet<>();
//            uncollected = new HashSet<>();
//
//            Gson gson = new Gson();
////            String json = userSes.getJson();
//            Type type = new TypeToken<List<PlotList>>() {
//            }.getType();
////            plotLists = gson.fromJson(json, type);
//            count = plotLists.size();
//
//
//            int id = R.anim.recycler;
//            LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), id);
//            recyclerView.setLayoutAnimation(animationController);
//            dataAdapter = new DataAdapter(getContext(), plotLists);
//            recyclerView.setAdapter(dataAdapter);
//
//        } catch (Exception e) {
//        }
//
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void collectGarbage(String plotId, int c) {
//        if (c % 23 == 0) {
//            for (int i = 0; i < plotLists.size(); i++) {
//                if (plotLists.get(i).getId().equals(plotId)) {
//                    Log.e("called", "collect garbage");
////                    Toast.makeText(getContext(), "collect garbage", Toast.LENGTH_SHORT).show();
//                    notifyGarbageCollected(i);
//                    break;
//                }
//            }
//        }
//    }
//
//    private void notifyGarbageCollected(final int pos) {
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        UserSes userSes;
////        Call<Acknowledgement> call = apiInterface.collectedGarbage(userSes.getWorkerid(), userSes.getToken(), "id", "1");
//        call.enqueue(new Callback<Acknowledgement>() {
//            @Override
//            public void onResponse(Call<Acknowledgement> call, Response<Acknowledgement> response) {
//                try {
//                    if (response.body().getResponse().equals("1")) {
//                        Log.e("response", response.body().getResponse());
//                        Toast.makeText(getContext(), "Garbage collected of plot No :-" + plotLists.get(pos).getPlotNo(), Toast.LENGTH_SHORT).show();
//                      //  performScroll(pos);
//                        pids.add(plotLists.get(pos).getId());
//
//
//                    } else {
//                        Toast.makeText(getContext(), "Perform Rescan", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Log.e("notifyGarbageCollected", e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Acknowledgement> call, Throwable t) {
//                Log.e("notifyGarbageCollected", t.toString());
////               Toast.makeText(getContext(), "Please Check your Internet Connection and try again..", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
////                        Log.e(TAG, "User agreed to make required location settings changes.");
//                        // Nothing to do. startLocationupdates() gets called in onResume again.
//                        break;
//                    case Activity.RESULT_CANCELED:
////                        Log.e(TAG, "User chose not to make required location settings changes.");
//                        boolean mRequestingLocationUpdates = false;
//                        break;
//                }
//                break;
//        }
//    }
//
//    private boolean checkPermissions() {
//        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }
    
}





