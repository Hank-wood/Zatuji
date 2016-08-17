package com.joe.zatuji.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.DPUtils;
import com.joe.zatuji.utils.LogUtils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.R.attr.key;
import static u.aly.au.B;

/**
 * Created by joe on 16/5/21.
 */
public class ImageHelper {
    private static int[] defaultColor=new int[]{R.color.DefaultGreen,R.color.DefaultBlue,
            R.color.DefaultRed,R.color.DefaultPurple};
    private static int getRandomColor() {
        Random random=new Random();
        return defaultColor[ random.nextInt(4)];
    }
    /**
     * 普通图片显示
     */
    private static DrawableRequestBuilder<String> baseGlide(ImageView iv, String key){
        return Glide.with(iv.getContext())
                .load(key)
                .crossFade(150)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(getRandomColor());

    }
    /**
     * gif图片显示
     */
    private static GifRequestBuilder<String> baseGif(ImageView iv, String key){
        return Glide.with(iv.getContext())
                .load(key)
                .asGif()
                .crossFade(150)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(getRandomColor());
    }
    /**
     * 展示缩略图
     */
    public static void showSmall(ImageView iv , String key,String type ){
//        LogUtils.d("type:"+type);
        if(type!=null&&type.contains("gif")){
            baseGif(iv,Api.HOST_PIC+key).fitCenter().into(iv);
        }else{
            baseGlide(iv,Api.HOST_PIC+key).centerCrop().into(iv);
        }

    }
    public static void showSmallFullKey(ImageView iv , String fullKey,String type){
        if(type!=null&&type.contains("gif")){
            baseGif(iv,fullKey).fitCenter().into(iv);
        }else{
            baseGlide(iv,fullKey).centerCrop().into(iv);
        }
    }

    public static void showSmall(ImageView iv , String fullKey){
//        LogUtils.d("type:"+type);
            baseGlide(iv,fullKey).centerCrop().into(iv);
    }
    /**
     * 展示全屏大图
     */
    private static final int MAX_HEIGHT_HEIGHT = 4096;
    public static void showBig(final PhotoView iv , DataBean.PicBean pic){
        resizeImage(iv,pic);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        if(getType(pic.file.type).contains("gif")){
            //iv.setZoomable(false);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iv.getLayoutParams();
            params.gravity = Gravity.CENTER;
            iv.setLayoutParams(params);
            baseGif(iv,Api.HOST_PIC+pic.file.key).into(iv);
        }else{
            iv.setZoomable(true);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(iv.getContext())
                    .load(Api.HOST_PIC+pic.file.key)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(getRandomColor())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onStart() {
                            iv.setImageResource(getRandomColor());
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            SoftReference<Bitmap> old = new SoftReference<Bitmap>(resource);
                            resource = null;
                            int oldWidth = old.get().getWidth();
                            int oldHeight = old.get().getHeight();
//                            LogUtils.d("size of big :"+oldHeight+"*"+oldWidth);
//                            LogUtils.d("size:"+old.get().getByteCount());
                            try {
                                if (oldWidth < oldHeight && oldHeight > MAX_HEIGHT_HEIGHT) {
                                    iv.setImageBitmap(Bitmap.createScaledBitmap(old.get(), oldWidth * MAX_HEIGHT_HEIGHT / oldHeight, MAX_HEIGHT_HEIGHT, true));
                                } else if (oldWidth > oldHeight && oldWidth > MAX_HEIGHT_HEIGHT) {
                                    iv.setImageBitmap(Bitmap.createScaledBitmap(old.get(), MAX_HEIGHT_HEIGHT, oldHeight * MAX_HEIGHT_HEIGHT / oldWidth, true));
                                } else {
                                    iv.setImageBitmap(old.get());
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }

    /**展示头像*/
    public static void showAvatar(CircularImageView iv , String url){
        Glide.with(iv.getContext())
                .load(url)
                .crossFade(150)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.front_default)
                .transform(new GlideCircleTransform(iv.getContext()))
                .into(iv);
//        iv.setBorderWidth(DPUtils.dip2px(iv.getContext(),2));
//        iv.setBorderColor(iv.getContext().getResources().getColor(R.color.white));
    }
    /**展示启动页*/
    public static void showWelcomeCover(ImageView iv , String url){
        Glide.with(iv.getContext())
                .load(url)
                .crossFade(150)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);
    }
    /**
     * 下载图片，从缓存中复制
     */
    public static File copyBytesFromCache(String url){
        File file = null;
        try {
            file = Glide.with(MyApplication.getInstance())
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if(file==null){
            return null;
        }else {
            return file;
        }
    }

    /**
     * 显示大图时重新计算图片的大小
     * 宽同屏幕
     * 高按原图比例计算
     */
    public static void resizeImage(View iv, DataBean.PicBean  pic){
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        //获取屏幕宽高
        DisplayMetrics dm =iv.getContext().getResources().getDisplayMetrics();
        params.width = dm.widthPixels;
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = iv.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = iv.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        //高小于屏幕的 与屏幕同高，大于的按图片高
        double times= (dm.widthPixels+0.0)/(pic.file.width +0.0);
        double resizeHeight = pic.file.height*times;
        if(resizeHeight<=dm.heightPixels && !getType(pic.file.type).contains("gif")){
            params.height = dm.heightPixels - statusBarHeight1;
        }else{
            params.height = (int) resizeHeight;
            if(resizeHeight-params.height>=0.5){
                params.height+=1;
            }
        }
//        LogUtils.d("=======================================");
//        LogUtils.d("图宽："+pic.file.width+"图高："+pic.file.height);
//        LogUtils.d("屏幕宽："+dm.widthPixels+"屏幕高："+dm.heightPixels);
//        LogUtils.d("图片宽："+params.width+"图片高："+params.height);
//        LogUtils.d("=======================================");
        iv.setLayoutParams(params);
    }

    //转换为圆形的图片
    public static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private  Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }

    public static void clearCache(){
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Glide.get(MyApplication.getInstance()).clearDiskCache();
                File cache = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+Constant.DIR_SHARE);
                if(cache.isDirectory()&&cache.exists()) {
                    File[] files = cache.listFiles();
                    if(files==null) return;
                    for (int i=0;i<files.length;i++){
                        files[i].delete();
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static int getCacheSize(){
        File cacheDir = Glide.getPhotoCacheDir(MyApplication.getInstance());
        LogUtils.d("" + cacheDir.length());
        LogUtils.d("" + Formatter.formatFileSize(MyApplication.getInstance(), cacheDir.length()));
        long size = 0;
        try {
            size = getFolderSize(cacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) (size/(1024*1024));
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getType(String type){
        if(TextUtils.isEmpty(type)) return "jpeg";
        if(type.contains("jpeg")){
            return "jpeg";
        }else if(type.contains("jpg")){
            return "jpg";
        }else if(type.contains("gif")){
            return "gif";
        }else if(type.contains("png")){
            return "png";
        }else {
            return "jpeg";
        }
    }
}
