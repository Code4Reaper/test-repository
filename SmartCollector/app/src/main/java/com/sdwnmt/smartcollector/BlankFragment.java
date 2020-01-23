package com.sdwnmt.smartcollector;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.ACK.endTripAck;
import com.sdwnmt.smartcollector.Modal.PlotList;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdwnmt.smartcollector.Home.layoutManager;
import static com.sdwnmt.smartcollector.Home.mPager;
import static com.sdwnmt.smartcollector.Home.recyclerView;

public class BlankFragment extends Fragment {

    private TextView name,plot,address;
    private FusedLocationProviderClient client;
    Button btn1,btn2,b2;
    Double lat,log;
    private AlertDialog dialog;
    ConstraintLayout layout;
    private UserSes userSes;
    private List<PlotList> plotLists;
    int flag = 0, flag2 = 0;

    String pid;
    public BlankFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blank, container, false);
        userSes = new UserSes(getContext());
        Gson gson = new Gson();
           String json = userSes.getJson();
            Type type = new TypeToken<List<PlotList>>() {
            }.getType();
          plotLists = gson.fromJson(json, type);


        layout = view.findViewById(R.id.layout);
        name = view.findViewById(R.id.name);
        plot = view.findViewById(R.id.plot);
        address = view.findViewById(R.id.address);
        btn1 = view.findViewById(R.id.button4);
        btn2 = view.findViewById(R.id.button5);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectGarbage("1");
                extractLocation();
