package com.joe.zatuji.module.favoritepage.model;

import android.content.Context;

import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.module.favoritepage.presenter.FavoriteTagListener;
import com.joe.zatuji.data.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
    private FavoriteTagListener listener;
    public FavoriteKathy(Context context, FavoriteTagListener listener){
        this.context=context;
        this.listener=listener;
    }

    public void getFavoriteTag(User user){
        BmobQuery<FavoriteTag> query = new BmobQuery<FavoriteTag>();
        query.order("createdAt");
        query.addWhereRelatedTo("tag",new BmobPointer(user));
        //query.addWhereEqualTo("belong",new BmobPointer(user));
        query.findObjects(context, new FindListener<FavoriteTag>() {
            @Override
            public void onSuccess(List<FavoriteTag> list) {
                listener.onSuccess((ArrayList<FavoriteTag>) list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }
        });
    }

    public void createTag(final FavoriteTag tag, final User user){
        tag.save(context, new SaveListener() {
            @Override
            public void onSuccess() {

                relateToUser(tag,user);

            }

            @Override
            public void onFailure(int i, String s) {
                listener.onCreateError(s);
            }
        });
    }

    //将该tag与用户绑定
    private void relateToUser(final FavoriteTag tag, User user) {
        BmobRelation relation = new BmobRelation();
        relation.add(tag);
//        user.setTag(relation);
//        user.update(context, new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                ArrayList<FavoriteTag> tags=new ArrayList<FavoriteTag>();
//                tags.add(tag);
//                listener.onCreateSuccess(tags);
//                LogUtils.d("关联成功");
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                LogUtils.d("关联失败"+s);
//                listener.onCreateError(s);
//            }
//        });
    }
//    public void getFavoriteData(int page,int limit){
//        FavoriteOpenHelper helper=new FavoriteOpenHelper(context);
//        SQLiteDatabase db=helper.getWritableDatabase();
//        String sql = "select * from FavoritePic limit "+(page-1)*limit+","+limit;
//        LogUtil.d(sql);
//        Cursor cursor=db.rawQuery(sql, null);
//        PicData picData=new PicData();
//        picData.pins=new ArrayList<PicData.PinsBean>();
//        if(cursor!=null){
//            while (cursor.moveToNext()){
//                PicData.PinsBean pin=new PicData.PinsBean();
//
//                pin.file=new PicData.PinsBean.FileBean();
//                pin.raw_text=cursor.getString(cursor.getColumnIndex(IMG_DESC));
//                pin.file.key=cursor.getString(cursor.getColumnIndex(IMG_URL));
//                pin.file.width=cursor.getInt(cursor.getColumnIndex(IMG_WIDTH));
//                pin.file.height=cursor.getInt(cursor.getColumnIndex(IMG_HEIGHT));
//                picData.pins.add(pin);
//            }
//        }
//        if(picData.pins.size()<=0){
//            listener.onError(null,false);
//        }else{
//            if(page>1){
//                listener.onSuccess(picData,true);
//            }else{
//                listener.onSuccess(picData,false);
//            }
//        }
//        if(db!=null){
//            cursor.close();
//            db.close();
//        }
//    }
}
