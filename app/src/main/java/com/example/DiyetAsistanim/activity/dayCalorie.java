package com.example.DiyetAsistanim.activity;

public class dayCalorie {
    private String dayText;
    private double Calorie;

    public dayCalorie(String dayText, double calorie) {
        this.dayText = dayText;
        Calorie = calorie;
    }

    public dayCalorie() {

    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public double getCalorie() {
        return Calorie;
    }

    public void setCalorie(double calorie) {
        Calorie = calorie;
    }
}