//                layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.collectedcard));

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectGarbage("0");
                extractLocation();

            }
        });



        assert getArguments() != null;
        name.setText(getArguments().getString("Name"));
        plot.setText(getArguments().getString("Plot"));
        address.setText(getArguments().getString("Address"));
        pid = getArguments().getString("pid");
        return view;
    }

    private void collectGarbage(String resp) {
            for (int i = 0; i < plotLists.size(); i++) {
                if (plotLists.get(i).getId().equals(pid)) {
                    Log.e("called", "collect garbage");
//                    Toast.makeText(getContext(), "collect garbage", Toast.LENGTH_SHORT).show();
                    notifyGarbageCollected(i,resp);
                    break;
                }
            }
        }

    private void notifyGarbageCollected(final int pos, final String resp) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Acknowledgement> call = apiInterface.collectedGarbage(userSes.getWorkerid(), userSes.getToken(),pid,resp);
        call.enqueue(new Callback<Acknowledgement>() {
            @Override
            public void onResponse(Call<Acknowledgement> call, Response<Acknowledgement> response) {
                try {
                        Log.e("response", response.body().getResponse());
                        Toast.makeText(getContext(), "Garbage collected of plot No :-" + plotLists.get(pos).getPlotNo(), Toast.LENGTH_SHORT).show();
                         performScroll(pos,resp);

                         if((mPager.getAdapter().getCount() -1) != (mPager.getCurrentItem())) {
                             mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
                         }else{
                             pop();
                         }

                } catch (Exception e) {
                    Log.e("notifyGarbageCollected", e.toString());
                }
            }

            @Override
            public void onFailure(Call<Acknowledgement> call, Throwable t) {
                Log.e("GarbageCollectedFailed", t.toString());
//               Toast.makeText(getContext(), "Please Check your Internet Connection and try again..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void performScroll(final int pos,final String resp) {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
            private static final float MILLISECONDS_PER_INCH = 120f;

            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        smoothScroller.setTargetPosition(pos);
        layoutManager.startSmoothScroll(smoothScroller);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int finalI = pos;

                recyclerView.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                       if (flag == 0) {
                            try {
                                if(resp.equals("1")) {
                                    Log.e("Tag", "imgchange called");
                                    View view = recyclerView.getLayoutManager().findViewByPosition(finalI);
                                    ((ImageView) view.findViewById(R.id.img)).setImageResource(R.drawable.greendust);
                                    ((CardView) view.findViewById(R.id.Card)).setCardBackgroundColor(Color.parseColor("#cbe2c0"));
                                }else{
                                    Log.e("Tag", "not collected");
                                    View view = recyclerView.getLayoutManager().findViewByPosition(finalI);

                                    ((CardView) view.findViewById(R.id.Card)).setCardBackgroundColor(Color.parseColor("#FF3D2B"));
                                    ((TextView) view.findViewById(R.id.name)).setTextColor(Color.parseColor("#FFFFFF"));
                                    ((TextView) view.findViewById(R.id.plot_no)).setTextColor(Color.parseColor("#FFFFFF"));
                                    ((TextView) view.findViewById(R.id.address)).setTextColor(Color.parseColor("#FFFFFF"));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        flag++;
                    }
                });
            }
        });
        flag = 0;
    }
        private void pop() {
                Button b1;
                final TextView dry, wet;
//               ModalPop modalPop = new ModalPop();
//                modalPop.show(getFragmentManager(),"ModalPop
                AlertDialog.Builder malert = new AlertDialog.Builder(getContext());
                final View v1 = getLayoutInflater().inflate(R.layout.modal, null);
                b1 = v1.findViewById(R.id.clear);
                b2 = v1.findViewById(R.id.cancel);
                dry = v1.findViewById(R.id.DryG);
                wet = v1.findViewById(R.id.wetG);
                malert.setView(v1);
                dialog = malert.create();
                dialog.setCanceledOnTouchOutside(false);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
//                        Log.e("pop", String.valueOf(pids.size()));
//                        for (int i = 0; i < plotLists.size(); i++) {
//                            if (!pids.contains(plotLists.get(i).getId())) {
//                                uncollected.add(plotLists.get(i).getId());
//                            }
//                        }
//                        Log.e("pop", uncollected.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
//                        final String NotCollected = uncollected.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                        final String d = simpleDateFormat.format(date);
                        Log.e("pop", d);
                        //Toast.makeText(getContext(), dry.getText().toString(), Toast.LENGTH_SHORT).show();
                        if ((dry.getText().toString().equals("")) || (wet.getText().toString().equals(""))) {
                            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Confirmation");
                            alertDialog.setMessage("Are you sure you want to Submit? \n You will be logged out after submitting.");
                            alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    endTrip(dry.getText().toString(), wet.getText().toString(), d, NotCollected);
//
                                }
                            });
                            alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            final AlertDialog dialog1 = alertDialog.create();
                            dialog1.show();
//
                        }

                    }
                });


    }
    private void endTrip(String dry, String wet, String date, String list) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

//        list = "-1,-2";
//        Log.e("Is empty",list);
        if (list.equals("")){
            list = "-1,-2";
            Log.e("Null",list);
        }
        Call<endTripAck> call = apiInterface.endTripData(userSes.getToken(), userSes.getWorkerid(), dry, wet, date, userSes.getWard(), list);
        call.enqueue(new Callback<endTripAck>() {
            @Override
            public void onResponse(Call<endTripAck> call, Response<endTripAck> response) {

                try {
                    Log.e("endTrip", response.body().getStat());

                    if (response.body().getStat().equals("1")) {
                        Toast.makeText(getContext(), "Thank You for your today's work!", Toast.LENGTH_SHORT).show();
                        userSes.removeUser();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        try{
                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);
                        }catch (NullPointerException ntp){}



                    } else {
                        Toast.makeText(getContext(), "something went wrong... retry!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<endTripAck> call, Throwable t) {
                Log.e("endTrip", t.toString());
                Toast.makeText(getContext(), "Please Check your Internet Connection and try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void extractLocation(){
        client = LocationServices.getFusedLocationProviderClient(getContext());
        client.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!= null) {
                    lat = location.getLatitude();
                    log = location.getLongitude();
//                    Toast.makeText(getContext(),String.valueOf(lat), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), String.valueOf(log), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
