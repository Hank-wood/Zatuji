package com.joe.zatuji.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
            byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = bytestream.toByteArray();

        return by;
    }

    public static Map<String, RequestBody> transformFiletoBinary(String fileName,File file){
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        params.put("AttachmentKey\"; filename=\""+fileName, requestBody);
        return params;
    }

    public static RequestBody transformFiletoBinary(File file){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        return requestBody;
    }
}
