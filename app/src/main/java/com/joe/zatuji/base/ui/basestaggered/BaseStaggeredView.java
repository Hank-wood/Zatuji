package com.joe.zatuji.base.ui.basestaggered;

import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.BaseBean;

import java.util.List;


/**
 * Created by joe on 16/5/27.
 */
public interface BaseStaggeredView extends BaseView {
    void loadData(List<? extends BaseBean> beanList);
    void refreshData(List<? extends BaseBean> beanList);
    void addData(List<? extends BaseBean> addList);
    void disableLoadMore(boolean isDisable);
    void showEmptyView();
    void setToCurrentPosition(int position);
}
