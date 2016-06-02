package com.joe.zatuji.module.gallerypage.presenter;

import com.joe.zatuji.data.bean.MyFavorite;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/2.
 */
public interface GalleryListener {
    void onGetImgSuccess(boolean isMore, ArrayList<MyFavorite> favorites);
    void onGetImgError(boolean isMore,String msg);
}
