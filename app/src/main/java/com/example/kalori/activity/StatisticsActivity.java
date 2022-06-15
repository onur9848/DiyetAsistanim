package com.example.kalori.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kalori.R;
import com.example.kalori.adapter.ViewPagerAdapter;
import com.example.kalori.realm.addMealTable;
import com.example.kalori.realm.dailyMacroDetailTable;
import com.example.kalori.realm.userTable;
import com.example.kalori.realm.weightHistory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import io.realm.Realm;
import io.realm.RealmResults;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsActivity extends AppCompatActivity {

    ViewPager2 macroDetail, calorieHistoryPager;
    Realm realm;
    SimpleDateFormat myFormat;
    LineChart mpLinechart;
    Spinner dayofrange;
    float avarageCalorie;
    ArrayAdapter<String> dayofrangeAdapter;
    List<String> spinnnerList;
    List<dayCalorie> totalCalorieAday;
    List<dayCalorie> totalCalorieAweek;
    List<dayCalorie> totalCalorieAmonth;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        tanimla();
        listele();
        avarageCalorieCalculate();
        doldurDailyMacro();
        setWeightHistorymp();
        kaloriKiyaslaifState();
        setSpinner();


    }

    private int getAge(String birthday) {
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat, Locale.getDefault());
        LocalDate dateTime = LocalDate.parse(birthday, formatter);
        LocalDate dateNow = LocalDate.now();
        Period diff = Period.between(dateTime, dateNow);
        return diff.getYears();
    }
    private void avarageCalorieCalculate() {
        userTable user = realm.where(userTable.class).findAll().first();
        double age = getAge(user.getDbbirthday());
        if (user.getDbcinsiyet().equals("Erkek")) {
            if (user.getTercih() == 1) {
                avarageCalorie = (float) (66+13.7*user.getDbweight()+5* user.getDbheight()-6.8*age)*1.1f;

            } else if (user.getTercih() == 2) {
                avarageCalorie = (float) (66+13.7*user.getDbweight()+5* user.getDbheight()-6.8*age)*1.4f;

            } else {
                avarageCalorie = (float) (66+13.7*user.getDbweight()+5* user.getDbheight()-6.8*age)*1.7f;

            }

        } else {
            if (user.getTercih() == 1) {
                avarageCalorie = (float) (655+9.6* user.getDbweight()+1.8* user.getDbheight()-4.7*age)*1.1f;
            } else if (user.getTercih() == 2) {
                avarageCalorie = (float) (655+9.6* user.getDbweight()+1.8* user.getDbheight()-4.7*age)*1.4f;
            } else {
                avarageCalorie = (float) (655+9.6* user.getDbweight()+1.8* user.getDbheight()-4.7*age)*1.7f;
            }
        }

    }

    public void kaloriKiyaslaifState() {
        RealmResults<dailyMacroDetailTable> tables = realm.where(dailyMacroDetailTable.class).findAll();
        if (!tables.isEmpty()) {
            kaloriKiyasla();
        }
    }

    private void barChartDoldur(int index) {


        barChart = findViewById(R.id.barChart);
        getBarEntries(index);
        barChart.setDragEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();

        LimitLine ll = new LimitLine(avarageCalorie, "Alınması gereken kalori");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);
        leftAxis.addLimitLine(ll);

        barDataSet = new BarDataSet(barEntriesArrayList, "Kalori Tablosu");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
