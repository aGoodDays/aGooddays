package com.jongseol.agoodday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jongseol.agoodday.API.APIClient;
import com.jongseol.agoodday.API.APIInterface;
import com.jongseol.agoodday.Adapter.PostureListAdapter;
import com.jongseol.agoodday.Model.Posture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    private Button btn_get, btn_put, btn_test;
    private ListView listView;
    private Vibrator vibrator;
    private APIInterface apiInterface;
    private ArrayList<Posture> postureArrayList;
    private PostureListAdapter postureListAdapter;
    public static final TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");
    public static final SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
    private String device_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        device_id = "901180076";
        btn_get = (Button) findViewById(R.id.main2_btn_get);
        btn_put = (Button) findViewById(R.id.main2_btn_put);
        btn_test = (Button) findViewById(R.id.main2_btn_test);
        listView = (ListView) findViewById(R.id.main2_listview);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        simpledateformat.setTimeZone(timezone);
        postureArrayList = new ArrayList<>();
        postureListAdapter = new PostureListAdapter(postureArrayList);



        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btn_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(1000);
            }
        });

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postureArrayList.clear();
                Call<JsonArray> call = apiInterface.getDevice(device_id);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            JsonArray jsonArray = response.body();
                            String today = simpledateformat.format(new Date());
                            Posture posture = new Posture(device_id, today);
                            for (JsonElement e : jsonArray) {
                                if (!today.equals(e.getAsJsonObject().get("date").getAsString())) { // 오늘 날짜가 아니라면
                                    if (posture.all_count == 0)
                                        posture.all_count = 1;
                                    posture.ratio = (float) posture.bad_count / posture.all_count;
                                    postureArrayList.add(posture); // list에 추가
                                    posture = new Posture(device_id, e.getAsJsonObject().get("date").getAsString()); // 새로운 객체 생성
                                    today = e.getAsJsonObject().get("date").getAsString();
                                }
                                posture.all_count++;
                                if (e.getAsJsonObject().get("posture").getAsInt() == 1) {
                                    posture.bad_count++;
                                }
                            }
                            if (posture.all_count == 0)
                                posture.all_count = 1;
                            posture.ratio = (float) posture.bad_count / posture.all_count;
                            postureArrayList.add(posture); // 마지막 객체 삽입
                            listView.setAdapter(postureListAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Toast.makeText(Main2Activity.this, "Sync Fail", Toast.LENGTH_SHORT).show();
                    }
                });
                listView.setVisibility(View.VISIBLE);
            }
        });
    }
}
