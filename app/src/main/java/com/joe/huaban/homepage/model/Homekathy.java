package com.joe.huaban.homepage.model;

import android.text.TextUtils;

import com.joe.huaban.base.model.PicData;
import com.joe.huaban.homepage.presenter.HomeDataListener;
import com.joe.huaban.base.model.BaseKathy;
import com.joe.huaban.global.Constant;
import com.joe.huaban.base.model.KathyParams;
import com.joe.huaban.global.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 主页网络数据请求的操作类
 * Created by Joe on 2016/4/13.
 */
public class HomeKathy extends BaseKathy{
    //请求的地址
    private final String HOME="photography";
    @Override
    public void getHomeData(String max, final HomeDataListener listener, final boolean isLoadMore){
        RequestParams params = KathyParams.getParams(Constant.HOST_TAG+HOME);
        if(!TextUtils.isEmpty(max)) params.addQueryStringParameter("max",max);
        LogUtils.d(params.toString());
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                listener.onSuccess((PicData) parseData(result,PicData.class),isLoadMore);
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

}
