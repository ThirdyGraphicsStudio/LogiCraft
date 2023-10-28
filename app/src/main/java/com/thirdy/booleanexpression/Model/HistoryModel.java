package com.thirdy.booleanexpression.Model;


import java.util.Date;

public class HistoryModel {
    int id;
    String date;
    String name;
    boolean isHeader = false;


    public HistoryModel(int id, String date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHeader() {
        return isHeader;
    }
}
