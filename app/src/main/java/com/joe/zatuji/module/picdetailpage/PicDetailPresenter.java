package com.joe.zatuji.module.picdetailpage;

import android.content.Context;
import android.net.Uri;

import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.BasePresenter;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.Constant;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.RxSubscriber;
import com.joe.zatuji.helper.ShareHelper;
import com.joe.zatuji.module.favoritepage.FavoriteModel;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.data.bean.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/4/18.
 */
public class PicDetailPresenter extends BasePresenter<PicDetailView,PicDetailModel> {
    private Context context;

    public void showTags(){
        mRxJavaManager.add(new FavoriteModel().getAllTags(MyApplication.mUser.objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseListBean<FavoriteTag>>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseListBean<FavoriteTag> favoriteTagBaseListBean) {
                    mView.showTag(favoriteTagBaseListBean.results);
            }
        }));
    }

    public void createTags(final FavoriteTag tag, MyFavorite img){
        mRxJavaManager.add(mModel.createTagAndSave(tag,img,MyApplication.mUser.objectId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                mView.showToastMsg("收藏成功");
                mRxJavaManager.post(Event.ADD_TAG,tag);
            }
        }));
    }

    public void chooseTagToSave(final FavoriteTag tag, final MyFavorite img){
        mRxJavaManager.add(mModel.alreadyToFavorite(tag,img)
        .flatMap(new Func1<BaseListBean<MyFavorite>, Observable<BaseBmobBean>>() {
            @Override
            public Observable<BaseBmobBean> call(BaseListBean<MyFavorite> myFavoriteBaseListBean) {
                if(myFavoriteBaseListBean.results!=null&&myFavoriteBaseListBean.results.size()>0){
                    return Observable.create(new Observable.OnSubscribe<BaseBmobBean>() {
                        @Override
                        public void call(Subscriber<? super BaseBmobBean> subscriber) {
                            BaseBmobBean baseBmobBean = new BaseBmobBean();
                            baseBmobBean.objectId = "-1";
                            subscriber.onNext(baseBmobBean);
                            subscriber.onCompleted();
                        }
                    });
                }else{
                    return mModel.saveFavoriteImg(tag,img);
                }

            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                mView.showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                if(baseBmobBean.objectId!=null && baseBmobBean.objectId.equals("-1")){
                    mView.showToastMsg("已经收藏过啦！");
                }else{
                    mView.showToastMsg("收藏成功！");
                    mRxJavaManager.post(Event.ADD_FAVORITE,baseBmobBean);
                }
            }
        }));
    }
    public void saveToPhone(String picUrl,String type){
        if(mModel.alreadySaved(picUrl)){
            mView.showToastMsg("已经保存过咯～");
            return;
        }
        mRxJavaManager.add(mModel.download(picUrl,type)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String name = dateFormat.format(now);
                        LogUtils.d("save:"+path);
                        mModel.updateGallery(new File(path),name);
                    }
                })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RxSubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                mView.showToastMsg(e.getMessage());
            }

            @Override
            public void onNext(String path) {
                mView.showToastMsg("保存成功");
            }
        }));
    }
    public void share(String picUrl,String type){
        mRxJavaManager.add(mModel.share(picUrl,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<String>() {
                    @Override
                    public void onError(ResultException e) {
                        mView.showToastMsg("分享失败");
                    }

                    @Override
                    public void onNext(String path) {
                        LogUtils.d("uri:"+ Uri.fromFile(new File(path)));
                        mView.showToastMsg("选择要分享的应用");
                        ShareHelper.share("分享自杂图集",Uri.fromFile(new File(path)),MyApplication.getInstance());
                    }
                }));
    }

    private boolean noMoreData = false;
    @Override
    public void onStart() {
        mRxJavaManager.subscribe(Event.LOAD_MORE_DONE, new Action1<Object>() {
            @Override
            public void call(Object data) {
                ArrayList list = (ArrayList) data;
                if(data ==null||list.size() == 0) {
                    noMoreData = true;
//                    mView.showToastMsg("加载更多图片失败～");
                }else if(list.get(0) instanceof MyFavorite){
                    LogUtils.d("gallery load more");
                    mView.addGallery(list);
                }else {
                    LogUtils.d("pics load more");
                    mView.addPics(list);
                }
            }
        });
    }

    public void getMoreData(){
        if(noMoreData) return;
        mRxJavaManager.post(Event.LOAD_MORE_DATA,null);
    }

    public void quiet(int position){
        mRxJavaManager.post(Event.QUITE_PIC_DETAIL,position);
    }
}
