package com.sdwnmt.smartcollector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.sdwnmt.smartcollector.Modal.SendPlayerId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements OSSubscriptionObserver {

    private TextView Wname,zone,route,vehicle,email,t1,t2,t3;
    private Button btn1,btn2;
    public UserSes userSes;
    private RecyclerView notifyrecyclerView;
    private String player_id,title,body;
    private FloatingActionButton notify;
    private List<String> notifyList;
    private NotificationAdapter notificationAdapter;

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
            showPop();
            }
        });
        try{
            userSes = new UserSes(Profile.this);
        }catch (NullPointerException npe){
            Toast.makeText(Profile.this, "Something went wrong..please try again", Toast.LENGTH_SHORT).show();
        }

        makeCall();
        notifyList = new ArrayList<>();
        readFromFile();
        //Location start

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
            writeToFile(body);
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

    public void showPop() {
        AlertDialog.Builder malert = new AlertDialog.Builder(Profile.this);
        final View v1 = getLayoutInflater().inflate(R.layout.notifymodal, null);
        t1 = v1.findViewById(R.id.title);
        t2 = v1.findViewById(R.id.button);
        t3 = v1.findViewById(R.id.textView);
//        clear = v1.findViewById(R.id.clear)
        notifyrecyclerView = v1.findViewById(R.id.notify);

        if (notifyList.isEmpty()){
            notifyrecyclerView.setVisibility(View.INVISIBLE);
        }else{
            t3.setVisibility(View.INVISIBLE);
            notifyrecyclerView.setHasFixedSize(true);
            notifyrecyclerView.setLayoutManager(new LinearLayoutManager(Profile.this));
            Collections.reverse(notifyList);
            notificationAdapter = new NotificationAdapter(Profile.this,notifyList);
            notifyrecyclerView.setAdapter(notificationAdapter);
        }

        Log.e("abc", "pop");
        malert.setView(v1);
        final AlertDialog dialog = malert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void readFromFile(){
        try {
//            new FileOutputStream("Notification.txt").close();
            FileInputStream fileInputStream = this.openFileInput("Notification.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String NewNote;

            while ((NewNote = bufferedReader.readLine()) != null){
                if(!NewNote.equals(""))
                    notifyList.add(NewNote);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String body){
        FileOutputStream fileoutputStream = null;
        try {
            fileoutputStream = this.openFileOutput("Notification.txt", Context.MODE_APPEND);
            fileoutputStream.write(("\n"+body).getBytes());
            fileoutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewData(){
        try {
            Wname.setText(userSes.getFullname());
            zone.setText(userSes.getZoneid());
            route.setText(userSes.getRoute());
            email.setText(userSes.getEmail());
            vehicle.setText(userSes.getVehicle());
        }catch (Exception e){}
    }


}
