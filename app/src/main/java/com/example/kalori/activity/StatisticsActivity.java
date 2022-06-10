package com.example.kalori.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;
import com.example.kalori.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    ViewPager2 macroDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        tanimla();
        listele();
    }

    private void listele() {
        List<String> list = new ArrayList<>();
        list.add("protein");
        list.add("karbon");
        list.add("yag");
        macroDetail.setAdapter(new ViewPagerAdapter(this,list,macroDetail));
    }

    private void tanimla() {
        macroDetail =(ViewPager2) findViewById(R.id.staticticsmakrodetails);
    }
}