package com.joe.zatuji.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joe.zatuji.Constant;

/**
 * Created by joe on 16/5/21.
 */
public class BaseOpenHelper extends SQLiteOpenHelper{
    public String createSql = "";

    public BaseOpenHelper(Context context) {
        super(context, Constant.DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
