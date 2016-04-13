package com.joe.huaban.global.utils;

import android.content.Context;

/**
 * Created by Joe on 2016/3/11.
 */
public class DPUtils {
        /**
         * dp和px的转换
         */
        public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        public static int px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }

}
