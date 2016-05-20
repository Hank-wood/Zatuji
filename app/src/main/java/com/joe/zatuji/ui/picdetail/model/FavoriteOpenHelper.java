package com.joe.zatuji.ui.picdetail.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 我的收藏数据库
 * Created by Joe on 2016/4/18.
 */
public class FavoriteOpenHelper extends SQLiteOpenHelper{
    private Context mContext;
    private final String CREATE="create table FavoritePic" +
            "(id integer primary key autoincrement,"+
            "img_url text,"+
            "img_desc text,"+
            "img_width integer,"+
            "img_height integer"+
            ")";
    public FavoriteOpenHelper(Context context) {
        super(context, "MyFavorite", null, 1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
