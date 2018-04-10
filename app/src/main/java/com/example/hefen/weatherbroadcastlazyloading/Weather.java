package com.example.hefen.weatherbroadcastlazyloading;

public class Weather {
    private String code;
    private String date;
    private String day;
    private String heigh;
    private String low;
    private String text;

    public Weather(String code, String date, String day, String heigh, String low, String text) {
        this.code = code;
        this.date = date;
        this.day = day;
        this.heigh = heigh;
        this.low = low;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHeigh() {
        return heigh;
    }

    public void setHeigh(String heigh) {
        this.heigh = heigh;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
