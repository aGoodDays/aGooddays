package com.jongseol.agoodday;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jongseol.agoodday.API.APIClient;
import com.jongseol.agoodday.API.APIInterface;
import com.jongseol.agoodday.Adapter.PostureAdapter;
import com.jongseol.agoodday.Model.Posture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static MainActivity myActivity;

    //Bluetooth
    public BluetoothSPP bluetoothSPP;

    //Vibrator
    public static Vibrator vibrator;

    private LinearLayout layout_bluetooth, layout_level, layout_controller, layout_mode;
    private Button btn_connect, btn_level1, btn_level2, btn_level3, btn_on, btn_stop, btn_off, btn_refresh, btn_server, btn_local, btn_view;
    private TextView textView_device_id;
    private ListView listView;

    private int LEVEL = 0;

    public static final TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");
    public static final SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");

    private String device_id;

    private APIInterface apiInterface;
    private ArrayList<Posture> postureArrayList;
    private PostureAdapter postureAdapter;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {//btn_connect
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.connect(data); // 연결 시도
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = this;

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //TimeZone Setting
        simpledateformat.setTimeZone(timezone);

        //VIew Init
        layout_bluetooth = (LinearLayout) findViewById(R.id.main_layout_bluetooth);
        layout_level = (LinearLayout) findViewById(R.id.main_layout_level);
        layout_controller = (LinearLayout) findViewById(R.id.main_layout_controller);
        layout_mode = (LinearLayout) findViewById(R.id.main_layout_mode);
        btn_connect = (Button) findViewById(R.id.main_btn_connect);
        btn_server = (Button) findViewById(R.id.main_btn_server);
        btn_local = (Button) findViewById(R.id.main_btn_local);
        btn_level1 = (Button) findViewById(R.id.main_btn_level_1);
        btn_level2 = (Button) findViewById(R.id.main_btn_level_2);
        btn_level3 = (Button) findViewById(R.id.main_btn_level_3);
        btn_on = (Button) findViewById(R.id.main_btn_on);
        btn_stop = (Button) findViewById(R.id.main_btn_stop);
        btn_off = (Button) findViewById(R.id.main_btn_off);
        btn_refresh = (Button) findViewById(R.id.main_btn_refresh);
        btn_view = (Button) findViewById(R.id.main_btn_view);
        textView_device_id = (TextView) findViewById(R.id.main_textview_device_id);
        listView = (ListView) findViewById(R.id.main_listview);

        //API Setting
        apiInterface = APIClient.getClient().create(APIInterface.class);

        //ListView Setting
        postureArrayList = new ArrayList<>();
        postureAdapter = new PostureAdapter(postureArrayList);

        //VISIBLE Setting
        layout_mode.setVisibility(View.GONE);
        layout_level.setVisibility(View.GONE);
        layout_controller.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        //Button Setting
        btn_connect.setOnClickListener(this);
        btn_level1.setOnClickListener(this);
        btn_level2.setOnClickListener(this);
        btn_level3.setOnClickListener(this);
        btn_on.setOnClickListener(this);
        btn_off.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_server.setOnClickListener(this);
        btn_local.setOnClickListener(this);
        btn_view.setOnClickListener(this);


        //Bluetooth Setting
        bluetoothSPP = new BluetoothSPP(this);

        if (!bluetoothSPP.isBluetoothAvailable()) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        }

        bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                if (message.contains("ID")) {
                    textView_device_id.setText(message.substring(2));
                    device_id = textView_device_id.getText().toString();
                    btn_connect.setText("DISCONNECT");
                    btn_view.setVisibility(View.VISIBLE);
                    layout_mode.setVisibility(View.VISIBLE);
                }
                Log.d("receiver ", message);
                vibrator.vibrate(100);

            }
        });

        bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) { //연결되었을 때


            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_SHORT).show();
                btn_connect.setText("CONNECT");
                textView_device_id.setText("");
                device_id = null;
                layout_mode.setVisibility(View.GONE);
                layout_controller.setVisibility(View.GONE);
                layout_level.setVisibility(View.GONE);

            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
                textView_device_id.setText("Fail");
                device_id = null;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothSPP.stopService();
        device_id = null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!bluetoothSPP.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bluetoothSPP.isServiceAvailable()) {
                bluetoothSPP.setupService();
                bluetoothSPP.startService(BluetoothState.DEVICE_OTHER);
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_level_1: {
                LEVEL = 1;
                bluetoothSPP.send("1", true);
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_level_2: {
                bluetoothSPP.send("2", true);
                LEVEL = 2;
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);

                break;
            }
            case R.id.main_btn_level_3: {
                bluetoothSPP.send("3", true);
                LEVEL = 3;
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_connect: {
                if (bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetoothSPP.disconnect(); // Bluetooth disconnect
                    btn_connect.setText("CONNECT");
                    textView_device_id.setText("");
                    layout_mode.setVisibility(View.GONE);
                    layout_controller.setVisibility(View.GONE);
                    layout_level.setVisibility(View.GONE);
                    device_id = null;
                } else {// 블루투스 연결 액티비티 이동
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
                break;
            }
            case R.id.main_btn_on: {
                bluetoothSPP.send("1", true);
                break;
            }
            case R.id.main_btn_stop: {
                bluetoothSPP.send("2", true);
                break;
            }
            case R.id.main_btn_off: {
                bluetoothSPP.send("0", true);
                break;
            }
            case R.id.main_btn_refresh: {
                postureArrayList.clear();
                Call<JsonArray> call = apiInterface.getPostureData(textView_device_id.getText().toString(), 7);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            Log.d("getData", "Success");
                            JsonArray jsonArray = response.body();
                            String today = simpledateformat.format(new Date());
                            Posture posture = new Posture(device_id, today);
                            for (JsonElement e : jsonArray) {
                                if (!today.equals(e.getAsJsonObject().get("date").getAsString())) { // 오늘 날짜가 아니라면
                                    if (posture.all_count == 0)
                                        posture.ratio = 0;
                                    else
                                        posture.ratio = (float) posture.bad_count / posture.all_count;
                                    postureArrayList.add(posture); // list에 추가
                                    posture = new Posture(device_id, e.getAsJsonObject().get("date").getAsString()); // 새로운 객체 생성
                                    today = e.getAsJsonObject().get("date").getAsString();
                                }
                                posture.all_count++;
                                if (e.getAsJsonObject().get("posture").getAsInt() == 1 || e.getAsJsonObject().get("posture").getAsInt() == 2) {
                                    posture.bad_count++;
                                }
                            }
                            if (posture.all_count == 0)
                                posture.ratio = 0;
                            else
                                posture.ratio = (float) posture.bad_count / posture.all_count;
                            postureArrayList.add(posture); // 마지막 객체 삽입

                            if (postureArrayList.get(0).all_count == 0)
                                postureArrayList.remove(0);
                            listView.setAdapter(postureAdapter);


                        } else {
                            postureArrayList.clear();
                            postureAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Sync Fail", Toast.LENGTH_SHORT).show();
                        postureArrayList.clear();
                        postureAdapter.notifyDataSetChanged();
                    }
                });
                listView.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_server: {
                bluetoothSPP.send("1", true);
                layout_mode.setVisibility(View.GONE);
                layout_level.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_local: {
                bluetoothSPP.send("2", true);
                layout_mode.setVisibility(View.GONE);
                layout_level.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_view: {
                if (device_id == null || device_id.equals("") || !isNumeric(device_id)) {
                    Toast.makeText(myActivity, "기기와 연결해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, ViewActivity.class);
                intent.putExtra("device_id", textView_device_id.getText().toString());
                startActivity(intent);
                break;
            }

        }
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
