package com.example.kalori.activity;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.R;
import io.realm.Realm;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class newMealActivity extends AppCompatActivity {

    Realm realm;
    SearchView searchView;
    ListView listView;
    ArrayList<Meal> meals = new ArrayList<Meal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);
        listMeal();
        tanimla();
        listele(getStrings());
        search();



    }

    private void clickMeal(String[] meals) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(newMealActivity.this, meals[i], Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void search() {
        String[] mealName = getStrings();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.toString().equals("")) {
                    listele(mealName);
                } else {
                    ArrayList<String> newMeals = new ArrayList<String>();
                    for (int i = 0; i < mealName.length; i++) {
                        if (mealName[i].toLowerCase().indexOf(s) >= 0) {
                            newMeals.add(mealName[i]);
                        }

                    }
                    String[] newMealName = newMeals.toArray(new String[0]);
                    listele(newMealName);
                }
                return false;
            }
        });

    }

    private void listele(String[] mealName) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mealName);
        listView.setAdapter(adapter);
        clickMeal(mealName);


    }

    public String[] getStrings() {
        String[] mealName = new String[233];
        int i = 0;
        for (Meal meal : meals) {
            mealName[i] = meal.getMealName().toString();
            i++;
        }
        return mealName;
    }

    private void tanimla() {
        searchView = (SearchView) findViewById(R.id.searchMeal);
        listView = (ListView) findViewById(R.id.listMeal);
    }

    private void listMeal() {
        InputStream is = getResources().openRawResource(R.raw.test);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))) {

            String satir;
            int i = 0;
            while ((satir = bufferedReader.readLine()) != null || i == 232) {

                System.out.println(i + "-)" + satir);
                String[] newsatir = satir.split(";");
                Meal newmeal = new Meal();
                newmeal.setMealName(newsatir[0]);
                newmeal.setAmount(newsatir[1]);
                newmeal.setServing(newsatir[2]);
                newmeal.setCarbohydrate(newsatir[3]);
                newmeal.setProtein(newsatir[4]);
                newmeal.setFat(newsatir[5]);
                newmeal.setCalorie(newsatir[6]);
                meals.add(newmeal);


                i++;
            }

        } catch (IOException e) {
            Toast.makeText(newMealActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

class Meal {
    private String mealName;
    private String amount;
    private String serving;
    private String carbohydrate;
    private String protein;
    private String fat;
    private String calorie;

    public Meal(String mealName, String amount, String serving, String carbohydrate, String protein, String fat, String calorie) {

        this.mealName = mealName;
        this.amount = amount;
        this.serving = serving;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public Meal() {

    }
}