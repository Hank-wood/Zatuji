package com.joe.zatuji.module.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jaeger.library.StatusBarUtil;
import com.joe.zatuji.Event;
import com.joe.zatuji.R;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.loginpage.ui.LoginFragment;
import com.joe.zatuji.module.loginpage.register.RegisterFragment;
import com.joe.zatuji.module.loginpage.view.FragmentView;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by Joe on 2016/4/30.
 */
public class LoginActivity extends BaseActivity implements FragmentView{
    private static final String TAG_LOGIN_FRAG = "login";
    private static final String TAG_RIGISTER_FRAG = "register";
    private FragmentManager mFragmentManager;
    private RegisterFragment mRegisterFragment;
    private RxJavaManager mRxJavaManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.BackgroundLogin));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initPresenter() {
        mRxJavaManager = new RxJavaManager();
        mRxJavaManager.subscribe(Event.LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object user) {
                finish();
            }
        });
    }

    private void initFragment() {
        LoginFragment mLoginFragment = new LoginFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transition= mFragmentManager.beginTransaction().add(R.id.fl_container_login, mLoginFragment,TAG_LOGIN_FRAG);

        transition.commit();
    }


    @Override
    public void changeFragment(String Tag) {
        mRegisterFragment = new RegisterFragment();
        FragmentTransaction transition= mFragmentManager.beginTransaction().add(R.id.fl_container_login, mRegisterFragment,TAG_RIGISTER_FRAG);
        transition.addToBackStack(TAG_RIGISTER_FRAG);
        transition.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == mActivity.RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            if(images.size()>0) {
                String mAvatarPath = images.get(0);
                mRegisterFragment.setAvatar(mAvatarPath);
                LogUtils.d(mAvatarPath);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRxJavaManager!=null){
            mRxJavaManager.remove();
        }
    }
}
