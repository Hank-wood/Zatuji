package com.joe.huaban.beauty.model;

import com.google.gson.Gson;
import com.joe.huaban.spider.CommonException;
import com.joe.huaban.utils.LogUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Joe on 2016/4/8.
 */
public class BeautyRequest {
    public static String HOST_HUABAN="http://api.huaban.com/favorite/beauty?limit=20";
    public static String HOST_Beauty="http://api.huaban.com/favorite/beauty?page=1&per_page=20&q=%E6%96%87%E8%89%BA";
    private static BeautyRequest instance;
    private BeautyRequest(){}
    public static BeautyRequest getInstance(){
        if(instance==null)instance = new BeautyRequest();
        return instance;
    }
    public BeautyData getBeautyData(){
        StringBuffer sb = new StringBuffer();
        Session webServer=new Session();
        try
        {
            HashMap<String,String> headers = new HashMap<String, String>();
            headers.put("Accept","*/*");
            headers.put("X-Client-ID:", "gzip, deflate, sdch");
            headers.put("Accept-Language", "zh-CN,zh;q=0.8");
            headers.put("Connection", "keep-alive");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36");
            headers.put("Host", "api.huaban.com");
            headers.put("Cookie", "_ga=GA1.2.1821778039.1457606239; __auc=99ae7f48153601e54cba88851c7; sid=KNrjNqFKA3WErpcurGxJpHNrlNP.FbRCh2JDSyXDG%2B83%2Fcm0zUTDjK3sjPz%2BIq7IicNkwec");
            headers.put("Upgrade-Insecure-Requests","1");
            HttpURLConnection conn =webServer.get(HOST_Beauty,headers);

            LogUtils.Logout("返回值："+conn.getResponseCode());
            if (conn.getResponseCode() == 200)
            {
                InputStream is = conn.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];

                while ((len = is.read(buf)) != -1)
                {
                    sb.append(new String(buf, 0, len, "UTF-8"));
                }

                is.close();
            } else
            {
                throw new CommonException("访问网络失败！");
            }

        } catch (Exception e)
        {
            try {
                throw new CommonException("访问网络失败！");
            } catch (CommonException e1) {
                e1.printStackTrace();
            }
        }
        return pareData(sb.toString());
    }

    private BeautyData pareData(String s) {
        LogUtils.Logout(s);
        Gson gson=new Gson();
        return gson.fromJson(s,BeautyData.class);
    }

}
