package com.joe.zatuji.helper;

import com.joe.zatuji.api.exception.ResultException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by joe on 16/5/17.
 */
public abstract class BmobSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            ResponseBody body = ((HttpException) e).response().errorBody();
            int code = ((HttpException) e).response().code();
            //服务器自定义异常
            if (code == 404) {
                try {
                    ResultException resultException = GsonHelper.fromJson(body.string(), ResultException.class);
                    if(resultException.getCode()!=0){
                        onError(resultException);
                    }else{
                        onOtherError(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    onOtherError(e);
                }
            }
            //onError(e);
        } else {
            onOtherError(e);
        }
    }

    public abstract void onError(ResultException e);

    public void onOtherError(Throwable e){

    };

    @Override
    public abstract void onNext(T t);

}
