package com.joe.zatuji.ui.settingpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.ui.loginpage.LoginActivity;
import com.joe.zatuji.ui.settingpage.presenter.UserInfoPresenter;
import com.joe.zatuji.ui.settingpage.view.SettingView;
import com.joe.zatuji.ui.settingpage.view.UserView;

import org.xutils.x;

/**
 * Created by Joe on 2016/4/30.
 */
public class UserFragment extends BaseFragment implements UserView,View.OnClickListener{

    private UserInfoPresenter mPresenter;
    private TextView mLogin;
    private BroadcastReceiver receiver;
    private CircularImageView mAvatar;
    @Override
    protected int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initPresenter() {
        SettingView settingView = (SettingView) getParentFragment();
        mPresenter = new UserInfoPresenter(mActivity,this,settingView);
        mPresenter.getUserInfo();
    }

    @Override
    protected void initView() {
        mLogin = (TextView) mRootView.findViewById(R.id.tv_user_nick);
        mAvatar = (CircularImageView) mRootView.findViewById(R.id.iv_user_avatar);
        mLogin.setOnClickListener(this);
        initReceiver();
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getUserInfo();
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constant.LOGIN_SUCCESS);
        mActivity.registerReceiver(receiver,intentFilter);
    }

    @Override
    public void setAvatar(String url) {
        x.image().bind(mAvatar,url);
    }

    @Override
    public void setDefaultAvatar() {
        mAvatar.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public void setNick(String nick) {
        mLogin.setText(nick);
        mLogin.setClickable(false);
    }

    @Override
    public void setLoginOrSignup() {
        mLogin.setText("登陆/注册");
        mLogin.setClickable(true);
    }

    @Override
    public void showError(String errorMsg) {
        KToast.show(errorMsg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_user_nick:
                startActivity(new Intent(mActivity, LoginActivity.class));
        }
    }

    public void exit(){
        mPresenter.loginOut();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(receiver);
    }
}
