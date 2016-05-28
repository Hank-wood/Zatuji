package com.joe.zatuji.module.discoverpage;


import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredModel;
import com.joe.zatuji.dao.CacheDao;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.LogUtils;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/28.
 */
public class DiscoverModel extends BaseStaggeredModel {
    public Observable<DataBean> getData(final String tag, String limit, final String max){

        return Api.getInstance()
                .mApiService
                .discoveryTag(tag,limit,max)
                .doOnNext(new Action1<DataBean>() {
                    @Override
                    public void call(DataBean dataBean) {
                        saveToCache(max,dataBean,tag);
                    }
                })
                .subscribeOn(Schedulers.io())
                ;
    }

    public Observable<DataBean> getCacheData(final String tag,final String limit, final int offset){

        return Observable.create(new Observable.OnSubscribe<DataBean>() {
            @Override
            public void call(Subscriber<? super DataBean> subscriber) {
                CacheDao dao = new CacheDao();
                DataBean dataBean = dao.getDiscoverCache(tag,Integer.parseInt(limit),offset);
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
