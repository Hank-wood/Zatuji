package com.joe.zatuji.helper.download;

import com.joe.zatuji.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by joe on 16/6/21.
 */
public class HttpClientHelper {
    static HttpLoggingInterceptor mLog = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.api(message);
        }
    });

    /**
     * 包装OkHttpClient，用于下载文件的回调
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addProgressResponseListener(final ProgressCallback.ProgressDownloadListener progressListener){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());

                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        mLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(mLog);
        return client.build();
    }


    /**
     * 包装OkHttpClient，用于上传文件的回调
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addProgressRequestListener(final ProgressCallback.ProgressUploadListener progressListener){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), new ProgressRequestBody(original.body(),progressListener))
                        .build();
                return chain.proceed(request);
            }
        });
        mLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(mLog);
        return client.build();
    }
}
