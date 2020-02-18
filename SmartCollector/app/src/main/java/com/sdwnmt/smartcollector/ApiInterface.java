package com.sdwnmt.smartcollector;

import com.sdwnmt.smartcollector.Modal.ACK.Acknowledgement;
import com.sdwnmt.smartcollector.Modal.ACK.endTripAck;
import com.sdwnmt.smartcollector.Modal.ACK.locACK;
import com.sdwnmt.smartcollector.Modal.SendPlayerId;
import com.sdwnmt.smartcollector.Modal.WorkerDet;
import com.sdwnmt.smartcollector.Modal.ACK.syncACK;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("validateUser")
    @FormUrlEncoded
    Call<WorkerDet> performLogin(@Field("uname") String uname, @Field("upass") String upass);

    @POST("collectData")
    @FormUrlEncoded
    Call<Acknowledgement> collectedGarbage(@Field("worker_id")String wid,@Query("token") String toke, @Field("plot_id")String plot, @Field("response") String resp,@Field("lati") String latitude,@Field("long") String longitude);

    @POST("endTrip")
    @FormUrlEncoded
    Call<endTripAck> endTripData(@Query("token") String token, @Field("worker_id") String worker, @Field("dry")String dry, @Field("wet") String wet, @Field("date")String Date, @Field("ward") String ward);

    @POST("getWorkerLocation")
    @FormUrlEncoded
    Call<locACK> sendLocation(@Field("id")String wid, @Field("x")String x, @Field("y")String y,@Query("token")String Toke);

    @POST("sendWorkerPlayerId")
    @FormUrlEncoded
    Call<SendPlayerId> sendPlayerID(@Field("player_id") String playid, @Field("worker_id") String pid);

    @POST("syncNow")
    @FormUrlEncoded
    Call<syncACK> syncData(@Field("worker_id") String pid,@Field("date") String date,@Field("token") String token,@Field("unsynced_data") String sync);

}
