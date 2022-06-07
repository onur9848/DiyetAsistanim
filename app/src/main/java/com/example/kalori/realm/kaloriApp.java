package com.example.kalori.realm;


import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class kaloriApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config =new RealmConfiguration.Builder().name("kilocalori.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
