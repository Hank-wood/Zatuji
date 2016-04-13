package com.joe.huaban.base.model;

import com.google.gson.Gson;
import com.joe.huaban.Home.presenter.HomeDataListener;

/**
 * Kathy为网络请求操作的封装工具类
 * Created by Joe on 2016/4/14.
 */
public abstract class BaseKathy {
    public abstract void getHomeData(String max, final HomeDataListener listener);
    protected BaseData parseData( String result, Class<? extends BaseData> baseDataClass) {
        Gson gson=new Gson();

        return gson.fromJson(result,baseDataClass);
    }
}
