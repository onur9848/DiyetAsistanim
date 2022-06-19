package com.example.DiyetAsistanim.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.DiyetAsistanim.R;
import com.example.DiyetAsistanim.realm.userTable;
import com.example.DiyetAsistanim.realm.weightHistory;
import io.realm.Realm;

import java.time.Instant;
import java.util.Date;

public class ProfilActivity extends AppCompatActivity {

    TextView profilisimtext, profilboytext, profilkilotext, profilbkitext, profildogumtext, profilidealkilotext, dialogyenikilotext, profilcinsiyettext;
    TextView profilkiyaslamatext;
    Button profilkilobuton, yenikilokaydet;
    RadioGroup radioGroup;
    Realm realm;
    String isim_text, boy_text, kilo_text, bki_text, dogum_text, idealkilo_text, cinsiyet_text;
    double boy_double, kilo_double, bki_double, idealkilo_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        tanimla();
        doldur();
        changeWeight();
        kiyaslama();

    }

    private String bkiText(double bki){
        if (bki<20)
            return "Zayıf";
        else if (bki<24.9)
            return "Normal";
        else if (bki<29.9)
            return "Hafif şişman";
        else if (bki<34.9)
            return "I. Derece Obez";
        else if (bki<39.9)
            return "II. Derece Obez";
        else
            return "III. Derece Obez";
    }

    @SuppressLint("SetTextI18n")
    private void kiyaslama() {
        if (idealkilo_double-2 >kilo_double) {
            profilkiyaslamatext.setText("İdeal kilonuzun Altındasınız.");
            profilkiyaslamatext.setBackgroundColor(Color.BLUE);
            profilkiyaslamatext.setTextColor(Color.YELLOW);
        }
        else if (idealkilo_double+2>kilo_double && kilo_double>idealkilo_double-2){
            profilkiyaslamatext.setText("Tebrikler ideal kilonuzdasınız.");
            profilkiyaslamatext.setBackgroundColor(Color.GREEN);
            profilkiyaslamatext.setTextColor(Color.RED);

        }
        else {
            profilkiyaslamatext.setText("İdeal kilonuzun üstündesiniz.");
            profilkiyaslamatext.setBackgroundColor(Color.RED);
            profilkiyaslamatext.setTextColor(Color.BLACK);
        }

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
        profilcinsiyettext = (TextView) findViewById(R.id.profilCinsiyet);
        profilkilobuton = (Button) findViewById(R.id.profilKiloDegis);
        radioGroup = findViewById(R.id.sexRadioButtonUser);
        profilkiyaslamatext = findViewById(R.id.profilkıyaslamaText);


    }

    private void doldur() {

        userTable usertable = realm.where(userTable.class).findFirst();
        assert usertable != null;
        boy_double = usertable.getDbheight();
        kilo_double = usertable.getDbweight();
        bki_double = usertable.getDbweight() / (Math.pow(usertable.getDbheight() / 100, 2));
        idealkilo_double = idealkilohesaplama(usertable.getDbheight(), usertable.getDbcinsiyet());

        isim_text = usertable.getDbname() + " " + usertable.getDbsurname();
        boy_text = "Boyunuz: " + boy_double;
        kilo_text = "Kilonuz: " + kilo_double;
        bki_text = "BKI: " + yuvarlama(bki_double)+"\n("+bkiText(bki_double)+")";
        dogum_text = "Doğum Tarihiniz: " + usertable.getDbbirthday().toString();
        idealkilo_text = "İdeal Kilonuz: " + yuvarlama(idealkilo_double);
        cinsiyet_text = "Cinsiyet: " + usertable.getDbcinsiyet();

        profilisimtext.setText(isim_text);
        profilboytext.setText(boy_text);
        profilkilotext.setText(kilo_text);
        profilbkitext.setText(bki_text);
        profildogumtext.setText(dogum_text);
        profilidealkilotext.setText(idealkilo_text);
        profilcinsiyettext.setText(cinsiyet_text);

    }

    double idealkilohesaplama(double boy, String cinsiyet) {
        double ideal;
        String bool = "Erkek";
        if (bool.equals(cinsiyet))
            ideal = 50 + 2.3 * ((boy / 2.54) - 60);
        else
            ideal = 45.5 + 2.3 * ((boy / 2.54) - 60);

        return ideal;
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
                userTable userTable = realm.where(com.example.DiyetAsistanim.realm.userTable.class).findFirst();
                realm.beginTransaction();
                double yenikilo = Double.parseDouble(dialogyenikilotext.getText().toString());
                userTable.setDbweight(yenikilo);
                realm.commitTransaction();
                kiloekle(yenikilo, datenow);
                doldur();
                kiyaslama();
                dialog.hide();
            }
        });
    }

    public void kiloekle(double kilo, Date tarih) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                weightHistory weightHistory = realm.createObject(com.example.DiyetAsistanim.realm.weightHistory.class);

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

