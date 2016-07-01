package com.joe.zatuji.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.DPUtils;
import com.joe.zatuji.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/5/21.
 */
public class ImageHelper {
    /**
     * 普通图片显示
     */
    private static DrawableRequestBuilder<String> baseGlide(ImageView iv, String key){
//        LogUtils.d("show img");
        return Glide.with(iv.getContext())
                .load(key)
                .crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);
//                .error(null)
//                .placeholder(null);
    }
    /**
     * gif图片显示
     */
    private static GifRequestBuilder<String> baseGif(ImageView iv, String key){
        return Glide.with(iv.getContext())
                .load(key)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(null)
                .placeholder(null);
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
    public static void showBig(ImageView iv , DataBean.PicBean pic){
        resizeImage(iv,pic);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        if(pic.file.type.equals("gif")){
            baseGif(iv,Api.HOST_PIC+pic.file.key).into(iv);
        }else{
            baseGlide(iv,Api.HOST_PIC+pic.file.key).into(iv);
        }
    }

    public static void showScaleBig(final SubsamplingScaleImageView iv, final DataBean.PicBean pic){
        resizeImage(iv,pic);
        Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                File file = copyBytesFromCache(Api.HOST_PIC+pic.file.key);
                if(file!=null && file.exists()){
                    LogUtils.d("uri:"+Uri.fromFile(file));
                    subscriber.onNext(Uri.fromFile(file));
                }else{
                    subscriber.onError(new Throwable("加载大图失败"));
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        iv.setImage(ImageSource.uri(uri));
                    }
                });
    }
    public static void showAvatar(CircularImageView iv , String url){
        baseGlide(iv,url)
                .transform(new GlideCircleTransform(iv.getContext()))
                .into(iv);
        iv.setBorderWidth(DPUtils.dip2px(iv.getContext(),2));
        iv.setBorderColor(iv.getContext().getResources().getColor(R.color.white));
    }

    /**
     * 下载图片，从缓存中复制
     */
    public static File copyBytesFromCache(String url){
        LogUtils.d("cache:"+url);
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
        double times= (dm.widthPixels+0.0)/(pic.file.width +0.0);
        double resizeHeight = pic.file.height*times;
        params.height = (int) resizeHeight;
        if(resizeHeight-params.height>=0.5){
            params.height+=1;
        }
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
