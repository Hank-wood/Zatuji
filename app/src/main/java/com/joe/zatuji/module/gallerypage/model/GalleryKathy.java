package com.joe.zatuji.module.gallerypage.model;

import android.content.Context;

import com.joe.zatuji.module.favoritepage.model.FavoriteTag;
import com.joe.zatuji.module.favoritepage.model.MyFavorite;
import com.joe.zatuji.module.gallerypage.presenter.GalleryListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 获取图集相应数据
 * Created by Joe on 2016/5/2.
 */
public class GalleryKathy {
    private Context context;
    private GalleryListener mListener;

    public GalleryKathy(Context context, GalleryListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    //分页查找
    public void getGalleryData(FavoriteTag tag,int limit,int page){
        BmobQuery<MyFavorite> query = new BmobQuery<MyFavorite>();
        query.order("-createAt");
        query.addWhereRelatedTo("img",new BmobPointer(tag));
        query.setLimit(limit);
        query.setSkip((page-1)*limit);
        final boolean isMore = page > 1;
        query.findObjects(context, new FindListener<MyFavorite>() {
            @Override
            public void onSuccess(List<MyFavorite> list) {
                mListener.onGetImgSuccess(isMore, (ArrayList<MyFavorite>) list);
            }

            @Override
            public void onError(int i, String s) {
                mListener.onGetImgError(isMore,s);
            }
        });
    }
}
