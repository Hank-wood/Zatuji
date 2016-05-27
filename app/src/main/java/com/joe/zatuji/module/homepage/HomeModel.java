package com.joe.zatuji.module.homepage;

import android.text.TextUtils;

import com.joe.zatuji.Constant;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.BaseModel;
import com.joe.zatuji.dao.CacheDao;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.LogUtils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/27.
 */
public class HomeModel implements BaseModel {
    public Observable<DataBean> getData(String limit, final String max){
        return Api.getInstance()
                .mApiService
                .getHomeData(limit,max)
                .doOnNext(new Action1<DataBean>() {
                    @Override
                    public void call(DataBean dataBean) {
                        LogUtils.d("写入缓存:max="+max);
                        CacheDao cacheDao = new CacheDao();
                        if(TextUtils.isEmpty(max)){
                            //每次刷新的时候先清掉之前的缓存 避免用户不停的刷新 缓存重复数据
                            cacheDao.clearCache();
                        }
                        cacheDao.intoCache(dataBean.pins, Constant.HOME);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<DataBean> getCacheData(final String limit, final int offset){

        return Observable.create(new Observable.OnSubscribe<DataBean>() {
            @Override
            public void call(Subscriber<? super DataBean> subscriber) {
                CacheDao dao = new CacheDao();
                DataBean dataBean = dao.getHomeCache(Integer.parseInt(limit),offset);
                LogUtils.d("cache:"+offset+":"+dataBean.pins.size());
                if(dataBean.pins.size()>0){
                    subscriber.onNext(dataBean);
                }else{
                    subscriber.onError(new Throwable("没有更多缓存数据啦～"));
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io());
    }
}
