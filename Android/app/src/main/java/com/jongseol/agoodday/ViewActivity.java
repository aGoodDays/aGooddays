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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_week, btn_2week, btn_4week, btn_search;
    private MaskedEditText mask_start_date, mask_end_date;
    private ListView listView;
    private LineChart lineChart;

    public static final TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");
    public static final SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
    private APIInterface apiInterface;
    private ArrayList<Posture> postureArrayList;
    private PostureAdapter postureAdapter;

    private String device_id;


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
        btn_week.performClick();


    }


    public void getData(int date) {
        postureArrayList.clear();
        Call<JsonArray> call = apiInterface.getDevice(device_id, date);
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

    public void getData(String start_date, String end_date) {
        postureArrayList.clear();
        Call<JsonArray> call = apiInterface.getDevice(device_id, start_date, end_date);
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

    public void makeGraph() {
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        for (int i = postureArrayList.size() - 1; i >= 0; i--) {
            entries.add(new Entry(postureArrayList.size() - i - 1, postureArrayList.get(i).ratio * 100));
            labels.add(postureArrayList.get(i).date);
        }


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
        Marker marker = new Marker(this, R.layout.item_marker);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

    }

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_btn_week: {
                getData(7);
                buttonDeley(R.id.view_btn_week);
                break;
            }
            case R.id.view_btn_2week: {
                getData(14);
                buttonDeley(R.id.view_btn_2week);
                break;
            }
            case R.id.view_btn_4week: {
                getData(28);
                buttonDeley(R.id.view_btn_4week);
                break;
            }
            case R.id.view_btn_search: {
                getData(mask_start_date.getText().toString(), mask_end_date.getText().toString());
                buttonDeley(R.id.view_btn_search);
                break;
            }

        }
    }

    private class SplashHandler implements Runnable {

        Button btn;

        public SplashHandler(int btn_id) {
            this.btn = (Button) findViewById(btn_id);
        }

        @Override
        public void run() {
            btn.setEnabled(true);
        }
    }

    public void buttonDeley(int btn_id) {
        Button btn = (Button) findViewById(btn_id);
        btn.setEnabled(false);
        Handler handler = new Handler();
        handler.postDelayed(new SplashHandler(btn_id), 5000);
    }
}


