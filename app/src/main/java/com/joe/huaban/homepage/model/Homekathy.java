package com.joe.huaban.homepage.model;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 主页网络数据请求的操作类
 * Created by Joe on 2016/4/13.
 */
public class HomeKathy extends BaseKathy{
    //请求的地址
    private final String HOME="photography";
    private Context mContext;
    public HomeKathy(Context context){
        this.mContext=context;
    }
    @Override
    public void getPicDataFromServer(final String max, final HomeDataListener listener, final boolean isLoadMore){
        RequestParams params = KathyParams.getParams(Constant.HOST_TAG+HOME);
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

    //从缓存获取数据
    @Override
    public void getPicDataFromCache(String max, HomeDataListener listener, boolean isLoadMore) {
        LogUtils.d("从缓存获取数据");
        File picFile=null;
        if(TextUtils.isEmpty(max)){
            picFile=new File(mContext.getCacheDir(),"pic.txt");
        }else{
            picFile=new File(mContext.getCacheDir(),"pic"+max+".txt");
        }
        if(!picFile.exists()) {
            getPicDataFromServer(max,listener,isLoadMore);
            return;
        }
        BufferedReader br=null;
        try {
            br=new BufferedReader(new FileReader(picFile));
            StringBuffer sb=new StringBuffer();
            String value="";
            while((value=br.readLine())!=null){
                sb.append(value);
            }
            LogUtils.d("从缓存获取数据成功"+sb.toString());
            if(!TextUtils.isEmpty(sb.toString()))  listener.onSuccess((PicData) parseData(sb.toString(),PicData.class),isLoadMore);
        } catch (Exception e) {
            e.printStackTrace();
        }   finally {
            getPicDataFromServer(max,listener,isLoadMore);
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //保存数据到缓存
    @Override
    public void savePicDataToCache(String result,String max) {
        File picFile=null;
        if(TextUtils.isEmpty(max)){
            picFile=new File(mContext.getCacheDir(),"pic.txt");
        }else{
            picFile=new File(mContext.getCacheDir(),"pic"+max+".txt");
        }
        if(!picFile.exists()) try {
            picFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw=null;
        try {
            bw=new BufferedWriter(new FileWriter(picFile));
            bw.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
