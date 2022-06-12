package com.example.kalori.activity;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class myAxisValueFormat extends ValueFormatter {
    private List<String> mdays;
    public myAxisValueFormat(List<String> myDay) {
        this.mdays= myDay;
    }

    @Override
    public String getFormattedValue(float value) {
        return mdays.get((int) value);
    }
}
