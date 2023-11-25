package com.example.economyplanner;

import java.util.Date;

public class Item {

    String name;
    Boolean isDone;
    String date;



    public Item(String name, String date, Boolean isDone) {
        this.name = name;
        this.date = date;
        this.isDone = isDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

}