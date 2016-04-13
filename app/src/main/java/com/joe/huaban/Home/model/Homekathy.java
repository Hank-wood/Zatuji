package com.joe.huaban.Home.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.joe.huaban.Home.presenter.HomeDataListener;
import com.joe.huaban.global.Constant;
import com.joe.huaban.global.request.KathyParams;
import com.joe.huaban.global.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 封装主页需要的请求
 * Created by Joe on 2016/4/13.
 */
public class Homekathy {
    private final String HOME="all";
    public void getHomeData(String max, final HomeDataListener listener){
        RequestParams params = KathyParams.getParams(Constant.HOST+HOME);
        if(!TextUtils.isEmpty(max)) params.addQueryStringParameter("max",max);
        LogUtils.d(params.toString());
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                parseData(listener,result,HomeData.class);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                listener.onError(ex,isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void parseData(HomeDataListener listener, String result, Class<HomeData> homeDataClass) {
        Gson gson=new Gson();
        listener.onSuccess(gson.fromJson(result,homeDataClass));
    }
}
