package com.example.kalori;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.activity.ProfilActivity;
import com.example.kalori.activity.StatisticsActivity;
import com.example.kalori.activity.newMealActivity;
import com.example.kalori.realm.userTable;
import io.realm.Realm;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    int userLog;
    Realm realm;

    TextView name_surname, weight_height, age, abki;
    Button istatistik, yeniogun, profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realmTanimla();
        tanimla();
        kontrol();
        kullanicibilgi();

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

    }
    public void clickAction(View view){
        switch (view.getId()){
            case R.id.statistics: showStatistics();
                break;
            case  R.id.profil: showProfil();
                break;
            case R.id.newmeal: addNewMeal();
        }
    }

    private void showStatistics() {
        Intent intent = new Intent(MainActivity.this,StatisticsActivity.class );
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
        Sweight_height = "boy: " +height + " kilo: " + weight;
        Sage = "Yaş: "+newAge;
        Sbki ="bki: "+ Math.round(bki*100.0)/100.0;
        name_surname.setText(Sname);
        weight_height.setText(Sweight_height);
        age.setText(Sage);
        abki.setText(Sbki);
    }

    private String getAge(String birthday) {
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat, Locale.getDefault());
        LocalDate dateTime = LocalDate.parse(birthday,formatter);
        LocalDate dateNow = LocalDate.now();
        Period diff = Period.between(dateTime,dateNow);
        return ""+diff.getYears();






    }

    public void kontrol() {

        userLog = realm.where(userTable.class).findAll().size();

        if (userLog == 0) {
            Intent intent = new Intent(MainActivity.this, newUser.class);
            startActivity(intent);
            finish();
        }
    }

}