package com.joe.zatuji.ui.favoritepage.view;

import com.joe.zatuji.ui.favoritepage.model.FavoriteTag;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/1.
 */
public interface TagView {
    void showTag(ArrayList<FavoriteTag> tags);
    void showErrorMsg(String msg);
    void showNotSign();
    void addTag(ArrayList<FavoriteTag> tags);
}
