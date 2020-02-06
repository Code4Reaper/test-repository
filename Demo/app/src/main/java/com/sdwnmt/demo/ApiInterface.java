package com.sdwnmt.demo;

import com.sdwnmt.demo.Modal.Acknowledge;
import com.sdwnmt.demo.Modal.Coordinates;
import com.sdwnmt.demo.Modal.FeedbackRate;
import com.sdwnmt.demo.Modal.LoginResp;
import com.sdwnmt.demo.Modal.RateModal;
import com.sdwnmt.demo.Modal.SendPlayerId;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

//    @POST("UserLog.php")
//    @FormUrlEncoded
//    Call<UserLog> getLoginInfo(@Field("user_pass") String passme);

    @POST("userLogin")
    @FormUrlEncoded
    Call<LoginResp> verifyLogin(@Field("mobile") String mob,@Field("adhar") String adh);

    @POST("userFeedback")
    @FormUrlEncoded
    Call<FeedbackRate> sendInfo(@Field("feedback") String feed, @Field("plot_id") String pid);

    @POST("userRating")
    @FormUrlEncoded
    Call<RateModal> sendRating(@Field("rate") String rate, @Field("plot_id") String pid);

    @POST("userAck")
    @FormUrlEncoded
    Call<Acknowledge> sendAck(@Field("ack") String ack, @Field("plot_id") String plot_id);

    @POST("sendPlayerId")
    @FormUrlEncoded
    Call<SendPlayerId> sendPlayerID(@Field("player_id") String playid, @Field("plot_id") String pid);

    @POST("getMyWorkerLocation")
    @FormUrlEncoded
    Call<Coordinates> acquireCoordinates(@Query ("token") String toke,@Field("lane_id") String Lane, @Field("plot_id") String Plot);
}
