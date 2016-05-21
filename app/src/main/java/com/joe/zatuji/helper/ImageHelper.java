package com.joe.zatuji.helper;

import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.data.bean.DataBean;

/**
 * Created by joe on 16/5/21.
 */
public class ImageHelper {
    /**
     * 普通图片显示
     */
    private static DrawableRequestBuilder<String> baseGlide(ImageView iv, String key){
        return Glide.with(iv.getContext())
                .load(key)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(null)
                .placeholder(null);
    }
    /**
     * gif图片显示
     */
    private static GifRequestBuilder<String> baseGif(ImageView iv, String key){
        return Glide.with(iv.getContext())
                .load(key)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(null)
                .placeholder(null);
    }
    /**
     * 展示缩略图
     */
    public static void showSmall(ImageView iv , String key,String type ){
        if(type.contains("gif")){
            baseGif(iv,key).fitCenter().into(iv);
        }else{
            baseGlide(iv,key).centerCrop().into(iv);
        }

    }
    /**
     * 展示全屏大图
     */
    public static void showBig(ImageView iv , DataBean.PicBean pic){
        resizeImage(iv,pic);
        if(pic.file.type.equals("gif")){
            baseGif(iv,Api.HOST_PIC+pic.file.key).into(iv);
        }else{
            baseGlide(iv,Api.HOST_PIC+pic.file.key).into(iv);
        }
    }

    /**
     * 显示大图时重新计算图片的大小
     * 宽同屏幕
     * 高按原图比例计算
     */
    public static void resizeImage(ImageView iv, DataBean.PicBean  pic){
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        //获取屏幕宽高
        DisplayMetrics dm =iv.getContext().getResources().getDisplayMetrics();
        params.width = dm.widthPixels;
        double times= (dm.widthPixels+0.0)/(pic.file.width +0.0);
        double resizeHeight = pic.file.height*times;
        params.height = (int) resizeHeight;
        if(resizeHeight-params.height>=0.5){
            params.height+=1;
        }
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
