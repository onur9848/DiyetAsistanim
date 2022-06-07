package com.example.kalori.activity;

import android.bluetooth.le.ScanFilter;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.R;

public class MealDetailActivity extends AppCompatActivity {

    TextView mealname,serving,carbohydrate,proteintext,fat,calorie;
    EditText amount;
    String mealName;
    String[] mealDetail;
    String miktar, porsiyon, karbonhidrat, protein, yag, kalori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);
        tanimla();
        doldur();

    }

    public void doldur() {

        mealname.setText(mealName);
        amount.setText(miktar);
        serving.setText(porsiyon);
        carbohydrate.setText(karbonhidrat);
        proteintext.setText(protein);
        fat.setText(yag);
        calorie.setText(kalori);

    }

    public void tanimla() {
        Bundle data = getIntent().getExtras();
        mealName = data.getCharSequence("key").toString();
        mealDetail = data.getStringArray("mealDetailKey");
        miktar = mealDetail[1].replace('g', ' ').trim();
        porsiyon = mealDetail[2];
        karbonhidrat = mealDetail[3].replace('g', ' ').trim();
        protein = mealDetail[4].replace('g', ' ').trim();
        yag = mealDetail[5].replace('g', ' ').trim();
        kalori = mealDetail[6].replace("kcal", " ").trim();


        mealname = (TextView) findViewById(R.id.mealName);
        amount = (EditText) findViewById(R.id.miktar);
        serving = (TextView) findViewById(R.id.porsiyon);
        carbohydrate= (TextView) findViewById(R.id.karbonhidrat);
        proteintext = (TextView) findViewById(R.id.protein);
        fat =( TextView) findViewById(R.id.yag);
        calorie = (TextView) findViewById(R.id.kalori);

    }


}