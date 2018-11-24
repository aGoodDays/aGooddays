package com.jongseol.agoodday.Trash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jongseol.agoodday.Model.Device;
import com.jongseol.agoodday.R;

public class SQLiteActivity extends AppCompatActivity {

    private Button get_button, post_button;
    private TextView result_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        final DBHelper DBHelper = new DBHelper(getApplicationContext(), "agoodday", null, 1);

        get_button = (Button) findViewById(R.id.sqlite_button_get);
        post_button = (Button) findViewById(R.id.sqlite_button_post);
        result_textview = (TextView) findViewById(R.id.sqlite_textview_result);
        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result_textview.setText("");
                DBHelper.select("SELECT * FROM " + DBHelper.TABLE_DEVICE);
                JsonArray jsonArray = DBHelper.getJsonArray();


                Gson gson = new Gson();
                String result = "";
                for(JsonElement jsonElement : jsonArray){
                    result += gson.toJson(jsonElement);
                }
                result_textview.setText(result);
            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device  = new Device("20181116");
                DBHelper.insertDevice(device);
            }
        });

    }
}
