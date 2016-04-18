package com.joe.huaban.base.model;

import com.google.gson.Gson;
import com.joe.huaban.homepage.presenter.HomeDataListener;

/**
 * Kathy为网络请求操作的封装工具类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseKathy {
    public abstract void getPicDataFromServer(String max, final HomeDataListener listener, boolean isLoadMore);
    public abstract void getPicDataFromCache(String max, final HomeDataListener listener, boolean isLoadMore);
    public abstract void savePicDataToCache(String result,String max);
    protected BaseData parseData( String result, Class<? extends BaseData> baseDataClass) {
        Gson gson=new Gson();

        return gson.fromJson(result,baseDataClass);
    }
}
