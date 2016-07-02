package com.joe.zatuji.base.model;

import com.joe.zatuji.Event;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 该类
 * Created by Joe on 16/5/18.
 */
public class RxJavaManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者

    //开始执行
    public void add(Subscription subscription){
        mCompositeSubscription.add(subscription);
    }
    public RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察者

    //remove
    public void remove(){
        mCompositeSubscription.unsubscribe();
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unSubscribe(entry.getKey(), entry.getValue());// 移除观察
        }
    }

    public Observable subscribe(String eventName, Action1<Object> action1) {
        Observable<?> mObservable = mRxBus.subscribe(eventName);
        mObservables.put(eventName, mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }));
        return mObservable;
    }
    public void unSubscribe(String event,Observable<?> observable){
        mRxBus.unSubscribe(event,observable);
    }
    public void unSubscribe(String event){
        mRxBus.unSubscribe(event);
    }
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
