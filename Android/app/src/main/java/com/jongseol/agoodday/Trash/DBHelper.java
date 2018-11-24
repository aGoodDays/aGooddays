package com.jongseol.agoodday.Trash;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jongseol.agoodday.Model.Device;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_DEVICE = "device";
    private Context context;
    private Device device;
    private ArrayList<Device> deviceList;
    private JsonArray jsonArray;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        jsonArray = new JsonArray();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE + " (" +
                "id INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "device_id TEXT not null, " +
                "posture INTEGER not null, " +
                "saX REAL default 0, " +
                "saY REAL default 0, " +
                "saZ REAL default 0, " +
                "sgX REAL default 0, " +
                "sgY REAL default 0, " +
                "sgZ REAL default 0, " +
                "xdegree REAL default 0, " +
                "ydegree REAL default 0, " +
                "zdegree REAL default 0, " +
                "date DATETIME default CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void insertDevice(Device device) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_DEVICE + "(device_id, posture, saX, saY, saZ, sgX, sgY, sgZ, xdegree, ydegree, zdegree)" +
                " VALUES('" + device.device_id + "', "
                + device.posture + ", "
                + device.saX + ", "
                + device.saY + ", "
                + device.saZ + ", "
                + device.sgX + ", "
                + device.sgY + ", "
                + device.sgZ + ", "
                + device.xdegree + ", "
                + device.ydegree + ", "
                + device.zdegree + ")";
        System.out.print(sql);
        db.execSQL(sql);
        db.close();
    }

    public JsonArray select(String sql) {
        SQLiteDatabase db = getReadableDatabase();

        Gson gson = new Gson();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("device_id", cursor.getString(0));
            jsonObject.addProperty("posture", cursor.getInt(1));
            jsonObject.addProperty("saX", cursor.getFloat(2));
            jsonObject.addProperty("saY", cursor.getFloat(3));
            jsonObject.addProperty("saZ", cursor.getFloat(4));
            jsonObject.addProperty("sgX", cursor.getFloat(5));
            jsonObject.addProperty("sgY", cursor.getFloat(6));
            jsonObject.addProperty("sgZ", cursor.getFloat(7));
            jsonObject.addProperty("xdegree", cursor.getFloat(8));
            jsonObject.addProperty("ydegree", cursor.getFloat(9));
            jsonObject.addProperty("zdegree", cursor.getFloat(10));
            jsonObject.addProperty("date", cursor.getString(11));
            jsonArray.add(jsonObject);
            String json = gson.toJson(jsonObject);
            System.out.println(json);
        }
        return jsonArray;
    }


    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }
}
