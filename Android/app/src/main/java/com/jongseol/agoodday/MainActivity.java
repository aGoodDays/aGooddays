package com.jongseol.agoodday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView device_id, saX, saY, date, result;
    private RestService restService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.main_button);
        device_id = (TextView) findViewById(R.id.main_textview_device_id);
        saX = (TextView) findViewById(R.id.main_textview_saX);
        saY = (TextView) findViewById(R.id.main_textview_saY);
        date = (TextView) findViewById(R.id.main_textview_date);
        result = (TextView) findViewById(R.id.main_textview_result);


        AppController appController = AppController.getInstance();
        appController.buildRestService("localhost", 8000);
        restService = AppController.getInstance().getRestService();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<Device>> listCall = restService.getDevice("20181113");
                listCall.enqueue(new Callback<List<Device>>() {
                    @Override
                    public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                        if(response.isSuccessful()){
                            List<Device> deviceList = response.body();
                            String show_text = "";
                            for(Device device:deviceList){
                                show_text += "ID: " + device.getDevice_id() +"\tData: " + device.getDate() +"\n";
                            }

                            result.setText(show_text);
                        }
                        else{
                            int statusCode = response.code();
                            Log.i("Device_Tag", "response code: "+statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Device>> call, Throwable t) {

                    }
                });

            }
        });


    }
}
