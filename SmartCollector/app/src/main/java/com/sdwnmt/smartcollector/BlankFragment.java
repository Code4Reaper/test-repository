package com.sdwnmt.smartcollector;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.PlotList;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sdwnmt.smartcollector.Home.layoutManager;
import static com.sdwnmt.smartcollector.Home.recyclerView;

public class BlankFragment extends Fragment {

    private TextView name,plot,address;
    Button btn1,btn2;
    ConstraintLayout layout;
    private UserSes userSes;
    private List<PlotList> plotLists;
    private HashSet<String> pids;
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
                Toast.makeText(getContext(),getArguments().getString("Name"), Toast.LENGTH_SHORT).show();
                collectGarbage();
//                layout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.collectedcard));

            }
        });



        assert getArguments() != null;
        name.setText(getArguments().getString("Name"));
        plot.setText(getArguments().getString("Plot"));
        address.setText(getArguments().getString("Address"));
        pid = getArguments().getString("pid");
        return view;
    }

    private void collectGarbage() {
            for (int i = 0; i < plotLists.size(); i++) {
                if (plotLists.get(i).getId().equals(pid)) {
                    Log.e("called", "collect garbage");
//                    Toast.makeText(getContext(), "collect garbage", Toast.LENGTH_SHORT).show();
                    notifyGarbageCollected(i);
                    break;
                }
            }
        }

    private void notifyGarbageCollected(final int pos) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Acknowledgement> call = apiInterface.collectedGarbage(userSes.getWorkerid(), userSes.getToken(),pid, "1");
        call.enqueue(new Callback<Acknowledgement>() {
            @Override
            public void onResponse(Call<Acknowledgement> call, Response<Acknowledgement> response) {
                try {
                    if (response.body().getResponse().equals("1")) {
                        Log.e("response", response.body().getResponse());
                        Toast.makeText(getContext(), "Garbage collected of plot No :-" + plotLists.get(pos).getPlotNo(), Toast.LENGTH_SHORT).show();
                         performScroll(pos);
                        pids.add(plotLists.get(pos).getId());


                    } else {
                        Toast.makeText(getContext(), "Perform Rescan", Toast.LENGTH_SHORT).show();
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

    private void performScroll(final int pos) {
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
//                        if (flag == 0) {
                            try {
                                Log.e("Tag", "imgchange called");
                                View view = recyclerView.getLayoutManager().findViewByPosition(finalI);
                                ((ImageView) view.findViewById(R.id.img)).setImageResource(R.drawable.greendust);
                                ((CardView) view.findViewById(R.id.Card)).setCardBackgroundColor(Color.parseColor("#cbe2c0"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                        }
//                        flag++;
                    }
                });
            }
        });
//        flag = 0;
    }
}
