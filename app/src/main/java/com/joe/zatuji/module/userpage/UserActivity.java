package com.joe.zatuji.module.userpage;

import android.view.View;

import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.utils.KToast;

/**
 * Created by joe on 16/6/2.
 */
public class UserActivity extends BaseActivity<UserPresenter> implements UserView,View.OnClickListener{
    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    public void updateAvatar(String url) {

    }

    @Override
    public void updateNickName(String nickname) {

    }

    @Override
    public void updateEmail(String email) {

    }

}
