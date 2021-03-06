package com.joe.zatuji.module.welcomepage;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.zatuji.Constant;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.BaseListBean;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.data.bean.WelcomeCover;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.GsonHelper;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.helper.TableHelper;
import com.joe.zatuji.module.homepage.HomeActivity;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
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

    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        TextView mVersion = (TextView) findViewById(R.id.tv_version);
        mIvBg = (ImageView) findViewById(R.id.bg_welcome);
        ImageHelper.showWelcomeCover(mIvBg,PrefUtils.getString(Constant.WELCOME_COVER,""));
        mVersion.setText("杂图集 "+MyApplication.getInstance().getVersionName());
        rxJavaManager = new RxJavaManager();
        //如果是用户手动退出 则不自动登录
        start = System.currentTimeMillis();
        if(Constant.IS_DEBUG||SettingHelper.isDebug()){
            getDebugCover();
        }else {
            getCover();
        }

    }


    private void getCover(){
        rxJavaManager.add(Api.getInstance()
                .mBmobService
                .getWelcomeCover(GsonHelper.toJsonObject(new WelcomeCover()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<WelcomeCover>() {
                    @Override
                    public void onError(ResultException e) {
                        LogUtils.d("cover error:"+e.getMessage());
                        toHome();
                    }

                    @Override
                    public void onNext(WelcomeCover welcomeCover) {
                        if(welcomeCover!=null&&!TextUtils.isEmpty(welcomeCover.result)){
                            ImageHelper.showWelcomeCover(mIvBg,welcomeCover.result);
                            PrefUtils.putString(Constant.WELCOME_COVER,welcomeCover.result);
                        }
                        toHome();
                    }
                })
        );
    }

    public  void getDebugCover(){
        rxJavaManager.add(Api.getInstance()
                .mBmobService
                .getWelcomeCoverDebug(GsonHelper.toJsonObject(new WelcomeCover()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BmobSubscriber<WelcomeCover>() {
                    @Override
                    public void onError(ResultException e) {
                        LogUtils.d("cover error:"+e.getMessage());
                        toHome();
                    }

                    @Override
                    public void onNext(WelcomeCover welcomeCover) {
                        if(welcomeCover!=null&&!TextUtils.isEmpty(welcomeCover.result)){
                            ImageHelper.showWelcomeCover(mIvBg,welcomeCover.result);
                            PrefUtils.putString(Constant.WELCOME_COVER,welcomeCover.result);
                        }
                        toHome();
                    }
                })
        );
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
