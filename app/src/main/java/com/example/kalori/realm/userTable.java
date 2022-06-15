package com.example.kalori.realm;

import io.realm.RealmObject;

public class userTable extends RealmObject {
    private Boolean user;
    private String dbname;
    private String dbsurname;
    private double dbheight;
    private double dbweight;
    private String dbbirthday;
    private String dbcinsiyet; // "Erkek","Kadın"
    private int tercih; //1-kilo verme, 2-sabit kilo, 3-kilo alma.

    public userTable(Boolean user, String dbname, String dbsurname, double dbheight, double dbweight, String dbbirthday, String dbcinsiyet, int tercih) {
        this.user = user;
        this.dbname = dbname;
        this.dbsurname = dbsurname;
        this.dbheight = dbheight;
        this.dbweight = dbweight;
        this.dbbirthday = dbbirthday;
        this.dbcinsiyet = dbcinsiyet;
        this.tercih = tercih;
    }

    public userTable() {
    }

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbsurname() {
        return dbsurname;
    }

    public void setDbsurname(String dbsurname) {
        this.dbsurname = dbsurname;
    }

    public double getDbheight() {
        return dbheight;
    }

    public void setDbheight(double dbheight) {
        this.dbheight = dbheight;
    }

    public double getDbweight() {
        return dbweight;
    }

    public void setDbweight(double dbweight) {
        this.dbweight = dbweight;
    }

    public String getDbbirthday() {
        return dbbirthday;
    }

    public void setDbbirthday(String dbbirthday) {
        this.dbbirthday = dbbirthday;
    }


    public String getDbcinsiyet() {
        return dbcinsiyet;
    }

    public void setDbcinsiyet(String dbcinsiyet) {
        this.dbcinsiyet = dbcinsiyet;
    }

    public int getTercih() {
        return tercih;
    }

    public void setTercih(int tercih) {
        this.tercih = tercih;
    }
}
