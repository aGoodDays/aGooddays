package com.jongseol.agoodday.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jongseol.agoodday.Model.Device;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("posture/{device_id}")
    Call<List<Device>> getDevice(@Path("device_id") String device_id);

    @GET("posture/{device_id}")
    Call<List<Device>> getDevice(@Path("device_id") String device_id,
                                 @Query("start_date") String start_date,
                                 @Query("end_date") String end_date);

    @GET("posture/{device_id}/")
    Call<JsonArray> getJson(@Path("device_id") String device_id);

    @GET("posture/{device_id}/")
    Call<JsonArray> getJson(@Path("device_id") String device_id,
                            @Query("start_date") String start_date,
                            @Query("end_date") String end_date);

 /*   @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("posture/insert/")
    Call<JsonArray> insertJson(@Body String jsonElement);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("posture/insert/")
    Call<JsonElement> insertJson(@Body JsonElement jsonElement);*/

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("posture/insert/")
    Call<JsonArray> insertJson(@Body JsonArray jsonArray);

    @FormUrlEncoded
    @POST("posture/insert/")
    Call<Device> insertDevice(@Field("device_id") String device_id,
                              @Field("posture") int postrue,
                              @Field("saX") float saX,
                              @Field("saY") float saY,
                              @Field("saZ") float saZ,
                              @Field("sgX") float sgX,
                              @Field("sgY") float sgY,
                              @Field("sgZ") float sgZ,
                              @Field("xdegree") float xdegree,
                              @Field("ydegree") float ydegree,
                              @Field("zdegree") float zdegree
    );

}