package com.joe.zatuji.module.homesettingpage;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.module.homesettingpage.user.UserFragment;
import com.joe.zatuji.module.loginpage.LoginActivity;
import com.joe.zatuji.utils.DoubleClick;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;

/**
 * Created by Joe on 2016/4/18.
 */
public class HomeHomeSettingFragment extends BaseFragment<HomeSettingPresenter> implements HomeSettingView,View.OnClickListener{

    private TextView tvCache;
    private RelativeLayout mExit;
    private UserFragment userFragment;
    private static HomeHomeSettingFragment mInstance;
    public static synchronized HomeHomeSettingFragment getInstance(){
        if(mInstance==null){
            mInstance = new HomeHomeSettingFragment();
        }
        return mInstance;
    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
        mPresenter.setContext(mActivity);
        mPresenter.getCache(mActivity);
    }

    @Override
    protected void initView() {
        initUserInfo();
        tvCache = (TextView) mRootView.findViewById(R.id.tv_cache);
        mRootView.findViewById(R.id.card_clear_cache).setOnClickListener(this);
        mRootView.findViewById(R.id.card_about).setOnClickListener(this);
        mRootView.findViewById(R.id.card_feedback).setOnClickListener(this);
        mRootView.findViewById(R.id.card_update).setOnClickListener(this);
        mExit = (RelativeLayout) mRootView.findViewById(R.id.card_exit);
        mExit.setOnClickListener(this);
        if(MyApplication.isLogin()) {
            showExit();
        }else{
            hideExit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume Setting");
        if(mPresenter!=null) {
            mPresenter.getCache(mActivity);
        }
    }

    /**
     * 加载用户信息
     */
    private void initUserInfo() {
        userFragment = new UserFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_container_user, userFragment).commit();
    }

    @Override
    public void onClick(View v) {
        if(DoubleClick.isDoubleClick(v.getId())) return;
        switch (v.getId()){
            case R.id.card_clear_cache:
                showLoading("努力清扫中...");
                mPresenter.clearCache();
                break;
            case R.id.card_exit:
                showLoading("退出...");
                mPresenter.exit();
                break;

            case R.id.card_update:
                showLoading("新版在哪里...");
                mPresenter.checkUpdate();
                break;
        }
    }


    @Override
    public void showExit() {
        doneLoading();
        mExit.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExit() {
        doneLoading();
        mExit.setVisibility(View.GONE);
    }

    @Override
    public void showCache(String cache) {
        doneLoading();
        tvCache.setText(cache);
    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }
}
