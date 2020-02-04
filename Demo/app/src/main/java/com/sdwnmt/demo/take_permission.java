package com.sdwnmt.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.spark.submitbutton.SubmitButton;

public  class take_permission extends AppCompatActivity {
    UserSes userSes;
    private SubmitButton btn;
    private ImageView jet1,jet2;
    PermissionResource permissionResource;
    private PermissionSes permissionSes;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_permission);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.myColor));
        }
        permissionResource = new PermissionResource();
        btn = findViewById(R.id.submitButton);
        jet1 = findViewById(R.id.jet);
        jet2 = findViewById(R.id.jet2);



        permissionResource.requestPerm(take_permission.this);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(jet1,"y",1600f);
        ObjectAnimator animatorYx = ObjectAnimator.ofFloat(jet2,"y",1600f);

        animatorY.setStartDelay(100);
        animatorY.setDuration(1500);
        animatorYx.setStartDelay(100);
        animatorYx.setDuration(1500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorY,animatorYx);
        animatorSet.start();
//        animatorSet.playTogether(animatorYx);
//        animatorSet.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText("Almost there !");
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(jet1,"y",-500f);
                ObjectAnimator animatorYx = ObjectAnimator.ofFloat(jet2,"y",-400f);

                animatorY.setStartDelay(100);
                animatorY.setDuration(1500);
                animatorYx.setStartDelay(120);
                animatorYx.setDuration(1500);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorY,animatorYx);
                animatorSet.start();

                btn.setText("Finish");
                if (permissionResource.status == 1){
                    Log.e("onClick","if executed");
                    startActivity(new Intent(take_permission.this,MainActivity.class));
                    finish();

                } else {
                    permissionResource.requestPerm(take_permission.this);
                }
            }
        });

    }
//    public void handleAnimation(View view){
//
//
//
//
//
//
//        ObjectAnimator animatorY2 = ObjectAnimator.ofFloat(jet,"y",100f);
//        animatorY.setStartDelay(100);
//        animatorY.setDuration(100);
//        AnimatorSet animatorSet2 = new AnimatorSet();
//        animatorSet2.playTogether(animatorY2);
//        animatorSet2.start();
//    }


}
