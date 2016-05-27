package com.joe.zatuji.module.homepage;


import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredPresenter;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredView;
import com.joe.zatuji.utils.LogUtils;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by joe on 16/5/27.
 */
public class HomePresenter extends BaseStaggeredPresenter<BaseStaggeredView, HomeModel> {

    @Override
    public void onStart() {
    }

    @Override
    public void loadData() {
        super.loadData();
        LogUtils.d("load data");
        mMax = "";
        mOffset = 0;
        Subscription subscription = getObservable()
                .subscribe(new RxSubscriber<DataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        mView.showToastMsg("出错啦");
                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        mView.loadData(dataBean.pins);
                        countOffset(dataBean);
                    }
                });
        mDataManager.add(subscription);
    }

    @Override
    public void reLoadData() {
        super.reLoadData();
        LogUtils.d("noMoreData:"+noMoreData);
        mMax = "";
        mOffset = 0;
        Subscription subscription = getObservable()
                .subscribe(new Action1<DataBean>() {
                    @Override
                    public void call(DataBean dataBean) {
                        mView.refreshData(dataBean.pins);
                        countOffset(dataBean);
                    }
                });
        mDataManager.add(subscription);
    }

    @Override
    public void loadMoreData() {
        super.loadMoreData();
        LogUtils.d("继续load");
        mDataManager.add(getObservable().subscribe(new RxSubscriber<DataBean>() {
            @Override
            public void onError(Throwable e) {
                LogUtils.d("error no more data");
                mView.addData(null);
                mView.disableLoadMore(true);
                mView.showToastMsg("没有更多图片啦");
            }

            @Override
            public void onNext(DataBean dataBean) {
                mView.addData(dataBean.pins);
                countOffset(dataBean);
            }
        }));
    }

    private Observable<DataBean> getObservable() {
        return Observable.concat(mModel.getData(mLimit, mMax)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d("error in api request:" + Thread.currentThread().getName());
                        mView.showToastMsg("网络不给力～");
                    }
                }).onErrorReturn(new Func1<Throwable, DataBean>() {
                    @Override
                    public DataBean call(Throwable throwable) {
                        return null;
                    }
                }), mModel.getCacheData(mLimit, mOffset))
                .first(new Func1<DataBean, Boolean>() {
                    @Override
                    public Boolean call(DataBean dataBean) {
                        return dataBean != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}
