package com.example.DiyetAsistanim.realm;

import io.realm.RealmObject;

import java.util.Date;

public class dailyWaterTable extends RealmObject {

    private Date date;
    private double dailyWater;

    public dailyWaterTable() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDailyWater() {
        return dailyWater;
    }

    public void setDailyWater(double dailyWater) {
        this.dailyWater = dailyWater;
    }
}
