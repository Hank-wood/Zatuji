package com.joe.zatuji.helper.download;

/**
 * Created by joe on 16/6/21.
 */
public interface ProgressCallback {
    /**
     * 请求体进度回调接口，用于文件上传进度回调
     */
    public interface ProgressUploadListener {
        void onUploadProgress(long bytesWritten, long contentLength, boolean done);
    }

    /**
     * 响应体进度回调接口，用于文件下载进度回调
     */
    public interface ProgressDownloadListener {
        void onDownLoadProgress(long bytesRead, long contentLength, boolean done);
    }
}
