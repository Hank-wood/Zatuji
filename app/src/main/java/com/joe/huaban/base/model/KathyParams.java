package com.joe.huaban.base.model;

import org.xutils.http.RequestParams;

/**
 * 封装RequestParams
 * Created by Joe on 2016/4/13.
 */
public class KathyParams {
    //获取一个带有header的请求
    public static RequestParams getParams(String url){
        RequestParams mParams=getNormalParams(url);
        mParams.addQueryStringParameter("limit","20");
        return mParams;
    }

    public static RequestParams getNormalParams(String url){
        RequestParams mParams=new RequestParams(url);
        mParams.setHeader("Accept","*/*");
        mParams.setHeader("X-Client-ID:", "gzip, deflate, sdch");
        mParams.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        mParams.setHeader("Connection", "keep-alive");
        mParams.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36");
        mParams.setHeader("Host", "api.huaban.com");
        mParams.setHeader("Cookie", "_ga=GA1.2.1821778039.1457606239; __auc=99ae7f48153601e54cba88851c7; sid=KNrjNqFKA3WErpcurGxJpHNrlNP.FbRCh2JDSyXDG%2B83%2Fcm0zUTDjK3sjPz%2BIq7IicNkwec");
        mParams.setHeader("Upgrade-Insecure-Requests","1");
        return mParams;
    }
}
