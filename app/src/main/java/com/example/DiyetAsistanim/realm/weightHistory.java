package com.example.DiyetAsistanim.realm;

import io.realm.RealmObject;

import java.util.Date;

public class weightHistory extends RealmObject {

    private Date date;
    private double weight;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
