package com.joe.zatuji.module.welcomepage;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.joe.zatuji.Constant;
import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.TableHelper;
import com.joe.zatuji.module.homepage.HomeActivity;
import com.joe.zatuji.module.loginpage.LoginAndRegisterModel;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.PrefUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by joe on 16/5/31.
 */
public class WelcomeActivity extends BaseActivity {
    long duration = 0 ;
    long start;
    long end;
    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        boolean isExit = PrefUtils.getBoolean(this,Constant.IS_EXIT,false);
        //如果是用户手动退出 则不自动登录
        start = System.currentTimeMillis();
        if(isExit){
            toHome();
        }else{
            autoLogin();
        }

    }

    private void autoLogin() {
        User user = new User();
        user.username = PrefUtils.getString(this,Constant.USER_NAME,"");
        user.password = PrefUtils.getString(this,Constant.PWD,"");
        if(!TextUtils.isEmpty(user.username)){
            final RxJavaManager rxJavaManager = new RxJavaManager();
            rxJavaManager.add(new LoginAndRegisterModel().login(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BmobSubscriber<User>() {
                @Override
                public void onError(ResultException e) {
                    toHome();
                }

                @Override
                public void onNext(User user) {
                    rxJavaManager.post(Event.LOGIN_SUCCESS,user);
                    toHome();
                }
            }));
        }else{
            toHome();
        }

    }
    private void toHome(){
        end = System.currentTimeMillis();
        duration = end - start;
        LogUtils.d("间隔："+duration);
        if(duration<2000 ) duration=2000-duration;
        LogUtils.d("计算后间隔："+duration);
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
}
