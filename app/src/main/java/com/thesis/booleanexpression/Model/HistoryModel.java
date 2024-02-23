package com.thesis.booleanexpression.Model;


public class HistoryModel {
    int id;
    String date;
    String name;
    String minterms;
    int variable;

    boolean isHeader = false;


    public HistoryModel(int id, String date, String name, String minterms, int variable) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.minterms = minterms;
        this.variable = variable;
    }


    public String getMinterms() {
        return minterms;
    }

    public int getVariable() {
        return variable;
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
