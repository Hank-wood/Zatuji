package com.joe.zatuji.api;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by joe on 16/6/21.
 */
public interface DownloadService {

    @GET
    Call<ResponseBody> download(@Url String fileUrl);
}
