package com.jongseol.agoodday.API;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @file jongseol.agoodday.API.APIClient.java
 * @brief Using open source Retrofit, OkHttpClient and Setting
 * @author jeje(las9897@gmail.com)
 */
public class APIClient {

    private static Retrofit retrofit = null;

    /**
     * @brief get Client and Retrofit setting
     * @return Retrofit
     */
    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://220.149.242.12:51222")
                //.baseUrl("http://localhost:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
