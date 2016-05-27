package com.joe.zatuji.module.searchingpage.model;

import com.joe.zatuji.base.BaseKathy;
import com.joe.zatuji.base.KathyParams;
import com.joe.zatuji.data.bean.PicData;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.homepage.presenter.HomeDataListener;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by Joe on 2016/4/20.
 */
public class SearchKathy extends BaseKathy{

    public void getPicDataFromServer(String q, String page , final HomeDataListener listener, final boolean isLoadMore) {
        RequestParams params = KathyParams.getNormalParams(Constant.HOST_SEARCH);
        params.addQueryStringParameter("page",page);
        params.addQueryStringParameter("per_page","20");
        params.addQueryStringParameter("q",q);
        LogUtils.d(params.toString());
        x.http().request(HttpMethod.GET, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                //缓存起来
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
    public  String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
    @Override
    public void getPicDataFromServer(String max, HomeDataListener listener, boolean isLoadMore) {

    }

    @Override
    public void getPicDataFromCache(String max, HomeDataListener listener, boolean isLoadMore) {

    }

    @Override
    public void savePicDataToCache(String result, String max) {

    }
}
