package com.joe.zatuji.module.userpage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.helper.UserHelper;
import com.joe.zatuji.utils.CheckUitls;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.ChangePwdDialog;
import com.joe.zatuji.view.InputDialog;
import com.joe.zatuji.view.MessageDialog;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

/**
 * 用户中心
 * Created by joe on 16/6/2.
 */
public class UserActivity extends BaseActivity<UserPresenter> implements UserView, View.OnClickListener, InputDialog.OnCompleteListener, ChangePwdDialog.OnCompleteListener {

    private ActionBar mActionbar;
    private CircularImageView mAvatar;
    private TextView mEmail;
    private TextView mNick;
    private InputDialog mInputDialog;
    private ChangePwdDialog mChangePwdDialog;
    private final int TYPE_NICK = 0;
    private final int TYPE_EMAIL = 1;

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
        mInputDialog = new InputDialog(mActivity);
        mChangePwdDialog = new ChangePwdDialog(mActivity);
        mChangePwdDialog.setOnCompleteListener(this);
        mInputDialog.setOnCompleteListener(this);
        mAvatar = (CircularImageView) findViewById(R.id.iv_avatar_user);
        mNick = (TextView) findViewById(R.id.tv_nick_user);
        mEmail = (TextView) findViewById(R.id.tv_email_user);
        setUserInfo();
    }

    private void setUserInfo() {
        ImageHelper.showAvatar(mAvatar, MyApplication.mUser.avatar);
        mNick.setText(MyApplication.mUser.nickname);
        if (TextUtils.isEmpty(MyApplication.mUser.email)) {
            mEmail.setText("暂未绑定");
            return;
        }
        mEmail.setText(MyApplication.mUser.email + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
        switch (v.getId()) {
            case R.id.ll_avatar_user:
                ImageSelectorActivity.start(mActivity, 1, ImageSelectorActivity.MODE_SINGLE, true, true, true);
                break;
            case R.id.ll_nick_user:
                showInputDialog("修改昵称", "输入新的昵称", TYPE_NICK);
                break;
            case R.id.ll_email_user:
                showInputDialog("绑定邮箱", "输入邮箱地址", TYPE_EMAIL);
                break;
            case R.id.ll_pwd_user:
                mChangePwdDialog.clearInput();
                mChangePwdDialog.show();
                break;
        }
    }

    public void showInputDialog(String title, String hint, int type) {
        mInputDialog.setTitleAndHint(title, hint);
        mInputDialog.setType(type);
        mInputDialog.clearInput();
        mInputDialog.show();
    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    public void updateAvatar(String url) {
        doneLoading();
        ImageHelper.showAvatar(mAvatar, url);
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
        LogUtils.d("update email");
        MessageDialog dialog =new MessageDialog(mActivity,"验证邮箱","邮箱绑定成功，系统已发送了一封验证邮件到 "+email+" 的邮箱中，请尽快完成验证，谢谢。");
        dialog.disableCancel();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            if (images.size() > 0) {
                String mAvatarPath = images.get(0);
                User user = MyApplication.mUser;
                showLoading("上传头像...");
                mPresenter.changeAvatar(mAvatarPath, user);
            }
        }
    }

    /**
     * 输入完成
     */
    @Override
    public void OnComplete(String input, InputDialog dialog) {
        if (TextUtils.isEmpty(input)) {
            showToastMsg("输入内容不能为空噢～");
            return;
        }
        switch (dialog.getType()) {
            case TYPE_NICK:
                User user1 = MyApplication.mUser;
                user1.nickname = input;
                mPresenter.changeNick(user1);
                break;
            case TYPE_EMAIL:
                if (!CheckUitls.isEmailFormat(input)) {
                    showToastMsg("邮箱格式错误!");
                    return;
                }
                User user2 = MyApplication.mUser;
                user2.email = input;
                mPresenter.changeEmail(user2);
                break;
        }
        dialog.dismiss();
        showLoading();
    }

    /**
     * 修改密码
     */
    @Override
    public void OnComplete(String old, String newPwd, String confirm, ChangePwdDialog dialog) {
        if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirm)) {
            showToastMsg("密码不能为空");
        } else if (!newPwd.equals(confirm)) {
            showToastMsg("两次密码输入不一致！");
        } else if (!old.equals(UserHelper.getCurrentUserPassword())) {
            showToastMsg("原始密码输入错误！");
        } else if (old.equals(newPwd)) {
            showToastMsg("新旧密码相同！");
        } else {
            User user = MyApplication.mUser;
            user.password = newPwd;
            mPresenter.changePWD(user);
            dialog.dismiss();
            showLoading("修改密码中...");
        }
    }
}
