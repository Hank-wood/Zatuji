package com.joe.zatuji.module.imageselectpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.joe.zatuji.R;

/**
 * 图片剪裁
 * Created by joe on 16/7/7.
 */
public class ImageCropActivity extends com.yongchun.library.view.ImageCropActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.crop_status_gray));
    }

    public static void startCrop(Activity activity, String path) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        activity.startActivityForResult(intent, REQUEST_CROP);
    }
}
