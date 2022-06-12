package com.example.kalori.realm;

import io.realm.RealmObject;

import java.util.Date;

public class dailyMacroDetailTable extends RealmObject {

    Date date;
    double DbTotalProtein;
    double DbTotalFat;
    double DbTotalCarbonhydrat;
    double DbTotalCalorie;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDbTotalProtein() {
        return DbTotalProtein;
    }

    public void setDbTotalProtein(double dbTotalProtein) {
        DbTotalProtein = dbTotalProtein;
    }

    public double getDbTotalFat() {
        return DbTotalFat;
    }

    public void setDbTotalFat(double dbTotalFat) {
        DbTotalFat = dbTotalFat;
    }

    public double getDbTotalCarbonhydrat() {
        return DbTotalCarbonhydrat;
    }

    public void setDbTotalCarbonhydrat(double dbTotalCarbonhydrat) {
        DbTotalCarbonhydrat = dbTotalCarbonhydrat;
    }

    public double getDbTotalCalorie() {
        return DbTotalCalorie;
    }

    public void setDbTotalCalorie(double dbTotalCalorie) {
        DbTotalCalorie = dbTotalCalorie;
    }
}