//        barDataSet.setBarBorderWidth(10f);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barData.setBarWidth(0.55f);

        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

    }

    private void getBarEntries(int index) {

        List<dayCalorie> list = new ArrayList<dayCalorie>();
        if (index == 0) {
            list = totalCalorieAday;
        } else if (index == 1) {
            list = totalCalorieAweek;
        } else {
            list = totalCalorieAmonth;
        }

        barEntriesArrayList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++)
            barEntriesArrayList.add(new BarEntry(i, (float) list.get(i).getCalorie()));

    }

    private void setSpinner() {
        dayofrangeAdapter.setDropDownViewResource(R.layout.my_custom_spinner);
        dayofrange.setAdapter(dayofrangeAdapter);
        RealmResults<dailyMacroDetailTable> tables = realm.where(dailyMacroDetailTable.class).findAll();
        dayofrange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
//                Toast.makeText(StatisticsActivity.this, "Seçilen item:" + adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if (!tables.isEmpty()) {
                    barChartDoldur(index);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

        LineData data = new LineData(dataSets);
        mpLinechart.setData(data);
        mpLinechart.setVisibleXRangeMaximum(5);
        mpLinechart.setGridBackgroundColor(Color.BLACK);
        mpLinechart.setDragEnabled(true);

        if (myDay.size() > 2) {
            XAxis axis = mpLinechart.getXAxis();
            axis.setValueFormatter(new myAxisValueFormat(myDay));
            axis.setGranularity(1);
            axis.setTextColor(Color.BLUE);
            axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }

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
        dayofrange = findViewById(R.id.staticticsSpinnir);
        spinnnerList = new ArrayList<String>();
        spinnnerList.add("Günlük Kalori Tablosu");
        spinnnerList.add("Haftalık Kalori Tablosu");
        spinnnerList.add("Aylık Kalori Tablosu");
        dayofrangeAdapter = new ArrayAdapter<String>(this, R.layout.my_custom_spinner, spinnnerList);
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
        totalCalorieAday = new ArrayList<dayCalorie>();
        totalCalorieAweek = new ArrayList<dayCalorie>();
        totalCalorieAmonth = new ArrayList<dayCalorie>();
        List<Date> myDate = new ArrayList<Date>();
        //günlük


        for (dailyMacroDetailTable table : tables) {
            dayCalorie item = new dayCalorie();
            myDate.add(table.getDate());
            item.setDayText(myFormat.format(table.getDate()));
            item.setCalorie(table.getDbTotalCalorie());
            totalCalorieAday.add(item);
        }   // Day List


        List<Calendar> myCalenderList = new ArrayList<Calendar>();
        for (Date date : myDate) {
            Calendar newCal = Calendar.getInstance();
            newCal.setTime(date);
            myCalenderList.add(newCal);
        }


        int hafta, yil;
        hafta = myCalenderList.get(0).get(Calendar.WEEK_OF_YEAR);
        yil = myCalenderList.get(0).get(Calendar.YEAR);
        SimpleDateFormat listFormat = new SimpleDateFormat("MMMM/yyyy");
        double totalkaloriHaftalik = 0;


        for (int i = 0; i < myCalenderList.size(); ) {
            dayCalorie item = new dayCalorie();
            if (hafta == myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR) && yil == myCalenderList.get(i).get(Calendar.YEAR)) {
                totalkaloriHaftalik += tables.get(i).getDbTotalCalorie();
                i++;
            } else {
                item.setDayText(myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR) + ".Hafta");
                item.setCalorie(totalkaloriHaftalik);
                totalCalorieAweek.add(item);
                totalkaloriHaftalik = 0;
                hafta = myCalenderList.get(i).get(Calendar.WEEK_OF_YEAR);
                yil = myCalenderList.get(i).get(Calendar.YEAR);

            }
            if (i == myCalenderList.size()) {
                item.setDayText(myCalenderList.get(i - 1).get(Calendar.WEEK_OF_YEAR) + ".Hafta");
                item.setCalorie(totalkaloriHaftalik);
                totalCalorieAweek.add(item);
            }
        } // Week List

        //aylık
        int ay;
        ay = myCalenderList.get(0).get(Calendar.MONTH);
        yil = myCalenderList.get(0).get(Calendar.YEAR);
        double totalKaloriAylik = 0;
        for (int i = 0; i < myCalenderList.size(); ) {
            dayCalorie item = new dayCalorie();
            if (ay == myCalenderList.get(i).get(Calendar.MONTH) && yil == myCalenderList.get(i).get(Calendar.YEAR)) {
                totalKaloriAylik += tables.get(i).getDbTotalCalorie();
                i++;
            } else {
                item.setDayText(listFormat.format(myCalenderList.get(i).getTime()));
                item.setCalorie(totalkaloriHaftalik);
                totalCalorieAmonth.add(item);
                totalKaloriAylik = 0;
                ay = myCalenderList.get(i).get(Calendar.MONTH);
                yil = myCalenderList.get(i).get(Calendar.YEAR);
            }
            if (i == myCalenderList.size()) {
                item.setDayText(listFormat.format(myCalenderList.get(i - 1).getTime()));
                item.setCalorie(totalKaloriAylik);
                totalCalorieAmonth.add(item);
            }
        }  // Month List
    }


}

