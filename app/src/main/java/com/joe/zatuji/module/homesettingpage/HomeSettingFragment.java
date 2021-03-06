package com.joe.zatuji.module.homesettingpage;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.data.bean.FeedBackBean;
import com.joe.zatuji.module.aboutpage.AboutActivity;
import com.joe.zatuji.module.homesettingpage.user.UserFragment;
import com.joe.zatuji.utils.DoubleClick;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.InputDialog;

/**
 * Created by Joe on 2016/4/18.
 */
public class HomeSettingFragment extends BaseFragment<HomeSettingPresenter> implements HomeSettingView,View.OnClickListener{

    private TextView tvCache;
    private RelativeLayout mExit;
    private UserFragment userFragment;
    private String mFeedBack="";
    private static HomeSettingFragment mInstance;
    public static synchronized HomeSettingFragment getInstance(){
        if(mInstance==null){
            mInstance = new HomeSettingFragment();
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
            case R.id.card_clear_cache://缓存
                showLoading("努力清扫中...");
                mPresenter.clearCache();
                break;
            case R.id.card_about://关于
                startActivity(new Intent(mActivity, AboutActivity.class));
                break;
            case R.id.card_feedback:
                showFeedBack();
                break;
            case R.id.card_update://更新
                showLoading("新版在哪里...");
                mPresenter.checkUpdate();
                break;
            case R.id.card_exit://退出
                showLoading("退出...");
                mPresenter.exit();
                break;
        }
    }

    private void showFeedBack() {
        InputDialog dialog = new InputDialog(mActivity,"意见反馈","请输入您宝贵的建议");
        dialog.setContent(mFeedBack);
        final FeedBackBean feedBack = new FeedBackBean();
        feedBack.type = "反馈";
        feedBack.version = MyApplication.getInstance().getVersionName();
        if(MyApplication.mUser!=null){
            feedBack.user_id = MyApplication.mUser.objectId;
            feedBack.user_email = MyApplication.mUser.email;
            feedBack.user_name = MyApplication.mUser.nickname;
        }
        feedBack.android_version = android.os.Build.VERSION.SDK;
        dialog.setOnTextChangeWatcher(new InputDialog.OnTextWatcher() {
            @Override
            public void onChange(String text) {
                mFeedBack = text;
            }
        });
        dialog.setOnCompleteListener(new InputDialog.OnCompleteListener() {
            @Override
            public void OnComplete(String input, InputDialog dialog) {
                feedBack.content = input;
                mFeedBack = input;
                mPresenter.feedBack(feedBack);
                showLoading("正在上传反馈...");
                dialog.dismiss();
            }
        });
        dialog.show();
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
