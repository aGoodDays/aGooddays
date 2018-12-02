package com.jongseol.agoodday.API;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @file jongseol.agoodday.API.APIInterface.java
 * @brief Retrofit을 이용한 통신 구현부입니다. 데이터는 디바이스를 통해서 서버로 바로 전송되기 때문에 데이터를 불러오는 GET 방식만 사용합니다.
 * @author jeje(las9897@gmail.com)
 */
public interface APIInterface {


    /**
     * @brief 오늘을 기준으로 입력된 date 일 전까지의 데이터를 불러옵니다. 7일, 14일, 28일의 데이터를 가져올 수 있도록 버튼을 만들어서 사용합니다.
     * @author jeje (las9897@gmail.com)
     * @param device_id
     * @param date
     * @return Call
     */
    @GET ( "device/{device_id}/" )
    Call<JsonArray> getPostureData(@Path ( "device_id" ) String device_id, @Query("date") int date);


    /**
     * @brief 날짜의 범위를 지정하여 데이터를 불러옵니다.
     * @author jeje (las9897@gmail.com)
     * @param device_id
     * @param start_date
     * @param end_date
     * @return Call
     */
    @GET ( "device/{device_id}/" )
    Call<JsonArray> getPostureData(@Path ( "device_id" ) String device_id, @Query("start_date") String start_date, @Query("end_date") String end_date);


}