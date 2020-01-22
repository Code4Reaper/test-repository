package com.sdwnmt.smartcollector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.PlotList;
import com.sdwnmt.smartcollector.Modal.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdwnmt.smartcollector.MainActivity.plotLists;

public class Home extends AppCompatActivity {

    private ViewPager mPager ;
    List<PlotList> plotLists;
    private CardStackAdapter mAdapter ;
    private  UserSes userSes;
    public static Result result;
    private AlertDialog dialog;
    private Button b1,b2,clear;
    public String json;
    public FrameLayout notification;
    public static RecyclerView.LayoutManager layoutManager;
    public static RecyclerView recyclerView,notifyrecyclerView;
    private String pid, player_id, title = "0", message, body;
    private HashSet<String> pids;
    private HashSet<String> uncollected;
    private List<String> notifyList;
    private DataAdapter dataAdapter;
    private NotificationAdapter notificationAdapter;
    private UserNotify userNotify;


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
        mPager.getAdapter();
        initRecycler();

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
}



