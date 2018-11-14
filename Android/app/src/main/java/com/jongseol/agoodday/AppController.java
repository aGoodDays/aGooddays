package com.jongseol.agoodday;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {
    private static AppController instance;

    public static AppController getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.instance = this;
    }


    private RestService restService;
    public RestService getRestService() {
        return restService;
    }
    private String baseURL;

    public void buildRestService(String ip, int port){
        synchronized (AppController.class){
            if(restService == null){
                baseURL = "http://10.0.2.2:8000/";
                Gson gson = new GsonBuilder().create();

                GsonConverterFactory factory = GsonConverterFactory.create(gson);
                // 서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해 사용
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(factory).build();
                restService = retrofit.create(RestService.class);
            }
        }
    }
}
