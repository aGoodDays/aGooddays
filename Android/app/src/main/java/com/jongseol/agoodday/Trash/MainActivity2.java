package com.jongseol.agoodday.Trash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jongseol.agoodday.API.APIClient;
import com.jongseol.agoodday.API.APIInterface;
import com.jongseol.agoodday.Adapter.PostureListAdapter;
import com.jongseol.agoodday.Model.Device;
import com.jongseol.agoodday.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity2 extends AppCompatActivity {

    private Button button_get, button_post, button_sqlite;
    private EditText device_id, start_date, end_date;
    private ListView listView;
    private APIInterface apiInterface;
    private PostureListAdapter adapter;
    private List<Device> deviceList;
    private ScrollView scrollView;

    private TextView jsonresult;
    private JsonArray jsonArray;
    private JsonObject jsonObject;
    private JsonElement jsonElement;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        device_id = (EditText) findViewById(R.id.main2_edittext_id);
        start_date = (EditText) findViewById(R.id.main2_edittext_startdate);
        end_date = (EditText) findViewById(R.id.main2_edittext_enddate);
        button_get = (Button) findViewById(R.id.main2_button_get);
        button_post = (Button) findViewById(R.id.main2_button_post);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        scrollView = (ScrollView) findViewById(R.id.main2_scrollview);
        listView = (ListView) findViewById(R.id.main2_listview);
        jsonresult = (TextView) findViewById(R.id.main2_textview_json);
        button_sqlite = (Button) findViewById(R.id.main2_button_sqlite);

        button_sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, SQLiteActivity.class);
                startActivity(intent);
            }
        });
        //Scroll
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

       /* button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<Device>> call = start_date.getText().toString().equals("") || end_date.getText().toString().equals("")
                        ? apiInterface.getDevice(device_id.getText().toString())
                        : apiInterface.getDevice(device_id.getText().toString(), start_date.getText().toString(), end_date.getText().toString());

                call.enqueue(new Callback<List<Device>>() {
                                 @Override
                                 public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                                     if (response.isSuccessful()) {
                                         deviceList = response.body();
                                         adapter = new PostureListAdapter(MainActivity.this, R.layout.posture_item, deviceList);
                                         listView.setAdapter(adapter);
                                         setListViewHeightBasedOnChildren(listView);
                                     } else {
                                         //호출 실패
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<List<Device>> call, Throwable t) {
                                 }
                             }
                );
            }
        });*/
        button_get.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              Call<JsonArray> call = apiInterface.getJson(device_id.getText().toString());
                                              call.enqueue(new Callback<JsonArray>() {
                                                  @Override
                                                  public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                                      // temp = response.body().toString().replace("[", "{").replace("]", "}");
                                                      jsonArray = response.body();
                                                  }

                                                  @Override
                                                  public void onFailure(Call<JsonArray> call, Throwable t) {

                                                  }
                                              });


                                          }
                                      }
        );

        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonArray> call = apiInterface.insertJson(jsonArray);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
            }
        });
    }




                /*
                ArrayList<Device> deviceList = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    deviceList.add(new Device(device_id.getText().toString(), "2018-11-15"));
                }
                //json화가 필요함!

                Call<List<Device>> call = apiInterface.insertJson(deviceList);
                call.enqueue(new Callback<List<Device>>() {
                    @Override
                    public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<Device>> call, Throwable t) {

                    }
                });*/


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //List가 생설될 때 스크롤링을 버리고 밑으로 다 나오게 함
        PostureListAdapter adapter = (PostureListAdapter) listView.getAdapter();
        if (adapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
