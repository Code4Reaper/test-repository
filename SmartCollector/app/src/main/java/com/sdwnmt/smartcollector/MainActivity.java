package com.sdwnmt.smartcollector;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.sdwnmt.smartcollector.Modal.PlotList;
import com.sdwnmt.smartcollector.Modal.Result;
import com.sdwnmt.smartcollector.Modal.WorkerDet;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout uname, upass;
    private boolean isConnected = true;
    private boolean monitoringConnectivity = false;
    private ConnectivityManager.NetworkCallback connectivityCallback;
    private Button btn;
    PermissionResource permissionResource;
    public static List<PlotList> plotLists;
    public static PlotList plotList;
    public static Result result;
    Window window;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        }
        permissionResource = new PermissionResource();
        permissionResource.requestPerm(MainActivity.this);
        connectionCheck();
        btn = findViewById(R.id.logbtn);
        uname = findViewById(R.id.uname);
        upass = findViewById(R.id.upass);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void connectionCheck(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityCallback  = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    isConnected = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.e("State","NetConnected:true");
//              Toast.makeText(MainActivity.this, "NetConnected:True", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onLost(Network network) {
                    isConnected = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar =  Snackbar.make(findViewById(R.id.constraintLayout_login),"You Appear to be offline...Check your internet connection.",Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            snackbar.show();
                        }
                    });
                    Log.e("state","NetConnected:False");
//              Toast.makeText(MainActivity.this, "NetConnected:False", Toast.LENGTH_SHORT).show();
                }
            };
        }
    }

    public void performLogin() {
        String user = uname.getEditText().getText().toString();
        String userpass = upass.getEditText().getText().toString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<WorkerDet> call = apiInterface.performLogin(user, userpass);

        call.enqueue(new Callback<WorkerDet>() {
            @Override
            public void onResponse(Call<WorkerDet> call, Response<WorkerDet> response) {
                List<Result> results = response.body().getResult();
                try {
                    if (!results.get(0).getResult().equals("1")) {
                        Toast.makeText(MainActivity.this, "Incorrect Username or password", Toast.LENGTH_SHORT).show();

                    } else {
                        setData(results);
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){}

            }

            @Override
            public void onFailure(Call<WorkerDet> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Please Check your Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        });
        uname.getEditText().setText("");
        upass.getEditText().setText("");
    }


    private void setData(List<Result> results) {
        UserSes userSes = new UserSes(MainActivity.this);

        result = new Result();
        plotList = new PlotList();
        try {
            for (int i = 0; i < results.size(); i++) {
                result.setName(results.get(i).getName());
                result.setEmail(results.get(i).getEmail());
                result.setRoleId(results.get(i).getRoleId());
                result.setZoneid(results.get(i).getZoneid());
                result.setToken(results.get(i).getToken());
                result.setWorkerid(results.get(i).getWorkerid());
                result.setWard(results.get(i).getWard());
                result.setRoute(results.get(i).getRoute());
                result.setVehicle(results.get(i).getVehicle());

                plotLists = results.get(i).getPlotList();
                Gson gson = new Gson();
                String json = gson.toJson(plotLists);

                userSes.setJson(json);
                plotList.setAddress(plotLists.get(i).getAddress());
                plotList.setId(plotLists.get(i).getId());
                plotList.setPlotNo(plotLists.get(i).getPlotNo());
                plotList.setPlotOwner(plotLists.get(i).getPlotOwner());
            }
            if(!result.equals("")) {
                userSes.setFullname(result.getName());
                userSes.setZoneid(result.getZoneid());
                userSes.setRoleid(result.getRoleId());
                userSes.setEmail(result.getEmail());
                userSes.setToken(result.getToken());
                userSes.setWorkerid(result.getWorkerid());
                userSes.setWard(result.getWard());
                userSes.setRoute(result.getRoute());
                userSes.setVehicle(result.getVehicle());
            }else{
                Log.e("Login","No data avaliable");
            }
            //Toast.makeText(MainActivity.this,result.getToken(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, plotList.getAddress(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
    }


    private void checkConnectivity() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            Log.e("State","not connected");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar snackbar =  Snackbar.make(findViewById(R.id.constraintLayout_login),"You Appear to be offline...Check your internet connection.",Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.registerNetworkCallback(
                        new NetworkRequest.Builder()
                                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                                .build(), connectivityCallback);
            }
            monitoringConnectivity = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }

    @Override
    protected void onPause() {
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.unregisterNetworkCallback(connectivityCallback);
            }
            monitoringConnectivity = false;
        }
        super.onPause();
    }

}



