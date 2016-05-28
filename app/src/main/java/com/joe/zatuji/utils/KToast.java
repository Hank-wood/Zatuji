package com.joe.zatuji.utils;

import android.widget.Toast;

import com.joe.zatuji.MyApplication;

/**
 * Created by Joe on 2016/4/18.
 */
public class KToast {
    private static long lastTime = 0;
    private static long duration = 3000;
    private static String lastStr = "";

    public static void show(String str) {
//        LogUtils.d("lastTime:" + lastTime + ":currentTime:" + System.currentTimeMillis());
//        LogUtils.d("duration:" + (System.currentTimeMillis() - lastTime));
//        LogUtils.d("lastStr:" + lastStr + ":str:" + str);
        //避免多次重复Toast
        if (str.equals(lastStr) && (System.currentTimeMillis() - lastTime) < duration) return;
        Toast.makeText(MyApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
        lastTime = System.currentTimeMillis();
        lastStr = str;
    }

}
