package com.joe.huaban.picdetail.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.joe.huaban.base.model.KathyParams;
import com.joe.huaban.global.Constant;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.picdetail.presenter.PicDetailListener;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 图片的收藏，保存，分享操作类
 * Created by Joe on 2016/4/18.
 */
public class PicKathy {
    private Context context;
    private PicDetailListener listener;
    private final String  TABLE="FavoritePic";
    private final String  IMG_URL="img_url";
    private final String  IMG_DESC="img_desc";
    private final String  IMG_WIDTH="img_width";
    private final String  IMG_HEIGHT="img_height";

    public PicKathy(Context context, PicDetailListener listener) {
        this.context = context;
        this.listener = listener;
    }

    //保存至数据库
    public void saveToFavorite(String img,String desc,int width,int height){
        if(alreadyLiked(img)){
            listener.onFavoriteError("已经收藏过咯");
            return;
        }
        FavoriteOpenHelper helper=new FavoriteOpenHelper(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(IMG_URL,img);
        values.put(IMG_DESC,desc);
        values.put(IMG_WIDTH,width);
        values.put(IMG_HEIGHT,height);
        db.insert(TABLE,null,values);
        listener.onFavoriteSuccess();
        if(db!=null){
            db.close();
            values.clear();
        }
    }

    //当前是否已经收藏过了
    private boolean alreadyLiked(String img) {
        FavoriteOpenHelper helper=new FavoriteOpenHelper(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.query(TABLE, null, IMG_URL+"=?", new String[]{img}, null, null, null);
        if(cursor!=null) {
            if (cursor.moveToNext()){
                return true;
            }
        }
        return false;
    }

    public void saveToPhone(String url){
        if(alreadySaved(url)) {
            listener.onSaveError("已经保存过咯");
            return;
        }
        RequestParams params= new RequestParams(Constant.HOST_PIC+url);
        params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/"+Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+url+".jpg");
        LogUtils.d(params.getSaveFilePath());
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<File>() {

            @Override
            public void onSuccess(File result) {
                listener.onSaveSuccess();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.d("请求失败："+ex);
                listener.onSaveError("哎呀~保存失败了");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private boolean alreadySaved(String url) {
        if(new File(Environment.getExternalStorageDirectory()+"/"+Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+url+".jpg").exists()) return true;
        return false;
    }
}
