package com.joe.zatuji.helper;

import rx.Subscriber;

/**
 * Created by joe on 16/5/17.
 */
public abstract class RxSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public abstract void onError(Throwable e);

    @Override
     public abstract void onNext(T t);

}
