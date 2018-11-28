package com.jongseol.agoodday.Model;

import com.google.gson.annotations.SerializedName;

public class Analysis {

    @SerializedName ("device_id")
    public String device_id;


    @SerializedName("count")
    public int count;

    @SerializedName("start_date")
    public String start_date;

    @SerializedName("end_date")
    public String end_date;
}
