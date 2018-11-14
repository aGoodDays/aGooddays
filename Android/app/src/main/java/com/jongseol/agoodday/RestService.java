package com.jongseol.agoodday;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService{
    @GET("posture/{device_id}")
    Call<List<Device>> getDevice(@Path("device_id") String device_id);

}