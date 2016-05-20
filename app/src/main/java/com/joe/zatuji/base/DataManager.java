package com.joe.zatuji.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Joe on 16/5/18.
 */
public class DataManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者

    //开始执行
    public void add(Subscription subscription){
        mCompositeSubscription.add(subscription);
    }

    //remove
    public void remove(){
        mCompositeSubscription.unsubscribe();
    }
}
