package com.jongseol.agoodday.Model;

import com.google.gson.annotations.SerializedName;

/**
 * @file jongseol.agoodday.Model.Posture
 * @brief 그래프와 리스트뷰에 사용될 자세 데이터 모델입니다.
 * @author jeje (las9897@gmail.com)
 */
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


    /**
     * @brief Constructor.
     * @param device_id
     * @param date
     * @author jeje (las9897@gmail.com)
     */
    public Posture(String device_id, String date) {
        this.device_id = device_id;
        this.date = date;
        this.all_count = 0;
        this.bad_count = 0;
        this.ratio = 0;
    }
}
