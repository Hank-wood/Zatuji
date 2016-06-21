package com.joe.zatuji.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 缓存数据库操作类
 * 缓存一次浏览数据，第二次缓存时清空上一次缓存数据
 * Created by joe on 16/5/21.
 */
public class FavoriteImgDao extends BaseDao{
    //表字段
    public static final String PIN_ID = "pin_id";
    public static final String KEY = "key";//the url of pics
    public static final String RAW_TEXT = "raw_text";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String TAG = "tag";
    public static final String CREATE = "create_date";
    public static final String TYPE ="type";

    public FavoriteImgDao(Context context) {
        super(context);
    }
    public FavoriteImgDao(){
        super(MyApplication.getInstance());
        if(!tabbleIsExist(TABLE_PIC_CACHES)) createTable();
    }
    @Override
    protected String createSql() {
        createSql = "create table if not exists "
                +TABLE_PIC_CACHES+" "+
                "(id integer primary key autoincrement,"+
                PIN_ID+" text,"+
                KEY+" text,"+
                RAW_TEXT+" text,"+
                WIDTH+" integer,"+
                HEIGHT+" integer,"+
                TAG+" text,"+
                TYPE+" text,"+
                CREATE+" datetime"+
                ")";
        return createSql;
    }


    //查询数据
    private DataBean queryPicCache(String tag, int limit, long offset){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from "+TABLE_PIC_CACHES+" where tag = ? order by -"+CREATE+" limit "+offset+","+limit;
        Cursor cursor = db.rawQuery(sql,new String[]{tag});
        DataBean dataBean = new DataBean();
        dataBean.pins = new ArrayList<DataBean.PicBean>();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                DataBean.PicBean picBean = new DataBean.PicBean();
                LogUtils.d("cache tag:"+cursor.getString(cursor.getColumnIndex(TAG)));
                picBean.file = new DataBean.PicBean.FileBean();
                picBean.pin_id = cursor.getString(cursor.getColumnIndex(PIN_ID));
                picBean.file.key = cursor.getString(cursor.getColumnIndex(KEY));
                picBean.file.width = cursor.getInt(cursor.getColumnIndex(WIDTH));
                picBean.file.height = cursor.getInt(cursor.getColumnIndex(HEIGHT));
                picBean.file.type = cursor.getString(cursor.getColumnIndex(TYPE));
                picBean.raw_text = cursor.getString(cursor.getColumnIndex(RAW_TEXT));
                dataBean.pins.add(picBean);
            }
            cursor.close();
        }
        close(db);
        return dataBean;
    }

    /**
     * 缓存数据
     * params picBeanList
     */
    public void intoCache(List<DataBean.PicBean> picDataList,String tag){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Date date = new Date(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        for (DataBean.PicBean pic:picDataList) {
            values.put(PIN_ID,pic.pin_id);
            values.put(KEY,pic.file.key);
            values.put(RAW_TEXT,pic.raw_text);
            values.put(WIDTH,pic.file.width);
            values.put(HEIGHT,pic.file.height);
            values.put(TAG,tag);
            values.put(TYPE,pic.file.type);
            values.put(CREATE,date.toString());
            db.insert(TABLE_PIC_CACHES,null,values);
            values.clear();
        }
        close(db);
    }


    /**
     * 获取首页缓存数据
     * params limit
     * params offset
     */
    public DataBean getHomeCache(int limit,long offset){
        return queryPicCache("home",limit,offset);
    }

    /**
     * 获取发现页缓存数据
     * params tag
     * params limit
     * params offset
     */
    public DataBean getDiscoverCache(String tag,int limit,long offset){
        return queryPicCache(tag,limit,offset);
    }


    /**
     * 清空缓存数据
     */
    public void clearCache(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(TABLE_PIC_CACHES,null,null);
        close(db);
    }

    public void clearTagCache(String tag){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TABLE_PIC_CACHES,TAG+"=?",new String[]{tag});
        close(db);
    }

}
