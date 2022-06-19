package com.example.DiyetAsistanim.activity;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.DiyetAsistanim.R;
import com.example.DiyetAsistanim.realm.mealListTable;
import io.realm.Realm;
import io.realm.RealmResults;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class newMealActivity extends AppCompatActivity {

    Realm realm;
    SearchView searchView;
    ImageView addButton;
    ListView listView;
    ArrayList<Meal> mealsArray = new ArrayList<Meal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);
        realmTanimla();
        tanimla();
        realmControlState();



    }

    private void realmControlState() {
        if (realm.where(mealListTable.class).findAll().isEmpty()){
            getCsvtoList();
            realmAdd();
        }
        else {
            listele(getStrings());
            search();
            addNewMeal();
        }

    }

    private void addNewMeal() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(newMealActivity.this, addNewMealActivity.class);
                startActivity(intent);
            }
        });
    }

    private void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    private void deleteAllDailyMealTable() {
        RealmResults<mealListTable> table = realm.where(mealListTable.class).findAll();
        realm.beginTransaction();
        for (mealListTable tables : table) {
            tables.deleteFromRealm();

        }
        realm.commitTransaction();
    }

    private void realmAdd() {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < mealsArray.size(); i++) {
                    mealListTable table = realm.createObject(mealListTable.class);
                    String mealname, serving, stAmount;
                    double calorie, protein, carbonhydrat, fat, amount;
                    mealname = mealsArray.get(i).getMealName();
                    serving = mealsArray.get(i).getServing();
                    stAmount = mealsArray.get(i).getAmount().replaceAll("g", "");
                    stAmount = stAmount.replace("ml", "");
                    amount = Double.parseDouble(stAmount);
                    carbonhydrat = Double.parseDouble(mealsArray.get(i).getCarbohydrate().replaceAll("g", ""));
                    protein = Double.parseDouble(mealsArray.get(i).getProtein().replaceAll("g", ""));
                    fat = Double.parseDouble(mealsArray.get(i).getFat().replaceAll("g", ""));
                    calorie = Double.parseDouble(mealsArray.get(i).getCalorie().replaceAll("kcal", ""));

                    table.setMealName(mealname);
                    table.setAmount(amount);
                    table.setServing(serving);
                    table.setCarbohydrate(carbonhydrat);
                    table.setProtein(protein);
                    table.setFat(fat);
                    table.setCalorie(calorie);
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(newMealActivity.this, "listele", Toast.LENGTH_SHORT).show();
                listele(getStrings());
                search();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });


    }

    private void clickMeal(String[] meals) {

        RealmResults<mealListTable> tables =realm.where(mealListTable.class).findAll();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(newMealActivity.this, meals[i], Toast.LENGTH_SHORT).show();
                String getMealName = meals[i];
                Intent intent = new Intent(newMealActivity.this, MealDetailActivity.class);
                mealListTable MealDetail = GetMealDetails(getMealName);
                intent.putExtra("mealDetailKey", MealDetail.getMealName());
                startActivity(intent);

            }
        });

    }

    private mealListTable GetMealDetails(String getMealName) {
        String[] allindexdata = getStrings();
        String[] mealDetail = new String[7];

        int i;
        for (i = 0; i < allindexdata.length; i++) {
            String s = allindexdata[i];
            if (s.equals(getMealName))
                break;
        }
        mealListTable mealDetails = realm.where(mealListTable.class).findAll().get(i);

        return mealDetails;
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
        String[] mealName = new String[realm.where(mealListTable.class).findAll().size()];
        int i = 0;
        RealmResults<mealListTable> list = realm.where(mealListTable.class).findAll();
        for (mealListTable object : list) {
            mealName[i] = object.getMealName();
            i++;
        }
        return mealName;
    }

    private void tanimla() {
        searchView = (SearchView) findViewById(R.id.searchMeal);
        listView = (ListView) findViewById(R.id.listMeal);
        addButton = (ImageView) findViewById(R.id.addNewMealButton);
    }

    void getCsvtoList() {
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
                mealsArray.add(newmeal);


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