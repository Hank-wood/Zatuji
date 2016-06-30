package com.joe.zatuji.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Joe on 16/6/30.
 */
public class ShareHelper {
    public static void share(String desc, Uri uri, Context context){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if(uri!=null){
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            //当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", desc);
        }else{
            shareIntent.setType("text/plain");

        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, desc);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = Intent.createChooser(shareIntent,"分享至");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
