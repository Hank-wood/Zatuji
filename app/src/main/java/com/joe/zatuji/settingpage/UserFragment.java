package com.joe.zatuji.settingpage;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.loginpager.LoginActivity;
import com.joe.zatuji.settingpage.presenter.UserInfoPresenter;
import com.joe.zatuji.settingpage.view.UserView;

/**
 * Created by Joe on 2016/4/30.
 */
public class UserFragment extends BaseFragment implements UserView,View.OnClickListener{

    private UserInfoPresenter mPresenter;
    private TextView mLogin;

    @Override
    protected int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new UserInfoPresenter(myApplication,this);
        mPresenter.getUserInfo();
    }

    @Override
    protected void initView() {
        mLogin = (TextView) mRootView.findViewById(R.id.tv_user_nick);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void setAvatar(String url) {

    }

    @Override
    public void setDefaultAvatar() {

    }

    @Override
    public void setNick(String nick) {

    }

    @Override
    public void setLoginOrSignup() {

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
}
