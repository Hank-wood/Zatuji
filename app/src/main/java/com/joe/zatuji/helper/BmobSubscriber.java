package com.joe.zatuji.helper;

import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.utils.LogUtils;

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
        LogUtils.e("error:"+e.getMessage());
        if (e instanceof HttpException) {
            ResponseBody body = ((HttpException) e).response().errorBody();
            int code = ((HttpException) e).response().code();
            //服务器自定义异常
            if (code == 404) {
                try {
                    ResultException resultException = GsonHelper.fromJson(body.string(), ResultException.class);
                    onError(resultException);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    onError(new ResultException("网络异常"));
                }
            }
            //onError(e);
        } else if(e instanceof ResultException){
            onError(e);
        }
        else {
            onError(new ResultException(e.getMessage()));
        }
    }

    public abstract void onError(ResultException e);

    public void onOtherError(Throwable e){

    };

    @Override
    public abstract void onNext(T t);

}
