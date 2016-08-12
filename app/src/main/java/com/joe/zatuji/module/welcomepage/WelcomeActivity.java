package com.joe.zatuji.module.welcomepage;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.WelcomeCover;
import com.joe.zatuji.data.bean.WelcomeCoverResult;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.helper.ResourceHelper;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.module.homepage.HomeActivity;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by joe on 16/5/31.
 */
public class WelcomeActivity extends BaseActivity {
    long duration = 0 ;
    long start;
    long end;
    private RxJavaManager rxJavaManager;
    private ImageView mIvBg;
    private TextView mRecommend;
    private BmobSubscriber<WelcomeCoverResult> mSubscriber;

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        TextView mVersion = (TextView) findViewById(R.id.tv_version);
        mVersion.setText("杂图集 "+MyApplication.getInstance().getVersionName());
        mIvBg = (ImageView) findViewById(R.id.bg_welcome);
        mRecommend = (TextView) findViewById(R.id.tv_recommend_by);
        ImageHelper.showWelcomeCover(mIvBg,PrefUtils.getString(Constant.WELCOME_COVER,""));
        mRecommend.setText(ResourceHelper.getFormatString(R.string.welcome_recommend_by
                ,PrefUtils.getString(Constant.WELCOME_AUTHER," ")));
        initCover();

    }

    private void initCover() {
        rxJavaManager = new RxJavaManager();
        start = System.currentTimeMillis();
        mSubscriber = new BmobSubscriber<WelcomeCoverResult>() {
            @Override
            public void onError(ResultException e) {
                LogUtils.d("cover error:"+e.getMessage());
                toHome();
            }

            @Override
            public void onNext(WelcomeCoverResult result) {
                if(result.result.contains("\\")) result.result = result.result.replace("\\","");
                WelcomeCover cover = GsonHelper.fromJson(result.result,WelcomeCover.class);
                if(cover!=null&&!TextUtils.isEmpty(cover.url)){
                    ImageHelper.showWelcomeCover(mIvBg,cover.url);
                    mRecommend.setText(ResourceHelper.getFormatString(R.string.welcome_recommend_by,cover.by));
                    PrefUtils.putString(Constant.WELCOME_COVER,cover.url);
                    PrefUtils.putString(Constant.WELCOME_AUTHER,cover.by);
                }
                toHome();
            }
        };
        showCover();
    }

    private void showCover() {
        Observable<WelcomeCoverResult> observable;
        Map<String,Integer> map = new HashMap<>();
        map.put("version",MyApplication.getInstance().getVersionCode());
        if(Constant.IS_DEBUG||SettingHelper.isDebug()){
            observable = getDebugCover(map);
        }else {
            observable = getCover(map);
        }
        rxJavaManager.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber)
        );
    }


    /**
     * 正式接口
     */
    private Observable<WelcomeCoverResult> getCover(Map body){
        return Api.getInstance().mBmobService
                .getWelcomeCover(GsonHelper.toJsonObject(body));
    }

    /**
     * 测试接口
     */
    public  Observable<WelcomeCoverResult> getDebugCover(Map body){
        return Api.getInstance().mBmobService
                .getWelcomeCoverDebug(GsonHelper.toJsonObject(body));
    }
    private void toHome(){
        end = System.currentTimeMillis();
        duration = end - start;
        if(duration<3000 ) duration=3000-duration;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                WelcomeActivity.this.finish();
            }
        },duration);
    }
    @Override
    protected void initPresenter() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxJavaManager.remove();
    }
}
