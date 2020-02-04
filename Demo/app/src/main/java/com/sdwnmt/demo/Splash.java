package com.sdwnmt.demo;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
        private ImageView iv;
        public UserSes userSes;
        private PermissionSes permissionSes;
        public TextView tv;
        Animation a,a2;
        public Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSes = new UserSes(Splash.this);
        permissionSes = new PermissionSes(this);
        setContentView(R.layout.activity_splash);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.nav));
        }
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.imageView6);
        a = AnimationUtils.loadAnimation(this,R.anim.myalpha);
        iv.startAnimation(a);
        tv.startAnimation(a);


        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4050);
                    if (!userSes.getFullname().equals("")) {
                        Intent homeIntent = new Intent(Splash.this, User_Home.class);
                        startActivity(homeIntent);
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this,LoginActivity.class));
                        finish();
                    }
                }
                catch (Exception e){e.printStackTrace();}
            }
        };
        t.start();
    }
}
