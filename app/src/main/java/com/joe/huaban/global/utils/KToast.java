package com.joe.huaban.global.utils;

import android.widget.Toast;

import com.joe.huaban.MyApplication;

/**
 * Created by Joe on 2016/4/18.
 */
public class KToast {
    public static void show(String str){
        Toast.makeText(MyApplication.getInstance(),str,Toast.LENGTH_SHORT).show();
    }
}
