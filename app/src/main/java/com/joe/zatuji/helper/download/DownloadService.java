package com.joe.zatuji.helper.download;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.joe.zatuji.Constant;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.utils.FileUtils;
import com.joe.zatuji.utils.LogUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by joe on 16/6/21.
 */
public class DownloadService extends IntentService {
    public DownloadService() {
        super("download");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        com.joe.zatuji.api.DownloadService service = Api.createDownloadService(com.joe.zatuji.api.DownloadService.class, new ProgressCallback.ProgressDownloadListener() {
            @Override
            public void onDownLoadProgress(long bytesRead, long contentLength, boolean done) {
                LogUtils.d("current:"+bytesRead);
                LogUtils.d("total:"+contentLength);
            }
        });
        try {
            Response<ResponseBody> body =service.download(url).execute();
            FileUtils.writeFile(body.body().bytes(), Environment.getExternalStorageDirectory()+"/"+ Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+"zatuji.apk");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
