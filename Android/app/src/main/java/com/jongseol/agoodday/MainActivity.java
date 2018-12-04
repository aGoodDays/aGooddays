package com.jongseol.agoodday;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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


/**
 * @file jongseol.agoodday.MainActivitiy.java
 * @brief 블루투스 연결, 디바이스 제어를 위한 액티비티. 오늘 날짜를 기준으로 3일 간의 자세 데이터를 보여주는 리스트도 포함되어있다.
 * @author jeje (las9897@gmail.com)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static MainActivity myActivity;
    //Bluetooth
    public BluetoothSPP bluetoothSPP;

    //Vibrator
    public static Vibrator vibrator;

    //Time
    public static final TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");
    public static final SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");

    //View
    private LinearLayout layout_bluetooth, layout_level, layout_controller, layout_mode;
    private Button btn_connect, btn_level1, btn_level2, btn_level3, btn_on, btn_stop, btn_off, btn_refresh, btn_server, btn_local, btn_view;
    private TextView textView_device_id;
    private ListView listView;

    private String device_id;

    private APIInterface apiInterface;
    private ArrayList<Posture> postureArrayList;
    private PostureAdapter postureAdapter;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /**
         * @brief 다른 액티비티에서 결과를 받아서 처리하는 부분입니다.
         * @param requestCode
         * @param resultCode
         * @param data
         */
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            /** * @brief 블루투스 리스트에서 받아온 name과 address를 가지고 커넥트를 시도합니다. */
            if (resultCode == Activity.RESULT_OK) {
                bluetoothSPP.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            /** * @brief 생명주기에서 onStart()를 실행할 때 블루투스 상태를 확인하고 그 결과를 바탕으로 수행하는 부분입니다. */
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
        /** * @brief 액티비티가 처음으로 실행되었을 때 작동하는 메소드입니다. 기본적으로 진동, 블루투스, View 등에 대해서 초기 설정을 해줍니다. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Base Setting
        myActivity = this;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        simpledateformat.setTimeZone(timezone);

        //VIew
        //bluetooth layout
        layout_bluetooth = (LinearLayout) findViewById(R.id.main_layout_bluetooth);
        btn_connect = (Button) findViewById(R.id.main_btn_connect);
        btn_view = (Button) findViewById(R.id.main_btn_view);
        textView_device_id = (TextView) findViewById(R.id.main_textview_device_id);
        //mode layout
        layout_mode = (LinearLayout) findViewById(R.id.main_layout_mode);
        btn_server = (Button) findViewById(R.id.main_btn_server);
        btn_local = (Button) findViewById(R.id.main_btn_local);
        //level layout
        layout_level = (LinearLayout) findViewById(R.id.main_layout_level);
        btn_level1 = (Button) findViewById(R.id.main_btn_level_1);
        btn_level2 = (Button) findViewById(R.id.main_btn_level_2);
        btn_level3 = (Button) findViewById(R.id.main_btn_level_3);
        //controller layout
        layout_controller = (LinearLayout) findViewById(R.id.main_layout_controller);
        btn_on = (Button) findViewById(R.id.main_btn_on);
        btn_stop = (Button) findViewById(R.id.main_btn_stop);
        btn_off = (Button) findViewById(R.id.main_btn_off);
        btn_refresh = (Button) findViewById(R.id.main_btn_refresh);
        //view layout
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


        /** * @brief bluetoothSSP를 이용해서 블루투스를 세팅하는 부분입니다. */
        bluetoothSPP = new BluetoothSPP(this);

        if (!bluetoothSPP.isBluetoothAvailable()) {
            /** @brief 블루투스를 지원하지 않는 기기일 때 실행하는 메소드입니다. 모든 레이아웃을 숨겨주는 방향으로 하고있습니다.
             *         블루투스 지원을 한다면 블루투스 관련된 초기 설정을 합니다. */
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            layout_bluetooth.setVisibility(View.GONE);
        } else {
            bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
                /**
                 * @brief 디바이스 측에서 보내는 메시지를 수신하는 부분입니다. 디바이스는 연결되자마자 ID를 보내게끔 설계되어있기 때문에, ID 값을 받아와서 textView에 세팅하게 만들었습니다.
                 *         그 후에는 Connect 버튼을 바꿔주고 모드를 고를 수 있게 하고, 데이터 리스트의 View를 VISIBLE로 세팅합니다.
                 *         또한 디바이스는 ID를 보낸 후에 신호를 지속적으로 발신하는데, 디바이스는 자세가 잘못되었을 때만 발신하기 때문에 ID가 아닌 신호에는 진동이 울리게끔 했습니다.
                 * @param data
                 * @param message
                 */
                @Override
                public void onDataReceived(byte[] data, String message) {
                    if (message.contains("ID")) {
                        textView_device_id.setText(message.substring(2));
                        device_id = textView_device_id.getText().toString();

                    }
                    vibrator.vibrate(100);
                }
            });

            /** @brief 블루투스 연결을 시도하고 난 후의 결과를 바탕으로 연결, 실패, 연결끊김될 때 실행되는 메소드들로 UI를 컨트롤합니다. */
            bluetoothSPP.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
                @Override
                public void onDeviceConnected(String name, String address) { //연결되었을 때
                    Toast.makeText(getApplicationContext(), "Connect", Toast.LENGTH_SHORT).show();
                    btn_connect.setText("DISCONNECT");
                    btn_view.setVisibility(View.VISIBLE);
                    layout_mode.setVisibility(View.VISIBLE);
                    textView_device_id.setText("ID를 불러오는 중입니다.");
                }

                @Override
                public void onDeviceConnectionFailed() {
                    Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
                    textView_device_id.setText("Fail");
                    device_id = null;
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


            });
        }
    }

    @Override
    protected void onDestroy() {
        /** @brief 액티비티가 종료되었을 때 실행되는 메소드입니다. 블루투스 서비스를 종료합니다. */
        super.onDestroy();
        bluetoothSPP.stopService();
    }

    @Override
    protected void onStart() {
        /** @brief onCreate가 실행된 후 바로 실행되는 메소드입니다. 어플을 잠시 백그라운드로 내렸다가 다시 올릴 떄도 실행됩니다.
         *          주로 블루투스가 꺼져있는걸 확인하거나 서비스를 활성화합니다. */
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
        /** @brief 액티비티 UI를 보면 버튼이 상당히 많습니다. 관리를 편하게 하기 위해서, 인터페이스 View.OnClickListener를 implement하여 onClick를 구현하여 UI의 ID를 통해 관리합니다. */
        switch (v.getId()) {
            case R.id.main_btn_connect: {
                /** @biref Bluetooth Layout의 Connect 버튼입니다. 이 버튼은 Disconnect의 역할도 할 수 있게 구현하였기 때문에, 현재 블루투스 연결 상태로 판단하여 다른 기능을 수행합니다.
                 *         블루투스가 연결되지 않았을 때는 디바이스를 검색하고 고를 수 있는 액티비티로 이동합니다. 연결되어있다면 연결을 해제하고 처음 상태로 돌아옵니다. */
                if (bluetoothSPP.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetoothSPP.disconnect(); // Bluetooth disconnect
                    btn_connect.setText("CONNECT");
                    textView_device_id.setText("");
                    layout_mode.setVisibility(View.GONE);
                    layout_controller.setVisibility(View.GONE);
                    layout_level.setVisibility(View.GONE);
                    device_id = null;
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
                break;
            }
            case R.id.main_btn_view: {
                /** @brief 자세 데이터를 시각화해서 보여주는 액티비티를 호출하는 버튼입니다. 디바이스와 연결되지 않았을 때는 아무런 기능을 수행하지 않도록 했습니다. */
                if (device_id == null || device_id.equals("") || !isNumeric(device_id)) {
                    Toast.makeText(myActivity, "기기와 연결해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent = new Intent(this, ViewActivity.class);
                intent.putExtra("device_id", textView_device_id.getText().toString());
                startActivity(intent);
                break;
            }

            /**
             * @brief 디바이스를 어떤 모드로 실행시킬지 선택하는 버튼들입니다.
             *         서버 모드로 실행하면 데이터를 서버에 지속적으로 전송합니다.
             *         로컬 모드는 데이터는 전송하지 않고, 잘못된 자세일 경우에 안드로이드로 신호를 보내기만 합니다.
             */
            case R.id.main_btn_server: {
                bluetoothSPP.send("1", true);
                layout_mode.setVisibility(View.GONE);
                layout_level.setVisibility(View.VISIBLE);
                btn_refresh.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_local: {
                bluetoothSPP.send("2", true);
                layout_mode.setVisibility(View.GONE);
                layout_level.setVisibility(View.VISIBLE);
                btn_refresh.setVisibility(View.GONE);
                break;
            }
            /** @brief 디바이스의 민감도를 설정하는 버튼들입니다. 1은 10도, 2는 20도, 3은 30도로 설정해놨습니다. */
            case R.id.main_btn_level_1: {
                bluetoothSPP.send("1", true);
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_level_2: {
                bluetoothSPP.send("2", true);
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.main_btn_level_3: {
                bluetoothSPP.send("3", true);
                layout_level.setVisibility(View.GONE);
                layout_controller.setVisibility(View.VISIBLE);
                break;
            }
            /**
             * @brief 디바이스를 제어하는 부분입니다. on은 디바이스를 켜는 것이 아니라, STOP 상태일시 자세를 다시 측정하라는 신호를 보냅니다.
             *         STOP은 측정을 멈추는 신호를 보내며, OFF는 디바이스를 꺼주는 신호를 보냅니다.
             */
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
            /**
             * @brief 사용자에게 자세가 얼마나 나빠졌는지 보여주는 리스트를 다시 호출하는 버튼입니다.
             *         서버 모드일 경우 데이터는 계속해서 저장되기 때문에 새로고침의 역할을 합니다. 로컬 모드일 경우 가려집니다.
             *         이 부분에서 서버와 통신을 하며 Retrofit을 이용했습니다. 결과를 JsonArray 타입의 CallBack으로 받아와서 필요한 데이터를 추출 및 가공해서 리스트뷰에 뿌려줍니다.
             */
            case R.id.main_btn_refresh: {
                postureArrayList.clear();
                Call<JsonArray> call = apiInterface.getPostureData(textView_device_id.getText().toString(), 7);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            JsonArray jsonArray = response.body();
                            String today = simpledateformat.format(new Date());
                            Posture posture = new Posture(device_id, today);
                            for (JsonElement e : jsonArray) {
                                if (!today.equals(e.getAsJsonObject().get("date").getAsString())) {
                                    if (posture.all_count == 0)
                                        posture.ratio = 0;
                                    else
                                        posture.ratio = (float) posture.bad_count / posture.all_count;
                                    postureArrayList.add(posture);
                                    posture = new Posture(device_id, e.getAsJsonObject().get("date").getAsString());
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


        }
    }

    /**
     * @brief param이 숫자로 구성된 문자열인지 판단하는 메소드입니다.
     * @param str
     * @return boolean
     */
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
