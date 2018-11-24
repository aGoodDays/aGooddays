package com.jongseol.agoodday.Trash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jongseol.agoodday.Adapter.PostureListAdapter;
import com.jongseol.agoodday.R;
import com.jongseol.agoodday.Model.Device;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostureActivity extends AppCompatActivity {

    private String device_id;
    private Button btn_onoff, btn_refresh;
    private EditText responsiveness, startdate, enddate;
    private ListView listview;
    private PostureListAdapter postureListAdapter;
    private ArrayList<Device> deviceArrayList;
    private Date last_refresh_date;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd %HH:%mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture);
        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");
        btn_onoff = (Button) findViewById(R.id.posture_btn_onoff);
        btn_refresh = (Button) findViewById(R.id.posture_btn_refresh);
        responsiveness = (EditText) findViewById(R.id.posture_edittext_responsiveness);
        startdate = (EditText) findViewById(R.id.posture_edittext_startdate);
        enddate = (EditText) findViewById(R.id.posture_edittext_enddate);
        listview = (ListView) findViewById(R.id.posture_listview);



        //DB 접속
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "agoodday", null, 1);
        dbHelper.select("select * from " + DBHelper.TABLE_DEVICE);






        //On/Off
        btn_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //새로고침
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postureListAdapter = new PostureListAdapter(PostureActivity.this, R.layout.posture_item, deviceArrayList);
                listview.setAdapter(postureListAdapter);


                //새로고침시 갱신
                last_refresh_date = new Date(System.currentTimeMillis());
            }
        });




    }
}
