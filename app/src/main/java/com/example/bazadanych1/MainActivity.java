package com.example.bazadanych1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _db = openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);
        Log.d("DB", "Baza utworzona");
        _db.execSQL("DROP TABLE IF EXISTS OSOBY;");
        _db.execSQL("CREATE TABLE OSOBY (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAZWISKO CHAR (20) NOT NULL," +
                "PESEL CHAR (9) NOT NULL," +
                "DATAUR DATE," +
                "WZROST REAL)");
        Log.d("DB", "Tabela utworzona");
        _db.execSQL("INSERT INTO OSOBY (NAZWISKO, PESEL, DATAUR, WZROST) VALUES (" +
                "'Kowalski', '123456789', '2016-01-09', 55.4)");
        _db.execSQL("INSERT INTO OSOBY (NAZWISKO, PESEL, DATAUR, WZROST) VALUES (" +
                "'Nowak', '123456788', '2016-01-08', 56.2)");
        Log.d("DB", "Rekordy dodane");

        Cursor cursor = _db.rawQuery("SELECT * FROM OSOBY", null);
        sendToLog(cursor);

        //select powyżej 56
        cursor = _db.rawQuery("SELECT * FROM OSOBY WHERE WZROST > 56", null);
        sendToLog(cursor);

        //Update
        _db.execSQL("UPDATE OSOBY SET WZROST = 57 WHERE ID = 2");
        cursor = _db.rawQuery("SELECT * FROM OSOBY", null);
        sendToLog(cursor);

        //Wyliczanie podsumowań
        cursor = _db.rawQuery("SELECT AVG(WZROST) FROM OSOBY", null);
        cursor.moveToFirst();
        double wzrost = cursor.getDouble(0);
        Log.d("DB", String.valueOf(wzrost));

        //Usuwanie danych
        _db.execSQL("DELETE FROM OSOBY WHERE WZROST < 56");
        cursor = _db.rawQuery("SELECT * FROM OSOBY", null);
        sendToLog(cursor);

        _db.close();
    }

    private void sendToLog(Cursor cursor) {
        while(cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nazwisko = cursor.getString(1);
            @SuppressLint("Range") String pesel = cursor.getString(cursor.getColumnIndex("PESEL"));
            @SuppressLint("Range") String dataUr = cursor.getString(cursor.getColumnIndex("DATAUR"));
            double wzrost = cursor.getDouble(4);
            Log.d("DB", id+"\t"+nazwisko+"\t"+pesel+"\t"+dataUr+"\t"+wzrost);
        }
    }
}