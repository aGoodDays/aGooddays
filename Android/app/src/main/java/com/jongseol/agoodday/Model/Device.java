package com.jongseol.agoodday.Model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Device {

    @SerializedName("device_id")
    public String device_id;
    @SerializedName("date")
    public String date;
    @SerializedName("posture")
    public int posture;
    @SerializedName("saX")
    public float saX;
    @SerializedName("saY")
    public float saY;
    @SerializedName("saZ")
    public float saZ;
    @SerializedName("sgX")
    public float sgX;
    @SerializedName("sgY")
    public float sgY;
    @SerializedName("sgZ")
    public float sgZ;
    @SerializedName("xdegree")
    public float xdegree;
    @SerializedName("ydegree")
    public float ydegree;
    @SerializedName("zdegree")
    public float zdegree;


    public Device(String device_id) {
        this.device_id = device_id;
        this.posture = 0;
        this.saX = 0;
        this.saY = 0;
        this.saZ = 0;
        this.sgX = 0;
        this.sgY = 0;
        this.sgZ = 0;
        this.xdegree = 0;
        this.ydegree = 0;
        this.zdegree = 0;
    }

}
