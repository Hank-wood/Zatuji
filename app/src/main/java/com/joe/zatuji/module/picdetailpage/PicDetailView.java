package com.joe.zatuji.module.picdetailpage;

import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;

import java.util.ArrayList;

/**
 * Created by joe on 16/6/2.
 */
public interface PicDetailView extends BaseView {
    void showTag(ArrayList<FavoriteTag> tags);
    void addGallery(ArrayList<MyFavorite> data);
    void addPics(ArrayList<DataBean.PicBean> data);
}
