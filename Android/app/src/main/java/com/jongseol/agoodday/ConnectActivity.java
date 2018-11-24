package com.jongseol.agoodday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @file jongseol.agoodday.ConnectActivity.java
 * @brief bluetooth connect and get device_id
 * @author jeje(las9897@gmail.com)
 * @see A Bluetooth connection module is required
 */
public class ConnectActivity extends AppCompatActivity {

    //Constant Variable
    private static final String TAG = "ConnetActivity";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    //View Variable
    private EditText device_id;
    private Button btn_commit, btn_bluetooth;
    private TextView result;


    /**
     * @brief Activity Setting and Init Variable
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Log.e(TAG, "onCreate");
        device_id = (EditText) findViewById(R.id.connect_edittext_device_id);
        btn_commit = (Button) findViewById(R.id.connect_btn_commit);
        btn_bluetooth = (Button) findViewById(R.id.connect_btn_bluetooth);


        /**
         * @brief Button btn_commit click event listener. send device_id to MainActivity.java
         */
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
