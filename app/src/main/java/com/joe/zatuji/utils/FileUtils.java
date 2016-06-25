package com.joe.zatuji.utils;


import android.os.Environment;

import com.joe.zatuji.Constant;

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
    /**
     * 转换file为二进制流
    */
    public static byte[] getFileToByte(File file) throws Exception {
        byte[] by = new byte[(int) file.length()];
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] bb = new byte[1024];
            int len;
            while ((len= is.read(bb))!= -1) {
                bytestream.write(bb, 0, len);
//                ch = is.read(bb);
            }
            by = bytestream.toByteArray();

        return by;

    }

    public static RequestBody toRequestBody(File value) {
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), value);
        return body;
    }

    public static void writeFile(byte[] buffer,String filePath){
        FileOutputStream fileOutputStream=null;
        try {
            File folder = new File(Environment.getExternalStorageDirectory()+"/"+Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD);
            if(!folder.exists()){
                folder.mkdirs();
            }
            File file = new File(filePath);
            if(!file.exists())file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
