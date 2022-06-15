package com.example.kalori.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.R;
import com.example.kalori.realm.addMealTable;
import com.example.kalori.realm.mealListTable;
import io.realm.Realm;

public class addNewMealActivity extends AppCompatActivity {
    EditText mealNameText, mealAmountText, mealServingText, mealCarbonhydratText, mealFatText, mealProteinText, mealCalorieText;
    Button addNewMealButton;
    Realm realm;
    mealListTable newMealListObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        realmTanimla();
        tanimla();
        setAddButton();
    }

    private void tanimla() {
        mealNameText = (EditText) findViewById(R.id.newMealName);
        mealAmountText = (EditText) findViewById(R.id.newMealAmount);
        mealServingText = (EditText) findViewById(R.id.newMealServing);
        mealCarbonhydratText = (EditText) findViewById(R.id.newMealCarbonhydrat);
        mealFatText = (EditText) findViewById(R.id.newMealFat);
        mealProteinText = (EditText) findViewById(R.id.newMealProtein);
        mealCalorieText = (EditText) findViewById(R.id.newMealCalorie);
        addNewMealButton = (Button) findViewById(R.id.newMealaddButton);
        newMealListObject = new mealListTable();
    }

    private void realmTanimla() {
        realm = Realm.getDefaultInstance();

    }

    private void setAddButton() {
        addNewMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mealName, serving;
                final double amount, carbonhydrat, fat, protein, calorie;
                mealName = mealNameText.getText().toString();
                amount = Double.parseDouble(mealAmountText.getText().toString());
                serving = mealServingText.getText().toString();
                carbonhydrat = Double.parseDouble(mealCarbonhydratText.getText().toString());
                fat = Double.parseDouble(mealFatText.getText().toString());
                protein= Double.parseDouble(mealProteinText.getText().toString());
                calorie = Double.parseDouble(mealCalorieText.getText().toString());

                newMealListObject.setMealName(mealName);
                newMealListObject.setAmount(amount);
                newMealListObject.setServing(serving);
                newMealListObject.setCarbohydrate(carbonhydrat);
                newMealListObject.setProtein(protein);
                newMealListObject.setFat(fat);
                newMealListObject.setCalorie(calorie);

                addMealListTable(newMealListObject);


            }
        });


    }

    private void addMealListTable(mealListTable object){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mealListTable table = realm.createObject(mealListTable.class);
                table.setMealName(object.getMealName());
                table.setAmount(object.getAmount());
                table.setServing(object.getServing());
                table.setCarbohydrate(object.getCarbohydrate());
                table.setProtein(object.getProtein());
                table.setFat(object.getFat());
                table.setCalorie(object.getCalorie());

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
}