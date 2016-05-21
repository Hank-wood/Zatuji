package com.joe.zatuji.ui.picdetail.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.joe.zatuji.ui.favoritepage.model.FavoriteTag;
import com.joe.zatuji.ui.favoritepage.model.MyFavorite;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.ui.loginpage.model.User;
import com.joe.zatuji.ui.picdetail.presenter.PicDetailListener;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    /**
     * 添加收藏时先查询是否已经收藏
     * 先判断该图片是否已经在数据库中，如果在直接setRelation到标签中
     * 如果数据库中没有该对象，则创建该对象再setRelation
    */
    //保存到用户收藏标签中
    public void addFavorite(String img, String desc, int width, int height, FavoriteTag tag,User user){
        MyFavorite favorite = new MyFavorite();
        favorite.setImg_url(Constant.HOST_PIC+img);
        favorite.setDesc(desc);
        favorite.setWidth(width);
        favorite.setHeight(height);
        favorite.setTag(new BmobRelation(tag));
        favorite.setUser(new BmobRelation(user));
        //判断是否已经收藏
        isAlreadyLiked(favorite,tag);
        //先查询该img，避免重复创建
        //findImageInCloud(favorite,tag);
        //alreadyLiked(favorite,tag);
    }

    private void isAlreadyLiked(final MyFavorite favorite, final FavoriteTag tag) {
        BmobQuery<MyFavorite> query = new BmobQuery<MyFavorite>();
        query.addWhereEqualTo("img_url",favorite.getImg_url());
        query.addWhereRelatedTo("img",new BmobPointer(tag));
        query.findObjects(context, new FindListener<MyFavorite>() {
            @Override
            public void onSuccess(List<MyFavorite> list) {
                if(list.size()>0){
                    //已经收藏过了
                    listener.onFavoriteError("已经收藏过咯");
                }else{
                    LogUtils.d("没有查到收藏数据");
                    findImageInCloud(favorite,tag);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d("没有查到收藏数据"+s);
                findImageInCloud(favorite,tag);
            }
        });
    }

    //在云端查询是否有该图片
    private void findImageInCloud(final MyFavorite favorite, final FavoriteTag tag) {
        BmobQuery<MyFavorite> query = new BmobQuery<MyFavorite>();
        query.addWhereEqualTo("img_url",favorite.getImg_url());
        query.findObjects(context, new FindListener<MyFavorite>() {
            @Override
            public void onSuccess(List<MyFavorite> list) {
                if(list.size()>0){
                    //已有数据直接关联
                    LogUtils.d("已有该图片直接关联");
                    setRelateToTag(list.get(0),tag);
                }else{
                    //没有有数据先创建
                    LogUtils.d("没有有该图片先创建");
                    saveMyFavoriteToCloud(favorite,tag);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d("没有有该图片先创建");
                saveMyFavoriteToCloud(favorite,tag);
            }
        });
    }

    private void saveMyFavoriteToCloud(final MyFavorite favorite, final FavoriteTag tag) {
        favorite.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtils.d("创建成功 开始关联");
                setRelateToTag(favorite,tag);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFavoriteError("收藏失败");
            }
        });
    }

    //关联数据
    private void setRelateToTag(MyFavorite favorite, FavoriteTag tag) {
        BmobRelation relation = new BmobRelation();
        relation.add(favorite);
        if(TextUtils.isEmpty(tag.getFront())) tag.setFront(favorite.getImg_url());
        tag.increment("number");
        tag.setImg(relation);
        tag.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                LogUtils.d("关联成功");
                listener.onFavoriteSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFavoriteError("收藏失败");
            }
        });
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
