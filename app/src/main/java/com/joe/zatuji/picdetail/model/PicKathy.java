package com.joe.zatuji.picdetail.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.joe.zatuji.favoritepage.model.FavoriteTag;
import com.joe.zatuji.favoritepage.model.MyFavorite;
import com.joe.zatuji.global.Constant;
import com.joe.zatuji.global.utils.LogUtils;
import com.joe.zatuji.loginpager.model.User;
import com.joe.zatuji.picdetail.presenter.PicDetailListener;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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

    //保存到用户收藏标签中
    public void addFavorite(String img, String desc, int width, int height, FavoriteTag tag,User user){
        MyFavorite favorite = new MyFavorite();
        favorite.setImg_url(Constant.HOST_PIC+img);
        favorite.setDesc(desc);
        favorite.setWidth(width);
        favorite.setHeight(height);
        favorite.setTag(new BmobRelation(tag));
        favorite.setUser(new BmobRelation(user));
        alreadyLiked(favorite,tag);
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
    private void alreadyLiked(final MyFavorite favorite, final FavoriteTag tag) {
        final boolean[] isAready = new boolean[1];
        BmobQuery<MyFavorite> query = new BmobQuery<MyFavorite>();
        query.addWhereEqualTo("img_url",favorite.getImg_url());
        query.addWhereEqualTo("tag",favorite.getTag());
        query.addWhereEqualTo("user",favorite.getUser());
        query.findObjects(context, new FindListener<MyFavorite>() {
            @Override
            public void onSuccess(List<MyFavorite> list) {
                LogUtils.d("查到："+list.size());
                if(list.size()>0) {
                    listener.onFavoriteError("已经收藏过咯");
                }else{
                    updateFavorite(favorite,tag);
                }
            }

            @Override
            public void onError(int i, String s) {
                updateFavorite(favorite,tag);
            }
        });
    }

    private void updateFavorite(MyFavorite favorite, final FavoriteTag tag) {
        tag.increment("number");
        if(TextUtils.isEmpty(tag.getFront())) tag.setFront(favorite.getImg_url());
        LogUtils.d("number:"+tag.getNumber()+":img:"+tag.getFront());
        favorite.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onFavoriteSuccess();
                tag.update(context);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFavoriteError("收藏失败");
            }
        });
    }

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
