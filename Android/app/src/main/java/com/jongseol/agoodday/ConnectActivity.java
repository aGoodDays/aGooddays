package com.jongseol.agoodday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothService2";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private EditText device_id;
    private Button btn_commit, btn_bluetooth;
    private TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Log.e(TAG, "onCreate");
        device_id = (EditText) findViewById(R.id.connect_edittext_device_id);
        btn_commit = (Button) findViewById(R.id.connect_btn_commit);
        btn_bluetooth = (Button) findViewById(R.id.connect_btn_bluetooth);


        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("device_id", device_id.getText().toString());
                setResult(100, intent);
                finish();
            }
        });


    }

}
