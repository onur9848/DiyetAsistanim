package com.example.kalori;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.realm.userTable;
import io.realm.Realm;
import io.realm.RealmResults;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class newUser extends AppCompatActivity {

    Realm realm;
    final Calendar myCalendar = Calendar.getInstance();
    EditText Name, Surname, Height, Weight, Birthday;
    Button loginButton;


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
                final boolean user = true;
                final String name = Name.getText().toString();
                final String surname = Surname.getText().toString();
                final Double height = Double.parseDouble(Height.getText().toString());
                final Double weight = Double.parseDouble(Weight.getText().toString());
                final String birthday = Birthday.getText().toString();

                loginset(user, name, surname, height, weight, birthday);


            }
        });
    }

    public boolean checkFields() {
        if (Name.getText().toString().length() > 0 &&
                Surname.getText().toString().length() > 0 &&
                Height.getText().toString().length() > 0 &&
                Weight.getText().toString().length() > 0 &&
                Birthday.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    private void loginset(final boolean users, final String names, final String surnames, final Double heights, final Double weights, final String birthdays) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                userTable userTable = realm.createObject(com.example.kalori.realm.userTable.class);
                userTable.setUser(users);
                userTable.setDbname(names);
                userTable.setDbsurname(surnames);
                userTable.setDbheight(heights);
                userTable.setDbweight(weights);
                userTable.setDbbirthday(birthdays);

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