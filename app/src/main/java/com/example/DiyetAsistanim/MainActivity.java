package com.example.DiyetAsistanim;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.DiyetAsistanim.activity.ProfilActivity;
import com.example.DiyetAsistanim.activity.StatisticsActivity;
import com.example.DiyetAsistanim.activity.newMealActivity;
import com.example.DiyetAsistanim.activity.newUser;
import com.example.DiyetAsistanim.adapter.MealAdapter;
import com.example.DiyetAsistanim.realm.addMealTable;
import com.example.DiyetAsistanim.realm.dailyMacroDetailTable;
import com.example.DiyetAsistanim.realm.dailyWaterTable;
import com.example.DiyetAsistanim.realm.userTable;
import io.realm.Realm;
import io.realm.RealmResults;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainActivity extends AppCompatActivity {


    Realm realm;

    TextView name_surname, weight_height, age, abki, suEkleText, progressBarText, adviceText;
    TextView totalprotein, totalfat, totalkarbonhidrat, toplamKalori, hedefKalori;
    Button istatistik, yeniogun, profil, suEkleButton;
    ProgressBar dailyWaterBar;
    ImageView deleteimage;
    RecyclerView list_recycler;
    LinearLayout mainpage, adviceView;
    MealAdapter adapter;
    float avarageCalorie;
    double gunlukSu;
    SimpleDateFormat myFormat;

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
            avarageCalorieCalculate();
            hideShow(true);
            adviceSystem();
        }
    }


    public void deleteRealmDatabase() {
        realm.beginTransaction();
        realm.commitTransaction();

    }

    @Override
    public void onResume() {

        super.onResume();
        kullanicibilgi();
        showList();
        setToplamKalori();
        adviceSystem();

    }


    public void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    @SuppressLint("SimpleDateFormat")
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
        dailyWaterBar = (ProgressBar) findViewById(R.id.waterBar);
        myFormat = new SimpleDateFormat("yyyy/MM/dd");
        progressBarText = (TextView) findViewById(R.id.progrsBarText);
        hedefKalori = (TextView) findViewById(R.id.hedefKalori);
        adviceView = (LinearLayout) findViewById(R.id.mainAdviceView);
        adviceText = (TextView) findViewById(R.id.mainAdviceText);
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
        userTable userTable = realm.where(com.example.DiyetAsistanim.realm.userTable.class).findFirst();
        String name = userTable.getDbname();
        String surname = userTable.getDbsurname();
        Double height = userTable.getDbheight();
        Double weight = userTable.getDbweight();
        double bki = weight / (Math.pow(height / 100, 2));
        String birthday = userTable.getDbbirthday();
        String newAge = getAge(birthday);
        gunlukSu = weight * 0.035;
        gunlukSu = yuvarlama(gunlukSu);


        Sname = name + " \n" + surname;
        Sweight_height = "Boy: " + height + " Kilo: " + weight;
        Sage = "Ya??: " + newAge;
        Sbki = "Bki: " + Math.round(bki * 100.0) / 100.0;
        name_surname.setText(Sname);
        weight_height.setText(Sweight_height);
        age.setText(Sage);
        abki.setText(Sbki);
        setGunlukSu();

    }

    private void setGunlukSu() {
        setWaterStatusBar();
        dailyWaterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddWaterDialog();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void setWaterStatusBar() {
        String text;
        int proggresBarStatus = 0;
        double icilensu;
        proggresBarStatus = getProggresBarStatus();
        if (proggresBarStatus > 100) {
            proggresBarStatus = 100;
            progressBarText.setText("Tebrikler g??nl??k su i??me hedefinizi tamamlad??n??z.");
        }
        dailyWaterBar.setProgress(proggresBarStatus);
    }

    private int getProggresBarStatus() {
        double icilensu;
        String text;
        int proggresBarStatus;

        if (!realm.where(dailyWaterTable.class).findAll().isEmpty()) {
            dailyWaterTable lastTable = realm.where(dailyWaterTable.class).findAll().last();
            icilensu = lastTable.getDailyWater();
        } else {
            icilensu = 0;
        }
        text = Double.toString(icilensu / 1000) + " L/" + gunlukSu + " L";
        progressBarText.setText(text);

        proggresBarStatus = (int) Math.round(icilensu / gunlukSu / 10);
        return proggresBarStatus;
    }

    public void showAddWaterDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_water_dialog);
        suEkleText = (TextView) dialog.findViewById(R.id.suEkleText);
        suEkleButton = (Button) dialog.findViewById(R.id.suEkleButton);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        suEkleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double icilensu;
                icilensu = getWaterText(suEkleText);
                dailyWaterTable dailyWaterTables = new dailyWaterTable();
                dailyWaterTables.setDailyWater(icilensu);
                dailyWaterTables.setDate(Date.from(Instant.now()));
                setDailyWaterTable(dailyWaterTables);
                dialog.hide();

                int proggresBarStatus = 0;
                proggresBarStatus = getProggresBarStatus();
                if (proggresBarStatus > 100) {
                    proggresBarStatus = 100;
                    progressBarText.setText("Tebrikler g??nl??k su i??me hedefinizi tamamlad??n??z.");
                }

                dailyWaterBar.setProgress(proggresBarStatus);

            }
        });


    }

    private double getWaterText(TextView waterText) {
        String waterString = waterText.getText().toString();
        if (waterString.isEmpty()) {
            return 200;
        } else {
            return Double.parseDouble(waterString);
        }


    }


    private void setDailyWaterTable(dailyWaterTable object) {
        RealmResults<dailyWaterTable> dailyWaterTables = realm.where(dailyWaterTable.class).findAll();
        if (dailyWaterTables.isEmpty()) {
            addDailyWaterTable(object);
        } else {
            for (dailyWaterTable tables : dailyWaterTables) {
                String objectDateFormat, tableDateFormat;
                objectDateFormat = myFormat.format(object.getDate());
                tableDateFormat = myFormat.format(tables.getDate());
                if (objectDateFormat.equals(tableDateFormat)) {
                    updateDailyWaterTable(object);

                } else if (objectDateFormat.compareTo(tableDateFormat)>0) {
                    addDailyWaterTable(object);
                }

            }
        }
    }

    public void addDailyWaterTable(dailyWaterTable object) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dailyWaterTable dailyWaterTables = realm.createObject(dailyWaterTable.class);
                dailyWaterTables.setDate(object.getDate());
                dailyWaterTables.setDailyWater(object.getDailyWater());


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });


    }

    public void updateDailyWaterTable(dailyWaterTable object) {
        RealmResults<dailyWaterTable> dailyWaterTables = realm.where(dailyWaterTable.class).findAll();
        String objectDateFormat, tableDateFormat;
        objectDateFormat = myFormat.format(object.getDate());
        for (int i = 0; i < dailyWaterTables.size(); i++) {
            tableDateFormat = myFormat.format(dailyWaterTables.get(i).getDate());
            if (objectDateFormat.equals(tableDateFormat)) {
                realm.beginTransaction();
                double newValue = 0;
                newValue = dailyWaterTables.get(i).getDailyWater() + object.getDailyWater();
                dailyWaterTables.get(i).setDate(object.getDate());
                dailyWaterTables.get(i).setDailyWater(newValue);
                realm.commitTransaction();
            }
        }
    }

    private void avarageCalorieCalculate() {
        userTable user = realm.where(userTable.class).findAll().first();
        double age = Double.parseDouble(getAge(user.getDbbirthday()));

        if (user.getDbcinsiyet().equals("Erkek")) {
            if (user.getTercih() == 1) {
                avarageCalorie = (float) (66 + 13.7 * user.getDbweight() + 5 * user.getDbheight() - 6.8 * age) * 1.1f;

            } else if (user.getTercih() == 2) {
                avarageCalorie = (float) (66 + 13.7 * user.getDbweight() + 5 * user.getDbheight() - 6.8 * age) * 1.4f;

            } else {
                avarageCalorie = (float) (66 + 13.7 * user.getDbweight() + 5 * user.getDbheight() - 6.8 * age) * 1.7f;

            }

        } else {
            if (user.getTercih() == 1) {
                avarageCalorie = (float) (655 + 9.6 * user.getDbweight() + 1.8 * user.getDbheight() - 4.7 * age) * 1.1f;
            } else if (user.getTercih() == 2) {
                avarageCalorie = (float) (655 + 9.6 * user.getDbweight() + 1.8 * user.getDbheight() - 4.7 * age) * 1.4f;
            } else {
                avarageCalorie = (float) (655 + 9.6 * user.getDbweight() + 1.8 * user.getDbheight() - 4.7 * age) * 1.7f;
            }
        }

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
        yuzdeProtein = (toplamProtein / totalValue) * 100;
        yuzdeYag = (toplamYag / totalValue) * 100;

        totalprotein.setText("Protein: %" + yuvarlama(yuzdeProtein) + "\n" + yuvarlama(toplamProtein) + "gram");
        totalfat.setText("Yag: %" + yuvarlama(yuzdeYag) + "\n" + yuvarlama(toplamYag) + "gram");
        totalkarbonhidrat.setText("Karbohidrat: %" + yuvarlama(yuzdeKarbon) + "\n" + yuvarlama(toplamKarbonhidrat) + "gram");
        toplamKalori.setText(toplamKaloridouble + "");
        hedefKalori.setText("Hedef: " + yuvarlama(avarageCalorie));
    }

    double yuvarlama(double sayi) {
        sayi = Math.round(sayi * 100.0) / 100.0;
        return sayi;
    }

    public void hideShow(boolean hideStatus) {
        LinearLayout.LayoutParams params;
        if (hideStatus) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            adviceView.setLayoutParams(params);
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            adviceView.setLayoutParams(params);
        }
    }

    public void adviceSystem() {
        RealmResults<dailyMacroDetailTable> macroDetailTables = realm.where(dailyMacroDetailTable.class).findAll();
        RealmResults<dailyWaterTable> waterTable = realm.where(dailyWaterTable.class).findAll();
        dailyWaterTable todayWater = null;
        dailyMacroDetailTable todayMacro = null;
        String listDate, todayDate = myFormat.format(Date.from(Instant.now()));
        for (dailyMacroDetailTable listMacro : macroDetailTables) {
            listDate = myFormat.format(listMacro.getDate());
            if (todayDate.equals(listDate)) {
                todayMacro = listMacro;
            }
        }
        for (dailyWaterTable listWater : waterTable) {
            listDate = myFormat.format(listWater.getDate());
            if (todayDate.equals(listDate)) {
                todayWater = listWater;
            }

        }

        if (todayMacro == null && todayWater == null) {
            hideShow(true);
        } else if (todayMacro != null && todayWater == null) {
            hideShow(false);
            adviceText.setText(getTodayMacroText(todayMacro));

        } else if (todayMacro == null && todayWater != null) {
            hideShow(false);
            adviceText.setText(getTodayWaterText(todayWater));

        } else {
            hideShow(false);
            adviceText.setText(getTodayMacroText(todayMacro) + getTodayWaterText(todayWater));

        }


    }

    private String getTodayWaterText(dailyWaterTable todayWater) {
        String waterAdvice;
        double yuzde = yuvarlama(todayWater.getDailyWater() / gunlukSu / 10);
        Calendar now = Calendar.getInstance();
        now.setTime(Date.from(Instant.now()));
        int hours = now.get(Calendar.HOUR_OF_DAY);
        if (yuzde > 20 && yuzde < 30) {
            if (hours < 12)
                waterAdvice = "??ok iyi gidiyorsunuz.";
            else if (hours < 16)
                waterAdvice = "Daha ??ok su i??melisiniz.";
            else if (hours < 20)
                waterAdvice = "Su t??ketiminiz ??ok az, l??tfen daha ??ok su t??ketmeyi deneyiniz.";
            else
                waterAdvice = "G??n i??inde ??ok az su t??kettiniz, az su t??ketimi bir??ok probleme sebep olabilir l??ften g??n i??inde d??zenli olarak su t??ketmeye dikkat ediniz.";
        } else if (yuzde < 50) {
            if (hours < 12)
                waterAdvice = "??ok iyi gidiyorsunuz";
            else if (hours < 16)
                waterAdvice = "??ok iyi gidiyorsunuz.";
            else if (hours < 20)
                waterAdvice = "Daha ??ok su i??melisiniz.";
            else
                waterAdvice = "Su t??ketiminiz ??ok az, l??tfen daha ??ok su t??ketmeyi deneyiniz.";
        } else if (yuzde < 70) {
            if (hours < 12)
                waterAdvice = "Fazla su t??ketmemeye dikkat ediniz, fazla su t??ketimi su zehirlenmelerine sebep olabilir.";
            else if (hours < 16)
                waterAdvice = "??ok iyi gidiyorsunuz";
            else if (hours < 20)
                waterAdvice = "Su hedefinize ula??man??za ??ok az kald??";
            else
                waterAdvice = "Biraz daha su i??meyi deneyiniz";
        } else {
            if (hours < 12)
                waterAdvice = "??ok fazla su t??ketiyorsunuz, k??sa zamanda ??ok su t??ketmemeye dikkat ediniz. Fazla su t??ketimi hastal??klara sebep olabilir";
            else if (hours < 16)
                waterAdvice = "Su t??ketimini g??ne yayman??z?? ??neririz.";
            else if (hours < 20)
                waterAdvice = "??ok iyi gidiyorsunuz";
            else
                waterAdvice = "Hedefinize ??ok az kald??";

        }


        return "Su: "+waterAdvice;
    }

    private String getTodayMacroText(dailyMacroDetailTable todayMacro) {
        double kalori = todayMacro.getDbTotalCalorie();
        String macroAdvice = "";
        double protein, yag, karbonhidrat, proteinyuzde, yagyuzde, karbonhidratyuzde;
        protein = todayMacro.getDbTotalProtein();
        yag = todayMacro.getDbTotalFat();
        karbonhidrat = todayMacro.getDbTotalCarbonhydrat();
        proteinyuzde = protein / (protein + yag + karbonhidrat)*100;
        yagyuzde = yag / (protein + yag + karbonhidrat)*100;
        karbonhidratyuzde = karbonhidrat / (protein + yag + karbonhidrat)*100;

        if (kalori < 1000) {
        } else if (kalori > avarageCalorie / 2) {
            macroAdvice = getString(proteinyuzde, yagyuzde, karbonhidratyuzde);
            macroAdvice = "??????n:"+macroAdvice+"/n";
            return macroAdvice;

        } else if (kalori > avarageCalorie) {
            macroAdvice = "Alman??z gereken kaloriyi ge??tiniz. " + getString(proteinyuzde, yagyuzde, karbonhidratyuzde);
            macroAdvice = "??????n:"+macroAdvice+"/n";
            return macroAdvice;

        }


        return "";
    }

    private String getString(double proteinyuzde, double yagyuzde, double karbonhidratyuzde) {
        String macroAdvice;
        if (proteinyuzde<10){
            macroAdvice = " ??ok az protein ald??n??z.";
        }
        else if (proteinyuzde<35){
            macroAdvice =" Protein t??ketiminiz ??ok iyi.";
        }
        else {
            macroAdvice = " ??ok fazla protein t??kettiniz.";

        }
        if (yagyuzde<20){
            macroAdvice += " Ya?? t??ketiminiz az.";
        }
        else if (yagyuzde<35){
            macroAdvice += " Ya?? t??ketimin ideal.";
        }
        else {
            macroAdvice +=" ??ok fazla ya??l?? t??ketiyorsunuz. Sa??l??????n??z i??in ya?? t??ketimini azalt??n.";
        }
        if (karbonhidratyuzde<65) {
            macroAdvice += " Karbonhidrat t??ketiminiz gayet iyi.";
        }else {
            macroAdvice +=" Daha az karbonhidratl?? besinler t??ketmeye ??al??????n??z.";
        }
        return macroAdvice;
    }


}