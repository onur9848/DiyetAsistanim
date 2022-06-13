package com.example.kalori.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;
import com.example.kalori.adapter.ViewPagerAdapter;
import com.example.kalori.realm.addMealTable;
import com.example.kalori.realm.dailyMacroDetailTable;
import com.example.kalori.realm.weightHistory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    ViewPager2 macroDetail,WeightDetail;
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
        kaloriKiyasla();
    }


    private void setWeightHistorymp() {
        ArrayList<Entry> myEntry = new ArrayList<>();//yd_degeri
        List<String> myDay = new ArrayList<>();//gunle
        mpLinechart.setDragEnabled(true);
        mpLinechart.setScaleEnabled(false);

        YAxis leftAxis = mpLinechart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        mpLinechart.getAxisRight().setEnabled(false);
        RealmResults<weightHistory> weightHistories = realm.where(weightHistory.class).findAll();
        for (int i = 0; i < weightHistories.size(); i++) {
            myEntry.add(new Entry(i, (float) weightHistories.get(i).getWeight()));
            myDay.add(myFormat.format(weightHistories.get(i).getDate()));
        }
        LineDataSet set1 = new LineDataSet(myEntry, "Kilo Grafiği");
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
        mpLinechart.setVisibleXRangeMaximum(5);
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
        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        mpLinechart = findViewById(R.id.mpChart);

    }

    public void doldurDailyMacro() {
        int a = 0;
        RealmResults<addMealTable> addMealTable = realm.where(com.example.kalori.realm.addMealTable.class).findAll();
        if (!addMealTable.isEmpty()) {
            dailyMacroDetailTable(addMealTable);
        }
        int lastpage = realm.where(dailyMacroDetailTable.class).findAll().size() - 1;
        macroDetail.setCurrentItem(lastpage);
    }

    public void dailyMacroDetailTable(RealmResults<addMealTable> realmResults) {

        RealmResults<dailyMacroDetailTable> newTable;
        Date addmealDate = realmResults.first().getDay();
        Date newDate = null;
        String addmealFormatDate = myFormat.format(addmealDate);
        String realmResultFormat;
        double totalcalorie = 0, totalprotein = 0, totalfat = 0, totalcarbonhydrat = 0;


        for (int i = 0; i < realmResults.size(); ) {
            realmResultFormat = myFormat.format(realmResults.get(i).getDay());
            dailyMacroDetailTable dailyMacro = new dailyMacroDetailTable();
            if (addmealFormatDate.equals(realmResultFormat)) {
                newDate = realmResults.get(i).getDay();
                totalcalorie += realmResults.get(i).getCalorie();
                totalcarbonhydrat += realmResults.get(i).getCarbonhydrat();
                totalfat += realmResults.get(i).getFat();
                totalprotein += realmResults.get(i).getProtein();
                i++;
            } else {


                dailyMacro.setDate(newDate);
                dailyMacro.setDbTotalCalorie(totalcalorie);
                dailyMacro.setDbTotalCarbonhydrat(totalcarbonhydrat);
                dailyMacro.setDbTotalFat(totalfat);
                dailyMacro.setDbTotalProtein(totalprotein);
                checkTable(dailyMacro);
                newDate = realmResults.get(i).getDay();
                totalcalorie = 0;
                totalprotein = 0;
                totalfat = 0;
                totalcarbonhydrat = 0;
                addmealFormatDate = myFormat.format(newDate);
            }
            if (i == realmResults.size()) {
                dailyMacro.setDate(newDate);
                dailyMacro.setDbTotalCalorie(totalcalorie);
                dailyMacro.setDbTotalCarbonhydrat(totalcarbonhydrat);
                dailyMacro.setDbTotalFat(totalfat);
                dailyMacro.setDbTotalProtein(totalprotein);
                checkTable(dailyMacro);
            }
        }

    }

    private void deleteAllDailyMealTable() {
        RealmResults<dailyMacroDetailTable> table = realm.where(dailyMacroDetailTable.class).findAll();
        realm.beginTransaction();
        for (dailyMacroDetailTable tables : table) {
            tables.deleteFromRealm();

        }
        realm.commitTransaction();
    }

    public void checkTable(dailyMacroDetailTable newObject) {
        RealmResults<dailyMacroDetailTable> dailyMacroDetailTables = realm.where(dailyMacroDetailTable.class).findAll();
        if (!dailyMacroDetailTables.isEmpty()) {
            Date lastDate = dailyMacroDetailTables.last().getDate();
            Date itemDate = newObject.getDate();
            String lastDateFormat, itemDateformat;
            lastDateFormat = myFormat.format(lastDate);
            itemDateformat = myFormat.format(itemDate);
            if (lastDateFormat.equals(itemDateformat)) {
                if (dailyMacroDetailTables.last().getDbTotalCalorie() != newObject.getDbTotalCalorie()) {
                    updateDailyMacroDetailTable(newObject);
                }
            } else if (lastDateFormat.compareTo(itemDateformat) < 0) {
                setDailyMacroDetailTable(newObject);
            } else {
                updateDailyMacroDetailTable(newObject);
            }
        } else {
            setDailyMacroDetailTable(newObject);
        }


    }

    public void updateDailyMacroDetailTable(dailyMacroDetailTable newObject) {
        RealmResults<dailyMacroDetailTable> dailyMacroDetailTables = realm.where(dailyMacroDetailTable.class).findAll();
        List<Boolean> bool = new ArrayList<Boolean>();
        int index;
        String dailyTableFormat, newobjectformat;
        newobjectformat = myFormat.format(newObject.getDate());
        for (dailyMacroDetailTable table : dailyMacroDetailTables) {
            dailyTableFormat = myFormat.format(table.getDate());
            bool.add(newobjectformat.equals(dailyTableFormat));
        }
        index = bool.indexOf(true);


        realm.beginTransaction();
        dailyMacroDetailTables.get(index).setDbTotalCalorie(yuvarlama(newObject.getDbTotalCalorie()));
        dailyMacroDetailTables.get(index).setDbTotalProtein(yuvarlama(newObject.getDbTotalProtein()));
        dailyMacroDetailTables.get(index).setDbTotalCarbonhydrat(yuvarlama(newObject.getDbTotalCarbonhydrat()));
        dailyMacroDetailTables.get(index).setDbTotalFat(yuvarlama(newObject.getDbTotalFat()));
        realm.commitTransaction();
        bool.clear();

    }

    public void setDailyMacroDetailTable(dailyMacroDetailTable newobject) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dailyMacroDetailTable dailyMacroDetailTable = realm.createObject(com.example.kalori.realm.dailyMacroDetailTable.class);
                dailyMacroDetailTable.setDate(newobject.getDate());
                dailyMacroDetailTable.setDbTotalCalorie(yuvarlama(newobject.getDbTotalCalorie()));
                dailyMacroDetailTable.setDbTotalCarbonhydrat(yuvarlama(newobject.getDbTotalCarbonhydrat()));
                dailyMacroDetailTable.setDbTotalFat(yuvarlama(newobject.getDbTotalFat()));
                dailyMacroDetailTable.setDbTotalProtein(yuvarlama(newobject.getDbTotalProtein()));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(StatisticsActivity.this, "başarılı", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "hatali" + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    double yuvarlama(double sayi) {
        sayi = Math.round(sayi * 100.0) / 100.0;
        return sayi;
    }

    public void kaloriKiyasla() {
        Calendar mycal = Calendar.getInstance();
        RealmResults<dailyMacroDetailTable> tables = realm.where(dailyMacroDetailTable.class).findAll();
        List<Double> totalCalorieAday = new ArrayList<Double>();
        List<Double> totalCalorieAweek = new ArrayList<Double>();
        List<Double> totalCalorieAmonth = new ArrayList<Double>();
        List<Date> myDate = new ArrayList<Date>();
        //günlük
        for (dailyMacroDetailTable table : tables) {
            myDate.add(table.getDate());
            totalCalorieAday.add(table.getDbTotalCalorie());
        }
        List<Calendar> myCalenderList = new ArrayList<Calendar>();
        for (Date date : myDate) {
            mycal.setTime(date);
            myCalenderList.add(mycal);
        }
        //haftalık
        int hafta, yil;
//        for (int i=0 ;i<myCalenderList.size();i++){
//            hafta = myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR);
//            yıl = myCalenderList.get(i).get(Calendar.YEAR);
//            if ()
//        }
        hafta = myCalenderList.get(0).get(Calendar.WEEK_OF_YEAR);
        yil = myCalenderList.get(0).get(Calendar.YEAR);
        double totalkaloriHaftalik = 0;
        for (int i = 0; i < myCalenderList.size(); ) {
            if (hafta == myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR) && yil == myCalenderList.get(i).get(Calendar.YEAR)) {
                totalkaloriHaftalik += tables.get(i).getDbTotalCalorie();
                i++;
            } else {
                totalCalorieAweek.add(totalkaloriHaftalik);
                hafta = myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR);
                yil = myCalenderList.get(i).get(Calendar.YEAR);

            }
            if (i == myCalenderList.size()) {
                totalCalorieAweek.add(totalkaloriHaftalik);
            }
        }
        yil = 51;


        //aylık


    }


}
