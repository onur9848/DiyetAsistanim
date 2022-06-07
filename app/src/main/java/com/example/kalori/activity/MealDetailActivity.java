package com.example.kalori.activity;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.R;

public class MealDetailActivity extends AppCompatActivity {
    Bundle data = getIntent().getExtras();
    TextView text;
    String mealName = data.getCharSequence("key").toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

    }





}