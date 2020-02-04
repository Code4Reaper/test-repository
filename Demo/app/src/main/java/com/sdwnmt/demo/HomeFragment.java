package com.sdwnmt.demo;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdwnmt.demo.Modal.Acknowledge;
import com.sdwnmt.demo.Modal.SendPlayerId;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OSSubscriptionObserver {
    private String body,title,message,plot_id, player_id;
    private TextView uname,mob,plotNo,add,mail,t1,t2;
    private Button logout,collect,notCollect,close;
    private UserSes userSes;
    private JSONObject jsonObject;
    private NoteSes noteSes;
    private ImageView notify;
    private boolean flag;
    private Animation animation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            userSes = new UserSes(getContext());
        }catch (NullPointerException npe){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        Log.e("tag","onCreateView");
        uname = view.findViewById(R.id.uname);
        mob = view.findViewById(R.id.mobile);
        plotNo = view.findViewById(R.id.plot);
        add = view.findViewById(R.id.address);
        mail = view.findViewById(R.id.email);
        logout = view.findViewById(R.id.logout);
        notify = view.findViewById(R.id.notify);

        String str = userSes.getJsonString();
        try {
          jsonObject = new JSONObject(str);

//            Toast.makeText(getContext(), jsonObject.getString("id"), Toast.LENGTH_SHORT).show();
          plot_id = jsonObject.getString("id");
            //Toast.makeText(getContext(), plot_id, Toast.LENGTH_SHORT).show();

            uname.setText(jsonObject.getString("name"));
            mob.setText(jsonObject.getString("mobile"));
            plotNo.setText(jsonObject.getString("plot"));
            mail.setText(jsonObject.getString("email"));
            add.setText(jsonObject.getString("address"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        makeCall();
        noteSes = new NoteSes(getContext());
        if(body != null) {
            noteSes.setNote(body);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

        return view;
    }
    public void logout(){
        userSes.removeUser();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().finish();
        getActivity().overridePendingTransition(0,0);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.addSubscriptionObserver(this);
        OneSignal.startInit(getContext())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();
        Log.e("init","onCreate");



        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();

        status.getSubscriptionStatus().getSubscribed();
        status.getSubscriptionStatus().getUserSubscriptionSetting();
        player_id = status.getSubscriptionStatus().getUserId();
        status.getSubscriptionStatus().getPushToken();
        OneSignal.sendTag("user_id", "1234");
//        Toast.makeText(getContext(), player_id, Toast.LENGTH_SHORT).show();

    }

    private void makeCall(){
//        Toast.makeText(getContext(), player_id, Toast.LENGTH_SHORT).show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SendPlayerId> call = apiInterface.sendPlayerID(player_id,plot_id);
        call.enqueue(new Callback<SendPlayerId>() {
            @Override
            public void onResponse(Call<SendPlayerId> call, Response<SendPlayerId> response) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<SendPlayerId> call, Throwable t) {
//                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            new AlertDialog.Builder(getContext())
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show();
            // get player ID
            stateChanges.getTo().getUserId();

            Toast.makeText(getContext(), player_id, Toast.LENGTH_SHORT).show();

        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;

            title = result.notification.payload.title;

            body = result.notification.payload.body;
            Log.e("notification",body);
            String customKey;

            Log.e("abc","notificationOpened");

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);

            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        }
    }
    public void showPop(){
        Log.e("abc","OnCreateView");
        AlertDialog.Builder malert = new AlertDialog.Builder(getContext());
        final View v1 = getLayoutInflater().inflate(R.layout.modal, null);

        t1 = v1.findViewById(R.id.title);
        t2 = v1.findViewById(R.id.body);

        collect = v1.findViewById(R.id.yes);
        notCollect = v1.findViewById(R.id.no);
        close = v1.findViewById(R.id.close);
//        Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();

//        Toast.makeText(getContext(), body, Toast.LENGTH_SHORT).show();
        malert.setView(v1);

        if (!noteSes.getNote().equals("")) {
            t1.setText("Smart User");
            t2.setText(noteSes.getNote());
            t2.setEnabled(true);
            collect.setVisibility(View.VISIBLE);
            notCollect.setVisibility(View.VISIBLE);
            close.setVisibility(View.INVISIBLE);


        } else {
            t2.setText("No Notifications Yet.!");
            t2.setEnabled(false);
            collect.setVisibility(View.INVISIBLE);
            notCollect.setVisibility(View.INVISIBLE);
            close.setVisibility(View.VISIBLE);

        }
        final AlertDialog dialog = malert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        acknowledgeNotification(dialog);


    }

    private void acknowledgeNotification(final AlertDialog dialog){
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Acknowledge> call = apiInterface.sendAck("collected",plot_id);
                call.enqueue(new Callback<Acknowledge>() {
                    @Override
                    public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                        if(response.body().getRStat().equals("1")){
                            noteSes.remove();
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<Acknowledge> call, Throwable t) {
                        Log.e("acknot",t.toString());
                        Toast.makeText(getContext(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        notCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Acknowledge> call = apiInterface.sendAck("Not Collected",plot_id);
                call.enqueue(new Callback<Acknowledge>() {
                    @Override
                    public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                        if(response.body().getRStat().equals("1")){
                            noteSes.remove();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Acknowledge> call, Throwable t) {

                    }
                });
            }
        });

    }
}
