package com.example.kalori.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.MainActivity;
import com.example.kalori.R;
import com.example.kalori.realm.userTable;
import com.example.kalori.realm.weightHistory;
import io.realm.Realm;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newUser extends AppCompatActivity {

    Realm realm;
    final Calendar myCalendar = Calendar.getInstance();
    EditText Name, Surname, Height, Weight, Birthday;
    Button loginButton;
    RadioGroup cinsiyetRadioButton;
    RadioButton selectedSex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        tanimla();
        islevler();
        login();

    }

    public void islevler() {
        date();


    }

    public void date() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(newUser.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void tanimla() {
        Name = (EditText) findViewById(R.id.name);
        Surname = (EditText) findViewById(R.id.surname);
        Height = (EditText) findViewById(R.id.height);
        Weight = (EditText) findViewById(R.id.weight);
        Birthday = (EditText) findViewById(R.id.birthday);
        loginButton = (Button) findViewById(R.id.login);
        cinsiyetRadioButton = (RadioGroup) findViewById(R.id.sexRadioButtonUser);
        realm = Realm.getDefaultInstance();

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.getDefault());
        Birthday.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void login() {


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = cinsiyetRadioButton.getCheckedRadioButtonId();
                selectedSex = findViewById(selectId);
                if (checkFields()) {
                    final boolean user = true;
                    final String name = Name.getText().toString();
                    final String surname = Surname.getText().toString();
                    final Double height = Double.parseDouble(Height.getText().toString());
                    final Double weight = Double.parseDouble(Weight.getText().toString());
                    final String birthday = Birthday.getText().toString();
                    final String cinsiyet = selectedSex.getText().toString();
                    loginset(user, name, surname, height, weight, birthday, cinsiyet);
                } else
                    Toast.makeText(newUser.this, "Lütfen Tüm kısımları doldurunuz", Toast.LENGTH_SHORT).show();


            }
        });
    }

    public boolean checkFields() {
        if (!Name.getText().toString().isEmpty() &&
                !Surname.getText().toString().isEmpty() &&
                !Height.getText().toString().isEmpty() &&
                !Weight.getText().toString().isEmpty() &&
                !Birthday.getText().toString().isEmpty() &&
                cinsiyetRadioButton.getCheckedRadioButtonId() != -1) {
            Toast.makeText(this, "true döndü", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "false döndü", Toast.LENGTH_SHORT).show();
            return false;
        }
//        String deneme = "Name: "+Name.getText().toString().isEmpty()+
//                "\nsurname: "+ Surname.getText().toString().isEmpty()+
//                "\nHeight: "+Height.getText().toString().isEmpty()+
//                "\nWeight: "+Weight.getText().toString().isEmpty()+
//                "\nbirdhday: "+Birthday.getText().toString().isEmpty()+
//                "\nCinsiyet: "+cinsiyetRadioButton.isActivated();
//        Toast.makeText(this, deneme, Toast.LENGTH_LONG).show();
//        return false;
    }

    private void loginset(final boolean users, final String names, final String surnames, final Double heights, final Double weights, final String birthdays, final String cinsiyet) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Date datenow = Date.from(Instant.now());
                userTable userTable = realm.createObject(com.example.kalori.realm.userTable.class);
                weightHistory weightHistory = realm.createObject(com.example.kalori.realm.weightHistory.class);
                userTable.setUser(users);
                userTable.setDbname(names);
                userTable.setDbsurname(surnames);
                userTable.setDbheight(heights);
                userTable.setDbweight(weights);
                userTable.setDbbirthday(birthdays);
                userTable.setDbcinsiyet(cinsiyet);
                weightHistory.setWeight(weights);
                weightHistory.setDate(datenow);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(newUser.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "hatali", Toast.LENGTH_SHORT).show();
            }
        });

    }


}