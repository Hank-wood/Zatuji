package com.joe.zatuji.module.picdetailpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.data.bean.Pointer;
import com.joe.zatuji.data.bean.Relation;
import com.joe.zatuji.data.bean.RelationQuery;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.TableHelper;
import com.joe.zatuji.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by joe on 16/6/2.
 */
public class PicDetailModel implements BaseModel {
    /**
     * { \
     "$inQuery": { \
     "where": { \
     "username": "Lily" \
     }
     * */
    public Observable<BaseListBean<MyFavorite>> alreadyToFavorite(FavoriteTag tag, MyFavorite img){
        TableHelper<BaseListBean<MyFavorite>> helper = new TableHelper<>();
        RelationQuery query = new RelationQuery();
        query.$relatedTo.key = "img";
        query.$relatedTo.object = new Pointer("FavoriteTag",tag.objectId);
        Map<String,Object> map = new HashMap<>();
        map.put("img_url",img.img_url);
        map.put("$relatedTo",query.$relatedTo);
        Type clazz = new TypeToken<BaseListBean<MyFavorite>>(){}.getType();
        return helper.query("MyFavorite",GsonHelper.toJsonObject(map),clazz,"img_url");
    }

    public Observable<BaseBmobBean> createTagAndSave(final FavoriteTag tag, final MyFavorite img, final String userId){
        return Api.getInstance().mBmobService.add("FavoriteTag",GsonHelper.toJsonObject(tag))
                .flatMap(new Func1<BaseBmobBean, Observable<BaseBmobBean>>() {
                    @Override
                    public Observable<BaseBmobBean> call(BaseBmobBean baseBmobBean) {
                        final String tagId = baseBmobBean.objectId;
                        Relation add = new Relation();
                        add.objects.add(new Pointer("FavoriteTag",tagId));
                        add.add();
                        Map<String,Relation> map = new HashMap<String, Relation>();
                        map.put("tag",add);
                        return Api.getInstance().mBmobService.update("_User",userId,GsonHelper.toJsonObject(map))
                                .flatMap(new Func1<BaseBmobBean, Observable<BaseBmobBean>>() {
                                    @Override
                                    public Observable<BaseBmobBean> call(BaseBmobBean baseBmobBean) {
                                        tag.objectId = tagId;
                                        return saveFavoriteImg(tag,img);
                                    }
                                });
                    }
                });
    }

    public Observable<BaseBmobBean> saveFavoriteImg(final FavoriteTag tag, final MyFavorite img){
        Map query = new HashMap();
        query.put("img_url",img.img_url);
        Type clazz = new TypeToken<BaseListBean<MyFavorite>>(){}.getType();
        return new TableHelper<BaseListBean<MyFavorite>>()
                .query("MyFavorite",GsonHelper.toJsonObject(query),clazz,"img_url")
                .flatMap(new Func1<BaseListBean<MyFavorite>, Observable<BaseBmobBean>>() {
                    @Override
                    public Observable<BaseBmobBean> call(BaseListBean<MyFavorite> myFavoriteBaseListBean) {
                        if(myFavoriteBaseListBean.results!=null && myFavoriteBaseListBean.results.size()>0){
                            MyFavorite i = myFavoriteBaseListBean.results.get(0);
                            Relation relation = new Relation();
                            relation.objects.add(new Pointer("MyFavorite",i.objectId));
                            relation.add();
                            FavoriteTag tag1= new FavoriteTag();
                            tag1.img = relation;
                            tag1.number=tag.number+1;
                            tag1.is_lock =tag.is_lock;
                            if(TextUtils.isEmpty(tag.front)) tag1.front = i.img_url;
                            return Api.getInstance().mBmobService.update("FavoriteTag",tag.objectId,GsonHelper.toJsonObject(tag1));
                        }else{
                            return Api.getInstance().mBmobService.add("MyFavorite",GsonHelper.toJsonObject(img))
                                    .flatMap(new Func1<BaseBmobBean, Observable<BaseBmobBean>>() {
                                        @Override
                                        public Observable<BaseBmobBean> call(BaseBmobBean baseBmobBean) {
                                            Relation relation = new Relation();
                                            relation.objects.add(new Pointer("MyFavorite",baseBmobBean.objectId));
                                            relation.add();
                                            FavoriteTag tag1= new FavoriteTag();
                                            tag1.img = relation;
                                            tag1.number=tag.number+1;
                                            tag1.is_lock =tag.is_lock;
                                            if(TextUtils.isEmpty(tag.front)) tag1.front = img.img_url;
                                            return Api.getInstance().mBmobService.update("FavoriteTag",tag.objectId,GsonHelper.toJsonObject(tag1));
                                        }
                                    });
                        }
                    }
                });

    }

    public boolean alreadySaved(String url) {
        if(new File(Environment.getExternalStorageDirectory()+"/"+ Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+url+".jpg").exists()) return true;
        return false;
    }

    public Observable<String> download(final String url){
        return Api.getInstance().mApiService.download(Api.HOST_PIC+url)
                .flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody responseBody) {
                        return toDisk(responseBody,url);
                    }
                });
    }

    public Observable<String> toDisk(final ResponseBody body, final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                boolean done = writeResponseBodyToDisk(body,url);
                if(done) {
                    subscriber.onNext("保存成功");
                }else{
                    subscriber.onError(new Throwable("保存失败"));
                }
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String url) {
        try {
            // todo change the file location/name according to your needs
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String name = dateFormat.format(now);
            File folder = new File(Environment.getExternalStorageDirectory()+"/"+Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD);
            if(!folder.exists()) folder.mkdirs();
            File avatar = new File(Environment.getExternalStorageDirectory()+"/"+Constant.DIR_APP+"/"+Constant.DIR_DOWNLOAD+"/"+url+".jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(avatar);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();
                updateGallery(avatar,name);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    private void updateGallery(File file,String fileName){
        try {
            MediaStore.Images.Media.insertImage(MyApplication.getInstance().getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        MyApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }
}
