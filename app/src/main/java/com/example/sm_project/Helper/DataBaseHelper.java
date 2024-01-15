package com.example.sm_project.Helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "Signup.db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "Signup.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase MyDataBase) {
        MyDataBase.execSQL("create Table allusers(email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
