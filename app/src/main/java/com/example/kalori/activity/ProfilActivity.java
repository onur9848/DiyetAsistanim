package com.example.kalori.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kalori.MainActivity;
import com.example.kalori.R;
import com.example.kalori.newUser;
import com.example.kalori.realm.userTable;
import com.example.kalori.realm.weightHistory;
import io.realm.Realm;

import java.time.Instant;
import java.util.Date;

public class ProfilActivity extends AppCompatActivity {

    TextView profilisimtext, profilboytext, profilkilotext, profilbkitext, profildogumtext, profilidealkilotext, dialogyenikilotext;
    Button profilkilobuton, yenikilokaydet;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        tanimla();
        doldur();
        changeWeight();

    }

    public void onResume() {

        super.onResume();
        doldur();
    }


    private void tanimla() {
        realm = Realm.getDefaultInstance();
        getSupportActionBar().setTitle("Profil");
        profilisimtext = (TextView) findViewById(R.id.profilUserNameSurname);
        profilboytext = (TextView) findViewById(R.id.profilBoy);
        profilkilotext = (TextView) findViewById(R.id.profilKilo);
        profilbkitext = (TextView) findViewById(R.id.profilbki);
        profildogumtext = (TextView) findViewById(R.id.profilDogumTarihi);
        profilidealkilotext = (TextView) findViewById(R.id.profilIdealKilo);
        profilkilobuton = (Button) findViewById(R.id.profilKiloDegis);

    }

    private void doldur() {

        userTable usertable = realm.where(userTable.class).findFirst();
        assert usertable != null;
        String isim, boy, kilo, bki, dogum, idealkilo;
        double bkihesap = usertable.getDbweight() / (Math.pow(usertable.getDbheight() / 100, 2));
        double idealkilohesap = 50 + 2.3 * (usertable.getDbheight() / 2.54 - 60);
        isim = usertable.getDbname() + " " + usertable.getDbsurname();
        boy = "Boyunuz: " + usertable.getDbheight();
        kilo = "Kilonuz: " + usertable.getDbweight();
        bki = "BKI: " + yuvarlama(bkihesap);
        dogum = "Doğum Tarihiniz: " + usertable.getDbbirthday().toString();
        idealkilo = "İdeal Kilonuz: " + yuvarlama(idealkilohesap);
        profilisimtext.setText(isim);
        profilboytext.setText(boy);
        profilkilotext.setText(kilo);
        profilbkitext.setText(bki);
        profildogumtext.setText(dogum);
        profilidealkilotext.setText(idealkilo);

    }

    double yuvarlama(double sayi) {
        sayi = Math.round(sayi * 100.0) / 100.0;
        return sayi;
    }

    public void changeWeight() {
        profilkilobuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_weight_diaolog);
        dialogyenikilotext = (TextView) dialog.findViewById(R.id.yeniKiloText);
        yenikilokaydet = (Button) dialog.findViewById(R.id.yeniKilokaydet);
        dialog.show();
        yenikilokaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date datenow = Date.from(Instant.now());
                userTable userTable = realm.where(com.example.kalori.realm.userTable.class).findFirst();
                realm.beginTransaction();
                double yenikilo = Double.parseDouble(dialogyenikilotext.getText().toString());
                userTable.setDbweight(yenikilo);
                realm.commitTransaction();
                kiloekle(yenikilo,datenow);
                doldur();
                dialog.hide();
            }
        });
    }

    public void kiloekle(double kilo,Date tarih){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                weightHistory weightHistory = realm.createObject(com.example.kalori.realm.weightHistory.class);
                
                weightHistory.setWeight(kilo);
                weightHistory.setDate(tarih);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(ProfilActivity.this, "başarılı", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "hatali", Toast.LENGTH_SHORT).show();
            }
        });
        
        
    }

}

