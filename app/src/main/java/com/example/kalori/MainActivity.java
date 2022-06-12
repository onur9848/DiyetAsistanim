package com.example.kalori;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalori.activity.ProfilActivity;
import com.example.kalori.activity.StatisticsActivity;
import com.example.kalori.activity.newMealActivity;
import com.example.kalori.activity.newUser;
import com.example.kalori.adapter.MealAdapter;
import com.example.kalori.realm.addMealTable;
import com.example.kalori.realm.userTable;
import io.realm.Realm;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    Realm realm;

    TextView name_surname, weight_height, age, abki;
    TextView totalprotein, totalfat, totalkarbonhidrat, toplamKalori;
    Button istatistik, yeniogun, profil;
    ImageView deleteimage;
    RecyclerView list_recycler;
    LinearLayout mainpage;
    MealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realmTanimla();
        tanimla();
        int userLog = realm.where(userTable.class).findAll().size();
        if (userLog == 0) {
            Intent intent = new Intent(getApplicationContext(), newUser.class);
            startActivity(intent);
            finish();
        } else {
            kullanicibilgi();
            showList();
            setToplamKalori();
        }
    }


    @Override
    public void onResume() {

        super.onResume();
        showList();
        setToplamKalori();
    }


    public void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    public void tanimla() {
        name_surname = (TextView) findViewById(R.id.name_surname);
        weight_height = (TextView) findViewById(R.id.height_weight);
        age = (TextView) findViewById(R.id.age);
        abki = (TextView) findViewById(R.id.bki);
        istatistik = (Button) findViewById(R.id.statistics);
        profil = (Button) findViewById(R.id.profil);
        yeniogun = (Button) findViewById(R.id.newmeal);
        list_recycler = (RecyclerView) findViewById(R.id.recyler_view_list);
        mainpage = (LinearLayout) findViewById(R.id.mainpage);
        toplamKalori = (TextView) findViewById(R.id.mainToplamKalori);
        totalfat = (TextView) findViewById(R.id.totalfat);
        totalkarbonhidrat = (TextView) findViewById(R.id.totalKarbonhidrat);
        totalprotein = (TextView) findViewById(R.id.totalProtein);
        deleteimage = (ImageView) findViewById(R.id.deleteproduct);

    }

    public void clickAction(View view) {
        switch (view.getId()) {
            case R.id.statistics:
                showStatistics();
                break;
            case R.id.profil:
                showProfil();
                break;

            case R.id.newmeal:
                addNewMeal();
                break;
            case R.id.mainpage:
                mainpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setToplamKalori();
                    }
                });
        }
    }

    private void showStatistics() {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(intent);
    }

    private void showProfil() {

        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
        startActivity(intent);

    }

    private void addNewMeal() {
        Intent intent = new Intent(MainActivity.this, newMealActivity.class);
        startActivity(intent);

    }

    public void kullanicibilgi() {
        String Sname, Sweight_height, Sage, Sbki;
        userTable userTable = realm.where(com.example.kalori.realm.userTable.class).findFirst();
        String name = userTable.getDbname();
        String surname = userTable.getDbsurname();
        Double height = userTable.getDbheight();
        Double weight = userTable.getDbweight();
        double bki = weight / (Math.pow(height / 100, 2));
        String birthday = userTable.getDbbirthday();
        String newAge = getAge(birthday);


        Sname = name + " " + surname;
        Sweight_height = "Boy: " + height + " Kilo: " + weight;
        Sage = "Yaş: " + newAge;
        Sbki = "Bki: " + Math.round(bki * 100.0) / 100.0;
        name_surname.setText(Sname);
        weight_height.setText(Sweight_height);
        age.setText(Sage);
        abki.setText(Sbki);
    }

    private String getAge(String birthday) {
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat, Locale.getDefault());
        LocalDate dateTime = LocalDate.parse(birthday, formatter);
        LocalDate dateNow = LocalDate.now();
        Period diff = Period.between(dateTime, dateNow);
        return "" + diff.getYears();
    }


    public void showList() {
//        Toast.makeText(this, "selam", Toast.LENGTH_SHORT).show();
        List<addMealTable> addMealTables = new ArrayList<addMealTable>(realm.where(addMealTable.class).findAll());
        List<addMealTable> addMealTablesToday = new ArrayList<addMealTable>();
        addMealTablesToday = getTodayList(addMealTables);
        adapter = new MealAdapter(addMealTablesToday, this);
        list_recycler.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_recycler.setLayoutManager(linearLayoutManager);

    }


    private List<addMealTable> getTodayList(List<addMealTable> addMealTables) {
        List<addMealTable> newList = new ArrayList<>();
        Date today = Date.from(Instant.now());

        for (int i = 0; i < addMealTables.size(); i++) {
            Date itemDay = addMealTables.get(i).getDay();
            if (itemDay.getDay() == today.getDay() && itemDay.getMonth() == today.getMonth() && itemDay.getYear() == today.getYear())
                newList.add(addMealTables.get(i));


        }

        return newList;
    }

    @SuppressLint("SetTextI18n")
    public void setToplamKalori() {
        List<addMealTable> addMealTables = new ArrayList<addMealTable>(realm.where(addMealTable.class).findAll());
        List<addMealTable> todayMealList = getTodayList(addMealTables);
        double toplamKaloridouble = 0, toplamKarbonhidrat = 0, toplamYag = 0, toplamProtein = 0, yuzdeYag, yuzdeKarbon, yuzdeProtein;
        for (int i = 0; i < todayMealList.size(); i++) {
            toplamKaloridouble += +todayMealList.get(i).getCalorie();
            toplamKarbonhidrat += todayMealList.get(i).getCarbonhydrat();
            toplamYag += todayMealList.get(i).getFat();
            toplamProtein += todayMealList.get(i).getProtein();

        }
        double totalValue = toplamKarbonhidrat + toplamProtein + toplamYag;
        yuzdeKarbon = (toplamKarbonhidrat / totalValue) * 100;
        yuzdeProtein = (toplamYag / totalValue) * 100;
        yuzdeYag = (toplamYag / totalValue) * 100;

        totalprotein.setText("Protein: %" + yuvarlama(yuzdeProtein) + "\n" + yuvarlama(toplamProtein) + "gram");
        totalfat.setText("Yag: %" + yuvarlama(yuzdeYag) + "\n" + yuvarlama(toplamYag) + "gram");
        totalkarbonhidrat.setText("Karbohidrat: %" + yuvarlama(yuzdeKarbon) + "\n" + yuvarlama(toplamKarbonhidrat) + "gram");
        toplamKalori.setText(toplamKaloridouble + "");
    }

    double yuvarlama(double sayi) {
        sayi = Math.round(sayi * 100.0) / 100.0;
        return sayi;
    }

}