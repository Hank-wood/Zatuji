package com.joe.zatuji.module.discoverpage;

import com.joe.zatuji.SConstant;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredPresenter;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredView;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.utils.LogUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by joe on 16/5/28.
 */
public class DiscoverPresenter extends BaseStaggeredPresenter<BaseStaggeredView, DiscoverModel> {
    protected String mTag;
    @Override
    public void onStart() {

    }
    public void setTag(String tag){
        this.mTag = tag;
    }
    @Override
    public void loadData() {
        super.loadData();
        mRxJavaManager.add(getObservable(mTag)
                .subscribe(new RxSubscriber<DataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        mView.showEmptyView();
                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        mView.loadData(dataBean.pins);
                        countOffset(dataBean);
                    }
                }));

    }

    @Override
    public void reLoadData() {
        super.reLoadData();
        mRxJavaManager.add(getObservable(mTag)
                .subscribe(new RxSubscriber<DataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        mView.showEmptyView();
                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        mView.refreshData(dataBean.pins);
                        countOffset(dataBean);
                    }
                }));
    }

    @Override
    public void loadMoreData() {
        super.loadMoreData();
        mRxJavaManager.add(getObservable(mTag)
                .subscribe(new RxSubscriber<DataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        showNoMoreData();
                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        mView.addData(dataBean.pins);
                        countOffset(dataBean);
                    }
                }));
    }

    private Observable<DataBean> getObservable(String tag) {
        return Observable.concat(mModel.getData(tag, mLimit, mMax)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d("error in api request:" + Thread.currentThread().getName());
                        mView.showToastMsg(SConstant.NET_NO_GOOD);
                    }
                }).onErrorReturn(new Func1<Throwable, DataBean>() {
                    @Override
                    public DataBean call(Throwable throwable) {
                        return null;
                    }
                }), mModel.getCacheData(tag, mLimit, mOffset))
                .first(new Func1<DataBean, Boolean>() {
                    @Override
                    public Boolean call(DataBean dataBean) {
                        return dataBean != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
