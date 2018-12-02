package com.jongseol.agoodday.API;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @file jongseol.agoodday.API.APIClient.java
 * @brief Retrofit 모듈을 사용하기 위한 설정값들입니다. 클라이언트로 OkHttpClient를, Convert는 GsonCovertFactory를 사용했습니다.
 * @author jeje(las9897@gmail.com)
 */
public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://220.149.242.12:51222")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
