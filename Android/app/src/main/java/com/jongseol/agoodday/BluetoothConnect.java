package com.jongseol.agoodday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import app.akexorcist.bluetotohspp.library.BluetoothState;

public class BluetoothConnect extends AppCompatActivity implements View.OnClickListener {

    public static final int BLUETOOTH_CONNECT_CODE = 201;
    public static final int BLUETOOTH_FAIL_CODE = 401;

    private static final int SSP_REQUEST_CODE = 1;
    private static final int THREAD_REQUEST_CODE = 2;



    private Button btn_ssp, btn_send, btn_test;
    private String device_id;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case SSP_REQUEST_CODE:{
                if(resultCode == BluetoothState.REQUEST_CONNECT_DEVICE){

                }
                break;
            }
            case THREAD_REQUEST_CODE:{
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bluetooth_connect_btn_test: {
                intent = getIntent();
                if (true) { // Bluetooth Connect
                    device_id = "20181113";
                    intent.putExtra("device_id", device_id);
                    setResult(BLUETOOTH_CONNECT_CODE, intent);
                    finish();
                } else { //Fail
                    setResult(BLUETOOTH_FAIL_CODE, intent);
                    finish();
                }
                break;
            }
            case R.id.bluetooth_connect_btn_spp: {
                intent = new Intent(getApplicationContext(), BluetoothSPPActivity.class);
                startActivityForResult(intent, SSP_REQUEST_CODE);
                break;
            }
            case R.id.bluetooth_connect_btn_send: {
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        //Init
        btn_ssp = (Button) findViewById(R.id.bluetooth_connect_btn_spp);
        btn_send = (Button) findViewById(R.id.bluetooth_connect_btn_send);
        btn_test = (Button) findViewById(R.id.bluetooth_connect_btn_test);

        btn_ssp.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_test.setOnClickListener(this);
    }
}
