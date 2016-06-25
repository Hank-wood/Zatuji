package com.joe.zatuji.helper.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.joe.zatuji.Constant;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.utils.FileUtils;
import com.joe.zatuji.utils.LogUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by joe on 16/6/21.
 */
public class DownloadService extends IntentService {

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;
    private File file;

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
        initNotification();
        file = new File(Environment.getExternalStorageDirectory()+"/"+ Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+"zatuji.apk");
        com.joe.zatuji.api.DownloadService service = Api.createDownloadService(com.joe.zatuji.api.DownloadService.class, new ProgressCallback.ProgressDownloadListener() {
            @Override
            public void onDownLoadProgress(long bytesRead, long contentLength, boolean done) {
                if(bytesRead!=contentLength){
                    int progress  = (int) (bytesRead*100.0/contentLength*1.0);
                    setProgress(progress,false);
                }else{
                    setProgress(0,true);
                }
            }
        });
        try {
            Response<ResponseBody> body =service.download(url).execute();
            FileUtils.writeFile(body.body().bytes(), Environment.getExternalStorageDirectory()+"/"+ Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+"zatuji.apk");
            startActivity(installAPK(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDone() {

    }

    private void showProgress() {


    }

    /**
     * 安装下载完成的APK
     * @param savedFile
     */
    private Intent installAPK(File savedFile) {
        //调用系统的安装方法
        Intent intent=new Intent();
        intent.setAction(intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(savedFile), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void initNotification(){
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("杂图集更新");
        mBuilder.setContentText("下载中");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
    }

    private void setProgress(int progress,boolean done){
        if(done){
            mBuilder.setProgress(0,0,false);
            mBuilder.setContentText("下载完成");
            PendingIntent pendingIntent = PendingIntent.getActivity(this,R.mipmap.ic_launcher,installAPK(file),PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            mManager.notify(R.mipmap.ic_launcher,mBuilder.build());
        }else {
            mBuilder.setContentText("下载中 "+progress+"%");
            mBuilder.setProgress(100,progress,false);
            mManager.notify(R.mipmap.ic_launcher,mBuilder.build());
        }
    };
}
