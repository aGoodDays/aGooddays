package com.jongseol.agoodday.Model;

import com.google.gson.annotations.SerializedName;

public class Posture {

    @SerializedName ( "device_id" )
    public String device_id;

    @SerializedName ( "date" )
    public String date;

    @SerializedName ( "all_count" )
    public int all_count;

    @SerializedName ( "bad_count" )
    public int bad_count;

    @SerializedName ( "ratio" )
    public float ratio;

    public Posture(String device_id, String date) {
        this.device_id = device_id;
        this.date = date;
        this.all_count = 0;
        this.bad_count = 0;
        this.ratio = 0;
    }


}
