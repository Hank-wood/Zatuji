package com.joe.zatuji.utils;


import android.os.Environment;

import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by joe on 16/5/30.
 */
public class FileUtils {

    public static RequestBody toRequestBody(File value) {
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), value);
        return body;
    }

    public static String writePicture(byte[] buffer, String filePath){
        FileOutputStream fileOutputStream=null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+Constant.DIR_DOWNLOAD+"/"+filePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(!file.exists())file.createNewFile();
            LogUtils.d("pic path:"+file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String writeFiles(byte[] buffer, String filePath){
        FileOutputStream fileOutputStream=null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+Constant.DIR_APP+"/"+filePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(!file.exists())file.createNewFile();
            LogUtils.d("file path:"+file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public static String writeCache(byte[] buffer,String filePath){
        FileOutputStream fileOutputStream=null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+Constant.DIR_SHARE+"/"+filePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(!file.exists())file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] file2Bytes(File file){
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
