package com.joe.zatuji.picdetail.presenter;

/**
 * Created by Joe on 2016/4/18.
 */
public interface PicDetailListener {
    void onFavoriteSuccess();
    void onFavoriteError(String errorMsg);
    void onSaveSuccess();
    void onSaveError(String errorMsg);
}
