package com.joe.zatuji.module.gallerypage.view;

import com.joe.zatuji.data.bean.MyFavorite;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/2.
 */
public interface GalleryView {
    void showLoadError(String msg);

    void refreshGallery(ArrayList<MyFavorite> favorites);

    void addMoreGallery(ArrayList<MyFavorite> favorites);
}
