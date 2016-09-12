package com.joe.zatuji.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES10;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.LogUtils;

import java.io.File;
import java.util.Random;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片帮助类
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
                .placeholder(getRandomColor())
                .error(R.color.white);

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
                .placeholder(getRandomColor())
                .error(R.color.white);
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
    public static int sMaxTextureSize = -1;
    public static void showBig(PhotoView iv,DataBean.PicBean pic){
        showBig(iv, pic, null);
    }
    public static void showBig(final PhotoView iv , final DataBean.PicBean pic, final OnFinishListener listener){
        if(listener != null){
            listener.onStart();
        }
        resizeImage(iv,pic);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        if(getType(pic.file.type).contains("gif")){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)iv.getLayoutParams();
//            params.gravity = Gravity.CENTER;
            iv.setLayoutParams(params);
            baseGif(iv,Api.HOST_PIC+pic.file.key)
                    .listener(new RequestListener<String, GifDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                            if(listener!=null){
                                listener.onFinished(false);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if(listener!=null){
                                listener.onFinished(true);
                            }
                            return false;
                        }
                    })
                    .into(iv);
        }else{
            iv.setZoomable(true);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(iv.getContext())
                    .load(Api.HOST_PIC+pic.file.key)
                    .crossFade(300)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(getRandomColor())
                    .error(R.color.white)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if(listener!=null){
                                listener.onFinished(false);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            if(listener!=null){
                                listener.onFinished(true);
                            }
                            int oldHeight = resource.getIntrinsicHeight();
                            int oldWidth = resource.getIntrinsicWidth();
                            if(sMaxTextureSize <= 0){
                                sMaxTextureSize = getMaxTextureSize();
                            }
                            if(sMaxTextureSize == 0) return false;
                            if(oldHeight > sMaxTextureSize){
                                LogUtils.d("oldHeight:"+oldHeight+" Max Texture Size:"+sMaxTextureSize);
                                //TODO:cast exception:GlideDrawable can't be cast to BitmapDrawable
                                GlideBitmapDrawable bd = (GlideBitmapDrawable) resource;
                                iv.setImageBitmap(Bitmap.createScaledBitmap(bd.getBitmap(), bd.getBitmap().getWidth() * sMaxTextureSize / oldHeight, sMaxTextureSize, true));
                                return true;
                            }else if(oldWidth > sMaxTextureSize){
                                LogUtils.d("oldHeight:"+oldHeight+" Max Texture Size:"+sMaxTextureSize);
                                GlideBitmapDrawable bd = (GlideBitmapDrawable) resource;
                                iv.setImageBitmap(Bitmap.createScaledBitmap(bd.getBitmap(), bd.getBitmap().getHeight() * sMaxTextureSize / oldWidth, sMaxTextureSize, true));
                                return true;
                            }
                            return false;
                        }
                    })
                    .into(iv);

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
        return file;
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

    public static int getMaxTextureSize(){
        EGL10 egl = (EGL10) EGLContext.getEGL();

        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);

        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {
            // TROUBLE! No config found.
        }
        EGLConfig config = configs[0];

        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;  // missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }

    public interface OnFinishListener {
        void onStart();
        void onFinished(boolean success);
    }
}
