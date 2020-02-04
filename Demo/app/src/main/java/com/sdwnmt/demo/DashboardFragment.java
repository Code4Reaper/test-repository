package com.sdwnmt.demo;


import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.sdwnmt.demo.Modal.FeedbackRate;
import com.sdwnmt.demo.Modal.RateModal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {
    private RatingBar ratingBar;
    private TextInputLayout feedback;
    private Button feedBut,rateBut;
    private String feedS,rateS,pid,pid2;
    private UserSes userSes;
    private JSONObject jsonObject;
    private DateSes dateSes;
    private RateSes rateSes;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ratingBar = view.findViewById(R.id.ratingBar);
        feedback = view.findViewById(R.id.textInputLayout);
        feedBut = view.findViewById(R.id.button2);
        rateBut = view.findViewById(R.id.ratebtn);

        try {
            userSes = new UserSes(getContext());
            dateSes = new DateSes(getContext());
            rateSes = new RateSes(getContext());
        }catch (NullPointerException e){}

//        Log.e("onCreateView",getCurrentDate());
//        Log.e("onCreateView",dateSes.getDate());
        pid = userSes.getJsonString();
        try {
             jsonObject = new JSONObject(pid);
             pid2 = jsonObject.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        feedbackChecker();
        ratingChecker();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateS = String.valueOf(rating);
            }
        });
        rateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRateData();
            }
        });

        return view;
    }

    private void feedbackChecker(){

        if((dateSes.getDate().equals(""))||(!dateSes.getDate().equals(getCurrentDate()))) {
            Log.e("feedBack","If executed");
            feedBut.setFocusable(true);
            feedBut.setEnabled(true);
            feedback.getEditText().setText("");
            feedback.setEnabled(true);
        }else if(dateSes.getDate().equals(getCurrentDate())){
            Log.e("ratingAndFeedBack","else executed");
            feedBut.setFocusable(false);
            feedBut.setEnabled(false);
            feedback.getEditText().setText("You've already provided feedback for the day");
            feedback.setEnabled(false);
        }
        provideFeedback();
    }
    private void provideFeedback(){
        feedBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedS = feedback.getEditText().getText().toString();
                if (feedS.equals("")) {
                    Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("feedback", feedS);
                    feedBut.setFocusable(false);
                    feedBut.setEnabled(false);
                    feedback.setEnabled(false);

                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                    dateSes.setDate(simpleDateFormat.format(date));
                    submitData();
                }
            }
        });
    }

    private void ratingChecker(){
        if (!rateSes.getRate().equals("")){
            rateBut.setFocusable(false);
            rateBut.setEnabled(false);
            ratingBar.setRating(Float.valueOf(rateSes.getRate()));
            ratingBar.setIsIndicator(true);
        } else {
            rateBut.setEnabled(true);
            rateBut.setFocusable(true);
            ratingBar.setIsIndicator(false);

        }
    }

    private void submitData(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<FeedbackRate> call = apiInterface.sendInfo(feedS,pid2);
        call.enqueue(new Callback<FeedbackRate>() {
            @Override
            public void onResponse(Call<FeedbackRate> call, Response<FeedbackRate> response) {
                Log.e("submitData",response.body().getStatus());
                try {
                    if (response.body().getStatus().equals("1")) {
                        Toast.makeText(getContext(), "Feedback for the day is recorded", Toast.LENGTH_SHORT).show();
                        feedback.getEditText().setText("");
                    } else {
                        Toast.makeText(getContext(), "Error Submitting feedback, please try again", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<FeedbackRate> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void submitRateData(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RateModal> call = apiInterface.sendRating(rateS,pid2);
        call.enqueue(new Callback<RateModal>() {
            @Override
            public void onResponse(Call<RateModal> call, Response<RateModal> response) {
                rateSes.setRate(rateS);
                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                ratingChecker();
            }

            @Override
            public void onFailure(Call<RateModal> call, Throwable t) {
                Toast.makeText(getContext(), "Error Submitting rating, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getCurrentDate(){
//        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            return simpleDateFormat.format(date);
//        }catch (Exception e){
//            Toast.makeText(getContext(), "Something went wrong, Please try again !", Toast.LENGTH_SHORT).show();
//        }
//            return " ";
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
}
