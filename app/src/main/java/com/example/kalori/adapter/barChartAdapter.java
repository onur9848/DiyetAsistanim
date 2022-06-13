package com.example.kalori.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.AttachedSurfaceControl;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;
import com.example.kalori.activity.dayCalorie;
import com.example.kalori.activity.myAxisValueFormat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class barChartAdapter extends RecyclerView.Adapter<barChartAdapter.ViewHolder> {

    private List<dayCalorie> dayCalorieList;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;

    public barChartAdapter(Context context, List<dayCalorie> dayCalorieList, ViewPager2 viewPager2) {
        this.dayCalorieList = dayCalorieList;
        this.mInflater = LayoutInflater.from(context);
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.calorie_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        for (int i = 0; i < dayCalorieList.size(); i++) {
            holder.barEntriesArrayList.add(new BarEntry(i, (float) dayCalorieList.get(i).getCalorie()));
        }
        holder.barDataSet = new BarDataSet(holder.barEntriesArrayList,"myLAber");
        holder.barData = new BarData(holder.barDataSet);
        holder.barChart.setData(holder.barData);
        holder.barDataSet.setColor(Color.RED);
        holder.barDataSet.setValueTextColor(Color.RED);

        holder.barDataSet.setValueTextSize(16f);
        holder.barChart.getDescription().setEnabled(false);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        BarChart barChart;
        BarDataSet barDataSet;
        BarData barData;
        ArrayList barEntriesArrayList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            barChart = itemView.findViewById(R.id.totalCalorieBarChart);
            barEntriesArrayList = new ArrayList<>();


        }
    }


}
