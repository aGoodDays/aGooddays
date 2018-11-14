package com.jongseol.agoodday;

import java.util.Date;

public class Device {

    private String device_id;
    private int posture;
    private float saX;
    private float saY;
    private float saZ;
    private float sgX;
    private float sgY;
    private float sgZ;
    private float xdegree;
    private float ydegree;
    private float zdegree;
    private Date date;

    public Device() {
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getPosture() {
        return posture;
    }

    public void setPosture(int posture) {
        this.posture = posture;
    }

    public float getSaX() {
        return saX;
    }

    public void setSaX(float saX) {
        this.saX = saX;
    }

    public float getSaY() {
        return saY;
    }

    public void setSaY(float saY) {
        this.saY = saY;
    }

    public float getSaZ() {
        return saZ;
    }

    public void setSaZ(float saZ) {
        this.saZ = saZ;
    }

    public float getSgX() {
        return sgX;
    }

    public void setSgX(float sgX) {
        this.sgX = sgX;
    }

    public float getSgY() {
        return sgY;
    }

    public void setSgY(float sgY) {
        this.sgY = sgY;
    }

    public float getSgZ() {
        return sgZ;
    }

    public void setSgZ(float sgZ) {
        this.sgZ = sgZ;
    }

    public float getXdegree() {
        return xdegree;
    }

    public void setXdegree(float xdegree) {
        this.xdegree = xdegree;
    }

    public float getYdegree() {
        return ydegree;
    }

    public void setYdegree(float ydegree) {
        this.ydegree = ydegree;
    }

    public float getZdegree() {
        return zdegree;
    }

    public void setZdegree(float zdegree) {
        this.zdegree = zdegree;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
