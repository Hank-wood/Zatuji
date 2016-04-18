package com.joe.huaban.favoritepage.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joe.huaban.base.model.PicData;
import com.joe.huaban.homepage.presenter.HomeDataListener;
import com.joe.huaban.picdetail.model.FavoriteOpenHelper;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/4/19.
 */
public class FavoriteKathy {
    private final String  TABLE="FavoritePic";
    private final String  IMG_URL="img_url";
    private final String  IMG_DESC="img_desc";
    private final String  IMG_WIDTH="img_width";
    private final String  IMG_HEIGHT="img_height";
    private Context context;
    private HomeDataListener listener;
    public FavoriteKathy(Context context, HomeDataListener listener){
        this.context=context;
        this.listener=listener;
    }

    public void getFavoriteData(int page,int limit){
        FavoriteOpenHelper helper=new FavoriteOpenHelper(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql = "select * from FavoritePic limit "+(page-1)*limit+","+limit;
        LogUtil.d(sql);
        Cursor cursor=db.rawQuery(sql, null);
        PicData picData=new PicData();
        picData.pins=new ArrayList<PicData.PinsBean>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                PicData.PinsBean pin=new PicData.PinsBean();

                pin.file=new PicData.PinsBean.FileBean();
                pin.raw_text=cursor.getString(cursor.getColumnIndex(IMG_DESC));
                pin.file.key=cursor.getString(cursor.getColumnIndex(IMG_URL));
                pin.file.width=cursor.getInt(cursor.getColumnIndex(IMG_WIDTH));
                pin.file.height=cursor.getInt(cursor.getColumnIndex(IMG_HEIGHT));
                picData.pins.add(pin);
            }
        }
        if(picData.pins.size()<=0){
            listener.onError(null,false);
        }else{
            if(page>1){
                listener.onSuccess(picData,true);
            }else{
                listener.onSuccess(picData,false);
            }
        }
        if(db!=null){
            cursor.close();
            db.close();
        }
    }
}
