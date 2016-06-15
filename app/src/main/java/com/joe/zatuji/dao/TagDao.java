package com.joe.zatuji.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.data.bean.FavoriteTag;

import java.util.ArrayList;

/**
 * Created by joe on 16/6/15.
 */
public class TagDao extends BaseDao {
    public static final String TAG_ID = "tag_id";//the url of pics
    public static final String USER_ID = "user_id";
    public static final String TAG = "tag";//the url of pics
    public static final String DESC = "desc";
    public static final String NUMBER = "number";
    public static final String IS_LOCK= "is_lock";//0false 1 true
    public static final String PWD = "pwd";
    public static final String FRONT ="front";

    public TagDao() {
        super(MyApplication.getInstance());
    }

    @Override
    protected String createSql() {
        createSql = "create table if not exists "
                +TABLE_TAG+" "+
                "(id integer primary key autoincrement,"+
                TAG_ID+" text,"+
                USER_ID+" text,"+
                TAG+" text,"+
                DESC+" text,"+
                NUMBER+" integer,"+
                IS_LOCK+" integer,"+
                PWD+" text,"+
                FRONT+" text"+
                ")";
        createTable();
        return createSql;
    }
    /**保存tag*/
    public void saveToCache(ArrayList<FavoriteTag> list){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (FavoriteTag tag:list) {
            if(!TextUtils.isEmpty(tag.user_id)){
                values.put(USER_ID,tag.user_id);
            }else{
                values.put(USER_ID,tag.belong.objectId);
            }
            values.put(TAG_ID,tag.objectId);
            values.put(TAG,tag.tag);
            values.put(DESC,tag.desc);
            values.put(NUMBER,tag.number);
            values.put(IS_LOCK,tag.is_lock?1:0);
            values.put(PWD,tag.pwd);
            values.put(FRONT,tag.front);
            db.insert(TABLE_TAG,null,values);
            values.clear();
        }
        close(db);
    }

    public ArrayList<FavoriteTag> queryTag(String user_id){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from "+TABLE_TAG+" where user_id = ? order by -"+NUMBER;
        Cursor cursor = db.rawQuery(sql,new String[]{user_id});
        ArrayList<FavoriteTag> tags = new ArrayList<>();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                FavoriteTag tag = new FavoriteTag();
                tag.user_id = cursor.getString(cursor.getColumnIndex(USER_ID));
                tag.objectId = cursor.getString(cursor.getColumnIndex(TAG_ID));
                tag.tag = cursor.getString(cursor.getColumnIndex(TAG));
                tag.desc = cursor.getString(cursor.getColumnIndex(DESC));
                tag.number = cursor.getInt(cursor.getColumnIndex(NUMBER));
                tag.is_lock = cursor.getInt(cursor.getColumnIndex(USER_ID))==1;
                tag.pwd = cursor.getString(cursor.getColumnIndex(PWD));
                tag.front = cursor.getString(cursor.getColumnIndex(FRONT));
                tags.add(tag);
            }
            cursor.close();
        }
        close(db);
        return tags;
    }

    public void clearCache(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(TABLE_TAG,null,null);
        close(db);
    }
}
