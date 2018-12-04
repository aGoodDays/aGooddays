package com.jongseol.agoodday;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jongseol.agoodday.API.APIClient;
import com.jongseol.agoodday.API.APIInterface;
import com.jongseol.agoodday.Adapter.PostureAdapter;
import com.jongseol.agoodday.Model.Marker;
import com.jongseol.agoodday.Model.Posture;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @file jongseol.agoodday.ViewActivity.java
 * @brief MySQL에 저장되어있는 자세 데이터를 가공해서 보여주는 액티비티 입니다.
 * @author jeje (las9897@gmail.com)
 */
public class ViewActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_week, btn_2week, btn_4week, btn_search;
    private MaskedEditText mask_start_date, mask_end_date;
    private ListView listView;
    private LineChart lineChart;

    private APIInterface apiInterface;
    private ArrayList<Posture> postureArrayList;

    private PostureAdapter postureAdapter;
    private String device_id;


    /**
     * @brief ViewActivity가 처음 실행되었을 때 수행되는 메소드입니다. 전달된 Intent로 id값을 받아오거나, 기본적인 View와 API들을 세팅합니다.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");
        mask_start_date = (MaskedEditText) findViewById(R.id.view_mask_start_date);
        mask_end_date = (MaskedEditText) findViewById(R.id.view_mask_end_date);
        btn_week = (Button) findViewById(R.id.view_btn_week);
        btn_2week = (Button) findViewById(R.id.view_btn_2week);
        btn_4week = (Button) findViewById(R.id.view_btn_4week);
        btn_search = (Button) findViewById(R.id.view_btn_search);
        listView = (ListView) findViewById(R.id.view_listview);
        lineChart = (LineChart) findViewById(R.id.view_linechart);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        postureArrayList = new ArrayList<>();
        postureAdapter = new PostureAdapter(postureArrayList);


        btn_week.setOnClickListener(this);
        btn_2week.setOnClickListener(this);
        btn_4week.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        /** @brief ViewActivity를 실행했을 때 일주일 치의 데이터를 보여주기 위해서 추가했습니다. */
        btn_week.performClick();


        /** @brief ViewActivity에서도 진동을 감지할 수 있도록 만들었습니다. */
        MainActivity.myActivity.bluetoothSPP.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {

                Log.d("receiver ", message);
                MainActivity.myActivity.vibrator.vibrate(100);

            }
        });

    }

    /**
     * @brief 많은 버튼을 편하게 관리하기 위해서, 해당하는 View의 id 값을 통해서 관리합니다.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** @brief 1주일, 2주일, 한달(4주일)의 데이터를 가져옵니다. Delay 메소드를 사용하여 지속적인 버튼 클릭을 방지합니다. */
            case R.id.view_btn_week: {
                getData(7);
                Delay();
                break;
            }
            case R.id.view_btn_2week: {
                getData(14);
                Delay();
                break;
            }
            case R.id.view_btn_4week: {
                getData(28);
                Delay();
                break;
            }
            /** @brief 지정한 날짜의 데이터를 가져옵니다. 날짜가 잘못될 경우 Toast를 날려 사용자에게 수정을 유도합니다. Delay 메소드를 사용하여 지속적인 버튼 클릭을 방지합니다. */
            case R.id.view_btn_search: {
                if (mask_start_date.getText().toString().contains(" ") || mask_end_date.getText().toString().contains(" ")) {
                    Toast.makeText(this, "올바른 형식으로 입력해주세요. \n예) YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                } else {
                    getData(mask_start_date.getText().toString(), mask_end_date.getText().toString());
                    Delay();
                }
                break;
            }

        }
    }

    /**
     * @brief 서버와 통신해서 JsonArray 타입의 데이터를 가져와 그래프와 리스트뷰에 뿌려주는 메소드입니다.
     *         매개변수를 int 타입으로 줘서 오늘을 기준으로 며칠 동안의 데이터를 보여줄 것인지 조절할 수 있습니다.
     * @param date
     */
    public void getData(int date) {
        postureArrayList.clear();
        Call<JsonArray> call = apiInterface.getPostureData(device_id, date);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.d("getData", "Success");
                    JsonArray jsonArray = response.body();
                    String today = MainActivity.simpledateformat.format(new Date());
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
                    makeGraph();
                    setListViewHeightBasedOnChildren(listView);
                } else {
                    Log.d("getData", "Success but Fail");
                    postureArrayList.clear();
                    lineChart.clear();
                    postureAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("getData", "Fail");
                postureArrayList.clear();
                lineChart.clear();
                postureAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * @brief 위의 getData와 같은 기능을 수행하지만, 시작 날짜와 끝 날짜를 지정해서 그 사이에 있는 데이터를 보여주는 메소드입니다.
     * @param start_date
     * @param end_date
     */
    public void getData(String start_date, String end_date) {
        postureArrayList.clear();
        Call<JsonArray> call = apiInterface.getPostureData(device_id, start_date, end_date);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.d("getData", "Success");
                    JsonArray jsonArray = response.body();
                    String today = MainActivity.simpledateformat.format(new Date());
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
                    makeGraph();
                    setListViewHeightBasedOnChildren(listView);
                } else {
                    Log.d("getData", "Success but Fail");
                    postureArrayList.clear();
                    lineChart.clear();
                    postureAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("getData", "Fail");
                postureArrayList.clear();
                lineChart.clear();
                postureAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * @brief getData 메소드에서 호출하는 그래프를 그려주는 메소드입니다. 그래프를 그릴 때는 MPAndroidChart를 사용하였습니다.
     *         그래프에 사용되는 데이터는 리스트뷰에 뿌리는 데이터 리스트를 사용하였습니다.
     */
    public void makeGraph() {
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        for (int i = postureArrayList.size() - 1; i >= 0; i--) {
            entries.add(new Entry(postureArrayList.size() - i - 1, postureArrayList.get(i).ratio * 100));
            labels.add(postureArrayList.get(i).date);
        }

        //Graph Setting
        LineDataSet dataSet = new LineDataSet(entries, "Posture Data");
        dataSet.setColor(Color.parseColor("#008577"));//color
        dataSet.setLineWidth(2);
        dataSet.setCircleRadius(6);
        dataSet.setCircleHoleRadius(3);
        dataSet.setCircleHoleColor(Color.parseColor("#008577"));
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawValues(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });

        Description description = new Description();
        description.setText("");
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDescription(description);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh

        //Marker Setting
        Marker marker = new Marker(this, R.layout.item_marker);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

    }

    /**
     * @brief ViewActivity는 ScrollVIew라서 스크롤할 때 ListVIew와 충돌이 일어납니다.
     *         그래서 ListView의 스크롤을 없애고, 생성되는 데이터들의 크기를 계산해서 ListView의 height 값을 조절하는 방법을 사용했습니다.
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    /**
     * @brief 서버와 통신하는 버튼들을 계속 누르다보면 데이터가 꼬이는 경우가 생겼습니다.
     *         이를 방지하고자 버튼을 눌렀을 때 3초 동안의 딜레이를 만들어서 다른 버튼을 누르지 못하게 했습니다.
     */
    public void Delay() {
        btn_week.setEnabled(false);
        btn_search.setEnabled(false);
        btn_2week.setEnabled(false);
        btn_4week.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new PostureHandler(), 3000);
    }

    /** @brief Delay에 사용될 핸들러입니다. */
    private class PostureHandler implements Runnable {
        @Override
        public void run() {
            btn_week.setEnabled(true);
            btn_search.setEnabled(true);
            btn_2week.setEnabled(true);
            btn_4week.setEnabled(true);
        }
    }
}

