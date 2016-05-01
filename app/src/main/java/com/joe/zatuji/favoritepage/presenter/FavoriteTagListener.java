package com.joe.zatuji.favoritepage.presenter;

import com.joe.zatuji.favoritepage.model.FavoriteTag;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/1.
 */
public interface FavoriteTagListener {
    void onSuccess(ArrayList<FavoriteTag> tags);
    void onError(String msg);

    void onCreateSuccess(ArrayList<FavoriteTag> tags);
    void onCreateError(String msg);
    void onNotSign();
}
