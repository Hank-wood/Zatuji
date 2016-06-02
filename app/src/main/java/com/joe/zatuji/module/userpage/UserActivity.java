package com.joe.zatuji.module.userpage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

/**
 * 用户中心
 * Created by joe on 16/6/2.
 */
public class UserActivity extends BaseActivity<UserPresenter> implements UserView,View.OnClickListener{

    private ActionBar mActionbar;
    private CircularImageView mAvatar;
    private TextView mEmail;
    private TextView mNick;

    @Override
    protected int getLayout() {
        return R.layout.activity_user;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionbar = getSupportActionBar();
        mActionbar.setTitle("个人资料");
        mActionbar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.ll_avatar_user).setOnClickListener(this);
        findViewById(R.id.ll_nick_user).setOnClickListener(this);
        findViewById(R.id.ll_email_user).setOnClickListener(this);
        findViewById(R.id.ll_pwd_user).setOnClickListener(this);

        mAvatar = (CircularImageView) findViewById(R.id.iv_avatar_user);
        mNick = (TextView) findViewById(R.id.tv_nick_user);
        mEmail = (TextView) findViewById(R.id.tv_email_user);
        setUserInfo();
    }

    private void setUserInfo() {
        ImageHelper.showAvatar(mAvatar,MyApplication.mUser.avatar);
        mNick.setText(MyApplication.mUser.nickname);
        if(TextUtils.isEmpty(MyApplication.mUser.email)) {
            mEmail.setText("暂未绑定");
            return;
        }
        mEmail.setText(MyApplication.mUser.email+"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return true;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_avatar_user:
                ImageSelectorActivity.start(mActivity, 1, ImageSelectorActivity.MODE_SINGLE, true,true,true);
                break;
            case R.id.ll_nick_user:
                break;
            case R.id.ll_email_user:
                break;
            case R.id.ll_pwd_user:
                break;
        }
    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    public void updateAvatar(String url) {
        doneLoading();
        ImageHelper.showAvatar(mAvatar,url);
    }

    @Override
    public void updateNickName(String nickname) {
        doneLoading();
        mNick.setText(nickname);
    }

    @Override
    public void updateEmail(String email) {
        doneLoading();
        mEmail.setText(email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == mActivity.RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            if(images.size()>0) {
                String mAvatarPath = images.get(0);
                User user = MyApplication.mUser;
                showLoading("上传头像...");
                mPresenter.changeAvatar(mAvatarPath,user);
            }
        }
    }
}
