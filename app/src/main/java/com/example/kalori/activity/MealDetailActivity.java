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
import com.example.kalori.realm.dailyMacroDetailTable;
import com.example.kalori.realm.mealListTable;
import io.realm.Realm;
import io.realm.RealmResults;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class MealDetailActivity extends AppCompatActivity {

    TextView mealname, serving, carbohydrate, proteintext, fat, calorie;
    EditText amount;
    String gram = " g", kcal = " kcal";
    String selectMeal;
    Button addmeal;
    Realm realm;
    RealmResults<mealListTable> mealList;
    mealListTable selectMealObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        tanimla();
        getMealIndexData();
        doldur();
        newMealDetails();
        addMealDay();


    }

    public void tanimla() {
        realm = Realm.getDefaultInstance();
        Bundle data = getIntent().getExtras();
        selectMeal = data.getString("mealDetailKey");
        mealList = realm.where(mealListTable.class).findAll();
        selectMealObject = new mealListTable();
        
        mealname = (TextView) findViewById(R.id.mealName);
        amount = (EditText) findViewById(R.id.miktar);
        serving = (TextView) findViewById(R.id.porsiyon);
        carbohydrate = (TextView) findViewById(R.id.karbonhidrat);
        proteintext = (TextView) findViewById(R.id.protein);
        fat = (TextView) findViewById(R.id.yag);
        calorie = (TextView) findViewById(R.id.kalori);
        addmeal = (Button) findViewById(R.id.addMeal);


    }
    private void getMealIndexData(){
        for (mealListTable object:mealList) {
            if (selectMeal.equals(object.getMealName())){
                selectMealObject.setMealName(object.getMealName());
                selectMealObject.setAmount(object.getAmount());
                selectMealObject.setServing(object.getServing());
                selectMealObject.setCarbohydrate(object.getCarbohydrate());
                selectMealObject.setProtein(object.getProtein());
                selectMealObject.setFat(object.getFat());
                selectMealObject.setCalorie(object.getCalorie());
                break;
            }
            
            
        }
    }

    private void addMealDay() {
        
        String myFormat = "MM/dd/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(myFormat, Locale.getDefault());


        addmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealTable addMealTable = new addMealTable();
                
                final Date today = Date.from(Instant.now());
                final String addMealName = mealname.getText().toString();
                final double addMealAmount = getAddMealAmount();
                final double addcarbonhydrat = Double.parseDouble(carbohydrate.getText().toString().replace('g', ' '));
                final double addprotein = Double.parseDouble(proteintext.getText().toString().replace('g', ' '));
                final double addfat = Double.parseDouble(fat.getText().toString().replace('g', ' '));
                final double addcalorie = Double.parseDouble(calorie.getText().toString().replace("kcal", " "));
               addMealTable.setDay(today);
               addMealTable.setMealName(addMealName);
               addMealTable.setMealAmount(addMealAmount);
               addMealTable.setCarbonhydrat(addcarbonhydrat);
               addMealTable.setProtein(addprotein);
               addMealTable.setFat(addfat);
               addMealTable.setCalorie(addcalorie);
                
                


                addMealSet(addMealTable);

            }
        });


    }

    private void addMealSet(addMealTable object) {


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                double totalcalorie = 0 , totalprotein=0,totalcarbonhydrat=0,totalfat=0;
                Date date = Date.from(Instant.now());
                addMealTable addMealTable = realm.createObject(com.example.kalori.realm.addMealTable.class);
//                addMealTable=object;
                addMealTable.setDay(object.getDay());
                addMealTable.setMealName(object.getMealName());
                addMealTable.setMealAmount(object.getMealAmount());
                addMealTable.setCarbonhydrat(object.getCarbonhydrat());
                addMealTable.setProtein(object.getProtein());
                addMealTable.setFat(object.getFat());
                addMealTable.setCalorie(object.getCalorie());
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


    private double getAddMealAmount() {
        double addMealAmount;
        if(amount.getText().toString().length()== 0 )
            addMealAmount = selectMealObject.getAmount();
        else
            addMealAmount = Double.parseDouble(amount.getText().toString());
        return addMealAmount;
    }

    @SuppressLint("SetTextI18n")
    public void doldur() {

        mealname.setText(selectMealObject.getMealName());

        amount.setHint(selectMealObject.getAmount()+"");
        serving.setText(selectMealObject.getServing());
//        carbohydrate.setText(karbonhidrat + gram);
//        proteintext.setText(protein + gram);
//        fat.setText(yag + gram);
//        calorie.setText(kalori + kcal);

    }

    public void newMealDetails() {
        valueUpdate(selectMealObject.getAmount());
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
                    valueUpdate(selectMealObject.getAmount());
                } else {
                    valueUpdate(Double.parseDouble(editable.toString()));
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void valueUpdate(double getNewMiktar) {
        double newKarbonhidrat,karbonhidrat=selectMealObject.getCarbohydrate();
        double newProtein,protein=selectMealObject.getProtein();
        double newYag,yag=selectMealObject.getFat();
        double newKalori,kalori=selectMealObject.getCalorie();
        double miktar =selectMealObject.getAmount();

        newKarbonhidrat = karbonhidrat * (getNewMiktar / miktar);
        newProtein = protein * (getNewMiktar / miktar);       //yeni Gramaja göre değerlerin hesaplanması
        newYag = yag * (getNewMiktar / miktar);
        newKalori = kalori * (getNewMiktar / miktar);

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