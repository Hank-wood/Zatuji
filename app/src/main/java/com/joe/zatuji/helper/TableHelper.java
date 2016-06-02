package com.joe.zatuji.helper;

import com.google.gson.JsonElement;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.utils.LogUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by joe on 16/6/2.
 */
public class TableHelper<T> {
    //key1:{"__op":"Increment","amount":value}原子计数器
    public Observable<T> querySingle(final String table, final String objectId, final Class clazz){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Call<ResponseBody> call = Api.getInstance().mBmobService.querySingle(table,objectId);
                T t =null;
                try {
                    ResponseBody body = call.execute().body();
                    t= (T) GsonHelper.fromJson(body.string(),clazz);
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                    e.printStackTrace();
                }finally {
                    if(t!=null) {
                        subscriber.onNext(t);
                    }else{
                        subscriber.onError(new Throwable("error"));
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<T> query(final String table, final JsonElement where, final Class clazz, final String order, final int limit, final int skip){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Call<ResponseBody> call = Api.getInstance().mBmobService.query(table,where,order,limit,skip);
                T t =null;
                try {
                    ResponseBody body = call.execute().body();
                    t= (T) GsonHelper.fromJson(body.string(),clazz);
                } catch (IOException e) {
                    LogUtils.d(e.getMessage());
                    e.printStackTrace();
                }finally {
                    if(t!=null) {
                        subscriber.onNext(t);
                    }else{
                        subscriber.onError(new Throwable("error"));
                    }
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<T> query(final String table, final JsonElement where, final Class clazz){
        return query(table,where,clazz,"",0,0);
    }

    public Observable<T> query(final String table, final JsonElement where, final Class clazz,String order){
        return query(table,where,clazz,order,0,0);
    }


    /**
     * 批量操作
     * {
     "requests": [
     {
     "method": "POST",
     "path": "/1/classes/TableName",
     "body": {
     key1: value1,
     key2: value2,
     ...
     }
     },
     {
     "method": "PUT",
     "token": "tokenValue"(具有ACL规则时),
     "path": "/1/classes/TableName/objectId",
     "body": {
     key1: value1,
     ...
     }
     },
     {
     "method": "DELETE",
     "token": "tokenValue"(具有ACL规则时),
     "path": "/1/classes/TableName/objectId"
     },

     ...

     ]
     }
     */
}
