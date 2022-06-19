package com.example.DiyetAsistanim.realm;

import io.realm.RealmObject;

import java.util.Date;

public class addMealTable extends RealmObject {

    Date day;
    String mealName;
    double mealAmount;
    double carbonhydrat;
    double protein;
    double fat;
    double calorie;

    public double getMealAmount() {
        return mealAmount;
    }

    public void setMealAmount(double mealAmount) {
        this.mealAmount = mealAmount;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getCarbonhydrat() {
        return carbonhydrat;
    }

    public void setCarbonhydrat(double carbonhydrat) {
        this.carbonhydrat = carbonhydrat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
}
