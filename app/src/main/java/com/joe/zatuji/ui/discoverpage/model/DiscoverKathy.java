package com.joe.zatuji.ui.discoverpage.model;

import android.content.Context;
import android.text.TextUtils;

import com.joe.zatuji.base.BaseKathy;
import com.joe.zatuji.base.KathyParams;
import com.joe.zatuji.data.bean.PicData;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.ui.homepage.presenter.HomeDataListener;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Joe on 2016/4/18.
 */
public class DiscoverKathy extends BaseKathy {
    private String mTag="beauty";
    private Context context;
    public void setTag(int tag){
        switch (tag){
            case 1:
                mTag="beauty";//妹子
                break;
            case 2:
                mTag="men";//型男
                break;
            case 3:
                mTag="kids";//萌宝
                break;
            case 4:
                mTag="photography";//摄影
                break;
            case 5:
                mTag="diy_crafts";//手工
                break;
            case 6:
                mTag="apparel";//女装
                break;
            case 7:
                mTag="travel_places";//旅行
                break;
            case 8:
                mTag="illustration";//插画
                break;
            case 9:
                mTag="architecture";//建筑
                break;
        }
    }
    public DiscoverKathy(Context context){
        this.context=context;
    }
    @Override
    public void getPicDataFromServer(final String max, final HomeDataListener listener, final boolean isLoadMore) {
        RequestParams params = KathyParams.getParams(Constant.HOST_TAG+mTag);
        if(!TextUtils.isEmpty(max)) params.addQueryStringParameter("max",max);
        LogUtils.d(params.toString());
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                //缓存起来
                savePicDataToCache(result,max);
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

    @Override
    public void getPicDataFromCache(String max, HomeDataListener listener, boolean isLoadMore) {

    }

    @Override
    public void savePicDataToCache(String result, String max) {

    }
}
