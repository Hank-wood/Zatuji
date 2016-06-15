package com.joe.zatuji.module.favoritepage;

import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.bean.FavoriteTag;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/1.
 */
public interface TagView extends BaseView {
    void showTag(ArrayList<FavoriteTag> tags);
    void showNotLogin();
    void showNoTag();
    void addTag(ArrayList<FavoriteTag> tags);
    void setAddedNew(boolean isAdded);
}
