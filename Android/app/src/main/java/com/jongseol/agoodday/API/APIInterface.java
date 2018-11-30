package com.jongseol.agoodday.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jongseol.agoodday.Model.Device;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author jeje(las9897 @ gmail.com)
 * @file jongseol.agoodday.API.APIInterface.java
 * @brief Restful API. GET and POSt
 */
public interface APIInterface {

    @GET ( "device/{device_id}/" )
    Call<JsonArray> getDevice(@Path ( "device_id" ) String device_id, @Query("date") int date);

    @GET ( "device/{device_id}/" )
    Call<JsonArray> getDevice(@Path ( "device_id" ) String device_id, @Query("start_date") String start_date, @Query("end_date") String end_date);


}