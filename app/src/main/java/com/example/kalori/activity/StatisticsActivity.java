package com.example.kalori.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;
import com.example.kalori.adapter.ViewPagerAdapter;
import com.example.kalori.realm.addMealTable;
import com.example.kalori.realm.dailyMacroDetailTable;
import com.example.kalori.realm.weightHistory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import io.realm.Realm;
import io.realm.RealmResults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    ViewPager2 macroDetail;
    Realm realm;
    SimpleDateFormat myFormat;
    LineChart mpLinechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        tanimla();
        listele();
        doldurDailyMacro();
        setWeightHistorymp();
        butondeneme();

    }

    private void butondeneme() {
        Button button = findViewById(R.id.denemebuton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StatisticsActivity.this, "tıkla", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWeightHistorymp() {
        List<Integer> tarih_modelList= new ArrayList<>();
        ArrayList<Entry> myEntry =new ArrayList<>();//yd_degeri
        List<String> myDay = new ArrayList<>();//gunle
        mpLinechart.setDragEnabled(true);
        mpLinechart.setScaleEnabled(false);

        YAxis leftAxis = mpLinechart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.enableGridDashedLine(10f,10f,0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        mpLinechart.getAxisRight().setEnabled(false);
        RealmResults<weightHistory> weightHistories=realm.where(weightHistory.class).findAll();
        for (int i =0; i<weightHistories.size();i++){
            myEntry.add(new Entry(i,(float) weightHistories.get(i).getWeight()));
            myDay.add(myFormat.format(weightHistories.get(i).getDate()));
        }
        LineDataSet set1 = new LineDataSet(myEntry,"Kilo Grafiği");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
//        myDay.add("pzt");
//        myDay.add("salı");
//        myDay.add("çrşm");
//        myDay.add("prşmb");
//        myDay.add("cuma");
//        myDay.add("cmrts");
//        myDay.add("pzr");

        LineData data = new LineData(dataSets);
        mpLinechart.setData(data);
        mpLinechart.setVisibleXRangeMaximum(7);
        mpLinechart.setGridBackgroundColor(Color.BLACK);
        mpLinechart.setDragEnabled(true);
        XAxis axis = mpLinechart.getXAxis();
        axis.setValueFormatter(new myAxisValueFormat(myDay));
        axis.setGranularity(1);
        axis.setTextColor(Color.BLUE);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);

    }



    private void listele() {
        List<dailyMacroDetailTable> dailyList = realm.where(dailyMacroDetailTable.class).findAll();
        macroDetail.setAdapter(new ViewPagerAdapter(this, dailyList, macroDetail));
    }

    @SuppressLint("SimpleDateFormat")
    private void tanimla() {
        macroDetail = (ViewPager2) findViewById(R.id.staticticsmakrodetails);
        realm = Realm.getDefaultInstance();
        myFormat = new SimpleDateFormat("dd/MM/yyyy");
        mpLinechart = findViewById(R.id.mpChart);
    }

    public void doldurDailyMacro() {
        int a = 0;
        RealmResults<addMealTable> addMealTable = realm.where(com.example.kalori.realm.addMealTable.class).findAll();
        if (!addMealTable.isEmpty()) {
            listeledaily(addMealTable, a);
        }
        int lastpage= macroDetail.getChildCount();
        macroDetail.setCurrentItem(lastpage);
    }

    public void listeledaily(RealmResults<addMealTable> addMealTable, int a) {
        Date date = addMealTable.get(a).getDay();
        String listDate = myFormat.format(date), realmDate;

        double totalcalorie = 0, totalprotein = 0, totalfat = 0, totalcarbonhydrat = 0;
        for (int i = a; i < addMealTable.size(); i++) {
            realmDate = myFormat.format(addMealTable.get(i).getDay());
            a = i;
            if (realmDate.equals(listDate)) {
                totalcalorie += addMealTable.get(i).getCalorie();
                totalcarbonhydrat += addMealTable.get(i).getCarbonhydrat();
                totalfat += addMealTable.get(i).getFat();
                totalprotein += addMealTable.get(i).getProtein();

            } else {

                date = addMealTable.get(i).getDay();
                listDate = myFormat.format(date);

            }
        }

        if (checkTable(date, totalcalorie, totalcarbonhydrat, totalfat, totalprotein)) {
            setDailyMacroDetailTable(date, totalcalorie, totalcarbonhydrat, totalfat, totalprotein);
        }
        if (!addMealTable.last().equals(addMealTable.get(a))) {
            listeledaily(addMealTable, a);
        }
    }

    public Boolean checkTable(Date date, double totalcalorie, double totalcarbonhydrat, double totalfat,
                              double totalprotein) {
        RealmResults<dailyMacroDetailTable> dailyMacroDetailTables = realm.where(dailyMacroDetailTable.class).findAll();
        if (dailyMacroDetailTables.isEmpty())
            return true;
        else {
            String lastDate = myFormat.format(dailyMacroDetailTables.last().getDate());
            String indexDate = myFormat.format(date);
            double lastcalorie = dailyMacroDetailTables.last().getDbTotalCalorie();
            if (indexDate.equals(lastDate)) {
                if (totalcalorie == lastcalorie) {
                    return false;
                } else
                    updateDailyMacroDetailTable(date, totalcalorie, totalcarbonhydrat, totalfat, totalprotein);
                return false;
            } else
                return true;
        }
    }

    public void updateDailyMacroDetailTable(Date date, double totalcalorie, double totalcarbonhydrat, double totalfat,
                                            double totalprotein) {
        RealmResults<dailyMacroDetailTable> dailyMacroDetailTables = realm.where(dailyMacroDetailTable.class).findAll();
        realm.beginTransaction();
        dailyMacroDetailTables.last().setDbTotalCalorie(yuvarlama(totalcalorie));
        dailyMacroDetailTables.last().setDbTotalProtein(yuvarlama(totalprotein));
        dailyMacroDetailTables.last().setDbTotalCarbonhydrat(yuvarlama(totalcarbonhydrat));
        dailyMacroDetailTables.last().setDbTotalFat(yuvarlama(totalfat));
        realm.commitTransaction();

    }

    public void setDailyMacroDetailTable(Date date, double totalcalorie, double totalcarbonhydrat, double totalfat,
                                         double totalprotein) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dailyMacroDetailTable dailyMacroDetailTable = realm.createObject(com.example.kalori.realm.dailyMacroDetailTable.class);
                dailyMacroDetailTable.setDate(date);
                dailyMacroDetailTable.setDbTotalCalorie(yuvarlama(totalcalorie));
                dailyMacroDetailTable.setDbTotalCarbonhydrat(yuvarlama(totalcarbonhydrat));
                dailyMacroDetailTable.setDbTotalFat(yuvarlama(totalfat));
                dailyMacroDetailTable.setDbTotalProtein(yuvarlama(totalprotein));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(StatisticsActivity.this, "başarılı", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "hatali", Toast.LENGTH_SHORT).show();
            }
        });


    }

    double yuvarlama(double sayi) {
        sayi = Math.round(sayi * 100.0) / 100.0;
        return sayi;
    }
}
