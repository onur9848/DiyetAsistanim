package com.example.kalori.activity;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.R;
import com.example.kalori.realm.addMealTable;
import io.realm.Realm;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MealDetailActivity extends AppCompatActivity {

    TextView mealname, serving, carbohydrate, proteintext, fat, calorie;
    EditText amount;
    String mealName, gram = " g", kcal = " kcal";
    String[] mealDetail;
    String miktar, porsiyon, karbonhidrat, protein, yag, kalori;
    Button addmeal;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        tanimla();
        doldur();
        newMealDetails();
        addMealDay();


    }

    public void tanimla() {
        Bundle data = getIntent().getExtras();
        mealName = data.getCharSequence("key").toString();
        mealDetail = data.getStringArray("mealDetailKey");
        miktar = mealDetail[1].replace('g', ' ').trim();
        miktar = miktar.replace("ml","");
        porsiyon = mealDetail[2];
        karbonhidrat = mealDetail[3].replace('g', ' ').trim();
        protein = mealDetail[4].replace('g', ' ').trim();
        yag = mealDetail[5].replace('g', ' ').trim();
        kalori = mealDetail[6].replace("kcal", " ").trim();


        mealname = (TextView) findViewById(R.id.mealName);
        amount = (EditText) findViewById(R.id.miktar);
        serving = (TextView) findViewById(R.id.porsiyon);
        carbohydrate = (TextView) findViewById(R.id.karbonhidrat);
        proteintext = (TextView) findViewById(R.id.protein);
        fat = (TextView) findViewById(R.id.yag);
        calorie = (TextView) findViewById(R.id.kalori);
        addmeal = (Button) findViewById(R.id.addMeal);
        realm = Realm.getDefaultInstance();

    }

    private void addMealDay() {
        addMealTable addMealTable = new addMealTable();
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat, Locale.getDefault());


        addmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Date today = Date.from(Instant.now());
                final String addMealName = mealname.getText().toString();
                final double addcarbonhydrat = Double.parseDouble(carbohydrate.getText().toString().replace('g', ' '));
                final double addprotein = Double.parseDouble(proteintext.getText().toString().replace('g', ' '));
                final double addfat = Double.parseDouble(fat.getText().toString().replace('g', ' '));
                final double addcalorie = Double.parseDouble(calorie.getText().toString().replace("kcal", " "));


                addMealSet(today, addMealName, addcarbonhydrat, addprotein, addfat, addcalorie);

            }
        });


    }

    private void addMealSet(Date today, String addMealName, double addcarbonhydrat, double addprotein, double addfat, double addcalorie) {

        double addMealAmount;
        if(amount.getText().toString().length()== 0 )
            addMealAmount = Double.parseDouble(miktar);
        else
            addMealAmount = Double.parseDouble(amount.getText().toString());
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                addMealTable addMealTable = realm.createObject(com.example.kalori.realm.addMealTable.class);
                addMealTable.setDay(today);
                addMealTable.setMealName(addMealName);
                addMealTable.setMealAmount(addMealAmount);
                addMealTable.setCarbonhydrat(addcarbonhydrat);
                addMealTable.setProtein(addprotein);
                addMealTable.setFat(addfat);
                addMealTable.setCalorie(addcalorie);


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "Eklendi: "+addMealName, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
//                Toast.makeText(getApplicationContext(), "hatali", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void doldur() {

        mealname.setText(mealName);

        amount.setHint(miktar);
        serving.setText(porsiyon);
//        carbohydrate.setText(karbonhidrat + gram);
//        proteintext.setText(protein + gram);
//        fat.setText(yag + gram);
//        calorie.setText(kalori + kcal);

    }

    public void newMealDetails() {
        valueUpdate(Double.parseDouble(miktar));
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(MealDetailActivity.this, "1. if", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(MealDetailActivity.this, "2. if", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(MealDetailActivity.this, "3. if", Toast.LENGTH_SHORT).show();
                if (editable.toString().length() == 0) {
                    double value = 0;
                    valueUpdate(Double.parseDouble(miktar));
                } else {
                    valueUpdate(Double.parseDouble(editable.toString()));
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void valueUpdate(double getNewMiktar) {
        double newKarbonhidrat;
        double newProtein;
        double newYag;
        double newKalori;

        newKarbonhidrat = Double.parseDouble(karbonhidrat) * (getNewMiktar / Double.parseDouble(miktar));
        newProtein = Double.parseDouble(protein) * (getNewMiktar / Double.parseDouble(miktar));       //yeni Gramaja göre değerlerin hesaplanması
        newYag = Double.parseDouble(yag) * (getNewMiktar / Double.parseDouble(miktar));
        newKalori = Double.parseDouble(kalori) * (getNewMiktar / Double.parseDouble(miktar));

        newKarbonhidrat = Math.round(newKarbonhidrat * 100.0) / 100.0;
        newProtein = Math.round(newProtein * 100.0) / 100.0;                                // Virgülden sonra 2 basamak göstermek için
        newYag = Math.round(newYag * 100.0) / 100.0;
        newKalori = Math.round(newKalori * 100.0) / 100.0;

        carbohydrate.setText(newKarbonhidrat + gram);
        proteintext.setText(newProtein + gram);                 //yeni değerleri yazdırmak için
        fat.setText(newYag + gram);
        calorie.setText(newKalori + kcal);


    }


}