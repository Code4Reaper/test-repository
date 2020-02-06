package com.sdwnmt.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    SurfaceView surfaceView;
    TextView textView;
    ImageView line;
    BarcodeDetector barcodeDetector;
    TranslateAnimation mAnimation;

    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        line  = findViewById(R.id.imageView4);
        line.setVisibility(View.VISIBLE);
        mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, .34f);
        mAnimation.setDuration(2000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        line.setAnimation(mAnimation);


            Log.e("tag","camera called");
            surfaceView = findViewById(R.id.cameraView);
            textView = findViewById(R.id.textView);
            startScanner();

    }

    public void startScanner(){
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            int i = 0;

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            i += 1;
                            if (i == 1) {
                                String data = qrCodes.valueAt(0).displayValue;
                                //performLogin(pass);
                                    data = data.replaceAll("[\\\\][\"]{1}", "\"");
                                    data = data.substring(data.indexOf("{"), data.lastIndexOf("}") + 1);
                                Toast.makeText(MainActivity.this,data, Toast.LENGTH_SHORT).show();
                                    UserSes userSes = new UserSes(MainActivity.this);
                                    userSes.setJsonString(data);
                                    userSes.setFullname(data);

                                    Intent i = new Intent(MainActivity.this, User_Home.class);
                                    startActivity(i);
                                    finish();
                            }
                        }
                    });

                }
            }
        });
    }

//    private void performLogin(String pass) {
//
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<UserLog> call = apiInterface.getLoginInfo(pass);
//        call.enqueue(new Callback<UserLog>() {
//            @Override
//            public void onResponse(Call<UserLog> call, Response<UserLog> response) {
//                if (response.body().getResponse().equals("ok")) {
//                    Toast.makeText(MainActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
//
//                    Intent i = new Intent(MainActivity.this, User_Home.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    Toast.makeText(MainActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserLog> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            cameraSource.release();
            barcodeDetector.release();
        }catch (Exception e){
            Log.e("OnDestroy",e.toString());
        }

    }
}
