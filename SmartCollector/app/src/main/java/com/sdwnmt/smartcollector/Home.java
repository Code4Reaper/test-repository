package com.sdwnmt.smartcollector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdwnmt.smartcollector.Modal.PlotList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.sdwnmt.smartcollector.MainActivity.plotLists;

public class Home extends AppCompatActivity {

    private ViewPager mPager ;
    List<PlotList> plotLists;
    private CardStackAdapter mAdapter ;
    private  UserSes userSes;
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
//        mPager.setPageTransformer(true, new CardStackTransformer());
        mPager.setAdapter(mAdapter);
        mPager.getAdapter();

    }

//    private class CardStackTransformer implements ViewPager.PageTransformer
//    {
//        @Override
//        public void transformPage(View page, float position)
//        {
//            if(position>=0)
//            {
//                page.setScaleX(0.8f - 0.02f * position);
//
//                page.setScaleY(0.8f);
//
//                page.setTranslationX(- page.getWidth()*position);
//
//                page.setTranslationY(30 * position);
//            }
//
//        }
//    }
}
