package com.joe.zatuji.module.searchingpage;

import android.text.TextUtils;

import com.joe.zatuji.Event;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredPresenter;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredView;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.utils.LogUtils;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by joe on 16/5/28.
 */
public class SearchPresenter extends BaseStaggeredPresenter<BaseStaggeredView,SearchModel> {
    protected String query="";
    @Override
    public void onStart() {

    }
    public void setQuery(String query){
        this.query = query;
    }
    @Override
    public void loadData() {
        super.loadData();
        LogUtils.d("page:"+mPage);
        if(TextUtils.isEmpty(query)) {
            mView.showEmptyView();
            return;
        }
        mRxJavaManager.add(mModel.getData(mPage,mLimit,query)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RxSubscriber<DataBean>() {
            @Override
            public void onError(Throwable e) {
                mView.showEmptyView();
                mView.showToastMsg("没有查到与"+query+"相关图片");
            }

            @Override
            public void onNext(DataBean dataBean) {
                if(dataBean.pins==null||dataBean.pins.size()<=0){
                    mView.showToastMsg("没有查到与"+query+"相关图片");
                }else{
                    mView.loadData(dataBean.pins);
                }
                countOffset(dataBean);
            }
        }));
    }

    @Override
    public void reLoadData() {
        super.reLoadData();
        loadData();
    }

    @Override
    public void loadMoreData() {
        super.loadMoreData();
        LogUtils.d("page:"+mPage);
        mRxJavaManager.add(mModel.getData(mPage,mLimit,query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<DataBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        mView.showEmptyView();
                        showNoMoreData();
                        mRxJavaManager.post(Event.LOAD_MORE_DONE,null);
                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        if(dataBean.pins==null||dataBean.pins.size()==0){
                            showNoMoreData();
                            mRxJavaManager.post(Event.LOAD_MORE_DONE,null);
                        }else{
                            mView.addData(dataBean.pins);
                            countOffset(dataBean);
                            mRxJavaManager.post(Event.LOAD_MORE_DONE,dataBean.pins);
                        }

                    }
                }));
    }

}
