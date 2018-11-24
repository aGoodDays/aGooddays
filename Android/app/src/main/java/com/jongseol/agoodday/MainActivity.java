package com.jongseol.agoodday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.jongseol.agoodday.API.APIClient;
import com.jongseol.agoodday.API.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @file jongseol.agoodday.MainActivity.java
 * @brief Control Device(Raspberry Pi) and Control Data Transmission
 * @author jeje(las9897@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    private static final boolean CONNECT_START = true;
    private static final boolean CONNECT_PAUSE = false;
    private static final int LOW_SENSITIVE_LEVEL = 1;
    private static final int MIDDLE_SENSITIVE_LEVEL = 2;
    private static final int HIGH_SENSITIVE_LEVEL = 3;

    // Data Variable
    private String device_id;
    private boolean connect_state;
    private int sensitive_level;

    // View Variable
    private Button btn_pause, btn_off, btn_level, btn_sync;
    private EditText startdate, enddate;
    private ListView listview;
    private TextView result;

    // Json Transmission Variable
    private APIInterface apiInterface;
    private JsonArray jsonArray;

    /**
     * @brief Processing after receiving device_id from ConnectActivity.java. Show
     *        data to the user.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 100:
            device_id = data.getStringExtra("device_id");
            Call<JsonArray> call = startdate.getText().toString().equals("") || enddate.getText().toString().equals("")
                    ? apiInterface.getJson(device_id)
                    : apiInterface.getJson(device_id, startdate.getText().toString(), enddate.getText().toString());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.isSuccessful()) {
                        jsonArray = response.body();
                        Gson gson = new Gson();
                        result.setText(gson.toJson(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
            break;
        default:
            break;
        }
    }

    /**
     * @brief Setting menu option
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_connect, menu);
        return true;
    }

    /**
     * @brief Selected Item. Start ConnectActivity.java
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.actionbar_connect:
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            startActivityForResult(intent, 0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @brief When the MainActivity.java is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Data;
        device_id = "";
        connect_state = false;
        sensitive_level = 0;

        // Init View
        btn_pause = (Button) findViewById(R.id.main_btn_pause);
        btn_off = (Button) findViewById(R.id.main_btn_off);
        btn_level = (Button) findViewById(R.id.main_btn_level);
        btn_sync = (Button) findViewById(R.id.main_btn_sync);
        startdate = (EditText) findViewById(R.id.main_edittext_startdate);
        enddate = (EditText) findViewById(R.id.main_edittext_enddate);
        listview = (ListView) findViewById(R.id.main_listview);

        //Init Json Transmission
        apiInterface = APIClient.getClient().create(APIInterface.class);
        result = (TextView) findViewById(R.id.main_textview_result);


        /**
         * @brief Button btn_pause click event listener. 
         * @see For Test Listener.
         */
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connect_state) {
                    connect_state = CONNECT_PAUSE;
                    btn_pause.setText("START");

                } else {
                    Call<JsonArray> call = apiInterface.insertJson(jsonArray);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.isSuccessful()) {
                                result.setText("성공!");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            result.setText("실패!");
                        }
                    });
                    connect_state = CONNECT_START;
                    btn_pause.setText("PAUSE");
                }
            }
        });

        /**
         * @brief Button btn_off click event listener. 
         * @see Nothing. but I will control disconnect or power off
         */
        btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disconnect & Device Off
            }
        });

        /**
         * @brief Button btn_level click event listener.
         *        Set Level Data
         */
        btn_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sensitive_level) {
                case LOW_SENSITIVE_LEVEL:
                    btn_level.setText("2");
                    sensitive_level = MIDDLE_SENSITIVE_LEVEL;
                    break;
                case MIDDLE_SENSITIVE_LEVEL:
                    btn_level.setText("3");
                    sensitive_level = HIGH_SENSITIVE_LEVEL;
                    break;
                case HIGH_SENSITIVE_LEVEL:
                    btn_level.setText("1");
                    sensitive_level = LOW_SENSITIVE_LEVEL;
                    break;
                default:
                    btn_level.setText("1");
                    sensitive_level = LOW_SENSITIVE_LEVEL;
                    break;
                }
            }
        });

        /**
         * @brief POST requset to /posture/insert
         */
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonArray> get_call = startdate.getText().toString().equals("")
                        || enddate.getText().toString().equals("") ? apiInterface.getJson(device_id)
                                : apiInterface.getJson(device_id, startdate.getText().toString(),
                                        enddate.getText().toString());
                get_call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            jsonArray = response.body();
                            Call<JsonArray> post_call = apiInterface.insertJson(jsonArray);
                            post_call.enqueue(new Callback<JsonArray>() {
                                @Override
                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                    if (response.isSuccessful()) {
                                        Gson gson = new Gson();
                                        result.setText(gson.toJson(response.body()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    result.setText("실패");
                                }
                            });

                            Gson gson = new Gson();
                            result.setText(gson.toJson(response.body()));
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                    }
                });
            }
        });

        // ListView

    }
}
