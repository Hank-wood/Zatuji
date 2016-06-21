package com.joe.zatuji.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.LogUtils;

/**
 * Created by joe on 16/5/21.
 */
public abstract class BaseDao {
    /******************** 建表字段 ********************/
    public static final String TABLE_PIC_CACHES = "pic_caches";//图片数据缓存表
    public static final String TABLE_FAVORITE = "favorite";//收藏表
    public static final String TABLE_TAG = "tag";//图集表
    /******************** end ********************/

    protected BaseOpenHelper mHelper;
    protected Context context;
    protected String createSql="";
    public BaseDao(Context context){
        this.context = context;
        mHelper = new BaseOpenHelper(context);
        createSql();
    }
    protected void createTable(){
        mHelper.getWritableDatabase().execSQL(createSql);
    }
    protected abstract String createSql();


    class BaseOpenHelper extends SQLiteOpenHelper {

        public BaseOpenHelper(Context context) {
            super(context, Constant.DATABASE, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createSql());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    protected void close(SQLiteDatabase db){
        if(db!=null) db.close();
    }

    public boolean tabbleIsExist(String tableName){
        boolean result = false;
        if(tableName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mHelper.getReadableDatabase();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+tableName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }
}
