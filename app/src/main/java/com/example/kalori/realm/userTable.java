package com.example.kalori.realm;

import io.realm.RealmObject;

public class userTable extends RealmObject {
    private Boolean user;
    private String dbname;
    private String dbsurname;
    private double dbheight;
    private double dbweight;
    private String dbbirthday;

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


}
