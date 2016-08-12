package com.joe.zatuji.helper;


import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;

import com.joe.zatuji.MyApplication;

/**
 * description - resource获取帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/16.
 * change
 * change on .
 */
public class ResourceHelper {
    public static String getString(@StringRes int id){
        return MyApplication.getContext().getResources().getString(id);
    }

    public static String getFormatString(@StringRes int id, Object...s){
        return String.format(MyApplication.getContext().getResources().getString(id),s);
    }

    public static float getDimen(@DimenRes int id){
        return MyApplication.getContext().getResources().getDimension(id);
    }

    public static float getDimenPixel(@DimenRes int id){
        return MyApplication.getContext().getResources().getDimensionPixelSize(id);
    }


}
