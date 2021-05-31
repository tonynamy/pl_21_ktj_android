package com.example.facmanager;

public class FacItem {
    String serialNum;
    String progress;

    public FacItem(String serialNum, String progress) {
        this.serialNum = serialNum;
        this.progress = progress;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

}
