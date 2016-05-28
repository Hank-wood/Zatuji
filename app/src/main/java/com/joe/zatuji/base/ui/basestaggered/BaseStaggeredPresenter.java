package com.joe.zatuji.base.ui.basestaggered;

import com.joe.zatuji.SConstant;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.TUtil;

/**
 * Created by joe on 16/5/27.
 */
public abstract class BaseStaggeredPresenter<T extends BaseStaggeredView, E extends BaseModel> {
    protected T mView;
    protected E mModel;
    protected String mLimit = "20";
    protected String mMax;
    protected int mPage;
    protected int mOffset;
    protected boolean noMoreData;//没有更多数据了

    protected RxJavaManager mRxJavaManager;

    public void setView(T view) {
        this.mView = view;
        this.mModel = TUtil.getT(this, 1);//获取model实例
        mRxJavaManager = new RxJavaManager();
    }

    public abstract void onStart();

    public void onRemove() {
        if (mRxJavaManager != null) mRxJavaManager.remove();
    }

    public void loadData() {
        mView.disableLoadMore(false);
        resetOffset();
    }

    public void reLoadData() {
        mView.disableLoadMore(false);
        noMoreData = false;
        resetOffset();
    }

    public void loadMoreData() {
    }

    /**没有更多数据*/
    protected void showNoMoreData(){
        mView.addData(null);
        mView.disableLoadMore(true);
        mView.showToastMsg(SConstant.NO_MORE_DATA);
    }
    private void resetOffset() {
        mMax = "";
        mOffset = 0;
        mPage = 1;
    }

    protected void countOffset(DataBean dataBean) {
        if(dataBean!=null && dataBean.pins!=null &&dataBean.pins.size()>0) {
            mMax = dataBean.pins.get(dataBean.pins.size() - 1).pin_id;
            mOffset += dataBean.pins.size();
            mPage++;
        }
    }

}
