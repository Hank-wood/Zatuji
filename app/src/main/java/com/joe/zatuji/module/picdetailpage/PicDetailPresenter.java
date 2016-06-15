package com.joe.zatuji.module.picdetailpage;

import android.content.Context;

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
import com.joe.zatuji.module.favoritepage.FavoriteModel;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.PrefUtils;
import com.joe.zatuji.data.bean.User;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Joe on 2016/4/18.
 */
public class PicDetailPresenter extends BasePresenter<PicDetailView,PicDetailModel> {
    private Context context;
    private LoadingView loadingView;

//    public PicDetailPresenter(Context context, LoadingView loadingView){
//        this.context=context;
//        this.loadingView=loadingView;
//    }
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
    public void saveToPhone(String picUrl){
        if(mModel.alreadySaved(picUrl)){
            mView.showToastMsg("已经保存过咯～");
            return;
        }
        mRxJavaManager.add(mModel.download(picUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new RxSubscriber<String>() {
            @Override
            public void onError(Throwable e) {
                mView.showToastMsg(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mView.showToastMsg(s);
            }
        }));
    }
    public void share(String picUrl){

    }


    @Override
    public void onStart() {

    }
}