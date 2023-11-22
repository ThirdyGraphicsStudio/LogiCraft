package com.thirdy.booleanexpression.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryTaskTable {

    DatabaseHelper dbHelper;
    Context context;


    public HistoryTaskTable(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    // Insert data
    public void insertData(String expression, String minterms, int variables) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("expression", expression);
        values.put("minterms", minterms);
        values.put("variables", variables);

        db.insert("history", null, values);
    }

    // Query data
    public Cursor getData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM history ORDER BY date DESC", null);
    }



}
