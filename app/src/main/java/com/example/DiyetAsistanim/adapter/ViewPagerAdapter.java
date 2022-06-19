package com.example.DiyetAsistanim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.DiyetAsistanim.R;
import com.example.DiyetAsistanim.realm.dailyMacroDetailTable;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private List<dailyMacroDetailTable> mData;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    private SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ViewPagerAdapter(Context context, List<dailyMacroDetailTable> mData, ViewPager2 viewPager2) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.makrodetails, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double protein = mData.get(position).getDbTotalProtein();
        double carbonhydrat = mData.get(position).getDbTotalCarbonhydrat();
        double fat = mData.get(position).getDbTotalFat();
        double calorie = mData.get(position).getDbTotalCalorie();
        double toplam = protein+carbonhydrat+fat;

        Date date = mData.get(position).getDate();
        holder.staticsprotein.setText("Protein: %"+yuzdeHesap(toplam,protein));
        holder.staticsCarbonhydrat.setText("Karbonhidrat: %"+yuzdeHesap(toplam,carbonhydrat));
        holder.staticsfat.setText("Yağ: %"+yuzdeHesap(toplam,fat));
        holder.day.setText("Tarih: "+myFormat.format(date));
        holder.staticsTotalCalorie.setText("Alınan Toplam Kalori\n"+calorie+"kcal");

        holder.pieChart.addPieSlice(
                new PieModel("Protein",(float) protein,Color.parseColor("#F9A825")));
        holder.pieChart.addPieSlice(
                new PieModel("Harbonhidrat",(float) fat,Color.parseColor("#C62828")));
        holder.pieChart.addPieSlice(
                new PieModel("Yag",(float) carbonhydrat,Color.parseColor("#29B6F6")));


        holder.pieChart.startAnimation();

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public double yuzdeHesap(double toplam, double istenen){
        istenen=(istenen/toplam)*100;
        istenen = Math.round(istenen * 100.0) / 100.0;

        return istenen;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, staticsprotein, staticsfat, staticsCarbonhydrat,staticsTotalCalorie;
        LinearLayout linearLayout;
        PieChart pieChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.staticticsDayText);
            staticsprotein = itemView.findViewById(R.id.staticitcsProteintext);
            staticsCarbonhydrat = itemView.findViewById(R.id.staticticsKarbonhidrattext);
            staticsfat = itemView.findViewById(R.id.staticitcsfattext);
            linearLayout = itemView.findViewById(R.id.container);
            pieChart = itemView.findViewById(R.id.macroChart);
            staticsTotalCalorie = itemView.findViewById(R.id.statisticsTotalCalori);
        }
    }
}
