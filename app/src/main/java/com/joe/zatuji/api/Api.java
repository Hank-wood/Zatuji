package com.joe.zatuji.api;



import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by baixiaokang on 16/3/9.
 */
public class Api {
    //接口地址
    public static final String HOST="http://api.huaban.com/";//主机地址
    public static final String HOST_PIC="http://img.hb.aicdn.com/";//图片保存地址
    public static final String HOST_BMOB = "https://api.bmob.cn/ ";//后台主机地址


    public ApiService mApiService;
    public BmobService mBmobService;
    public static String sToken= PrefUtils.getString(MyApplication.getInstance(),Constant.TOKEN,"");//

    Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Accept", "*/*")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36")
                    .addHeader("Cookie", "ga=GA1.2.1821778039.1457606239; __auc=99ae7f48153601e54cba88851c7; sid=KNrjNqFKA3WErpcurGxJpHNrlNP.FbRCh2JDSyXDG%2B83%2Fcm0zUTDjK3sjPz%2BIq7IicNkwec")
                    .addHeader("X-Client-ID", "gzip, deflate, sdch")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .build();
            return chain.proceed(request);
        }
    };
    Interceptor mBmobInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("X-Bmob-Application-Id", Constant.BMOB_KEY)
                    .addHeader("X-Bmob-REST-API-Key", Constant.BMOB_REST)
                    .addHeader("Content-Type","application/json")
                    .addHeader("X-Bmob-Session-Token",sToken)
                    .addHeader("Content-Transfer-Encoding","binary")
                    .build();
            return chain.proceed(request);
        }
    };

    HttpLoggingInterceptor mLog = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.api(message);
        }
    });
    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    //获取单例
    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //构造方法私有
    private Api() {
        mApiService = getRetrofit(mInterceptor,HOST).create(ApiService.class);
        mBmobService = getRetrofit(mBmobInterceptor).create(BmobService.class);
    }

    private Retrofit getRetrofit(Interceptor header,String host){
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient(header))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(host)
                .build();
        return retrofit;
    }
    private Retrofit getRetrofit(Interceptor header){

        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient(header))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HOST_BMOB)
                .build();
        return retrofit;
    }

    private OkHttpClient getOkHttpClient(Interceptor header) {
        mLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .addInterceptor(header)
                .addInterceptor(mLog)
                .build();
    }

    public void updateToke(String token){
        sToken = token;
        mBmobInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Bmob-Application-Id", Constant.BMOB_KEY)
                        .addHeader("X-Bmob-REST-API-Key", Constant.BMOB_REST)
                        .addHeader("Content-Type","application/json")
                        .addHeader("X-Bmob-Session-Token",sToken)
                        .build();
                return chain.proceed(request);
            }
        };
        mBmobService = getRetrofit(mBmobInterceptor).create(BmobService.class);
    }
}