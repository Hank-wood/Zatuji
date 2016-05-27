package com.joe.zatuji.module.loginpage.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.R;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.loginpage.model.User;
import com.joe.zatuji.module.loginpage.presenter.LoginPresenter;
import com.joe.zatuji.module.loginpage.view.FragmentView;
import com.joe.zatuji.module.loginpage.view.LoginView;

import org.xutils.x;

/**
 * Created by Joe on 2016/5/1.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener,LoginView{

    private LoginPresenter mPresenter;
    private CircularImageView mAvatar;
    private EditText mAccount;
    private EditText mPwd;
    private LoadingView mLoadingView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initPresenter() {
        FragmentView fragmentView = (FragmentView) mActivity;
        mPresenter = new LoginPresenter(mActivity,this,fragmentView);
        mPresenter.getCurrentUser();
    }

    @Override
    protected void initView() {
        mLoadingView = (LoadingView) mActivity;
        mAvatar = (CircularImageView) mRootView.findViewById(R.id.iv_login_avatar);
        mAccount = (EditText) mRootView.findViewById(R.id.et_login_user);
        mPwd = (EditText) mRootView.findViewById(R.id.et_login_pwd);
        mRootView.findViewById(R.id.tv_login_forget).setOnClickListener(this);
        mRootView.findViewById(R.id.bt_login_sign).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_login_register).setOnClickListener(this);
    }

    @Override
    protected void initListener() {
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains(".com")) return;
                mPresenter.queryAvatar(s.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_forget:
                showForgetDialog();
                break;
            case R.id.bt_login_sign:
                login();
                break;
            case R.id.tv_login_register:
                mPresenter.jumpRegister();
                break;
        }
    }
    //忘记密码确认对话框
    private void showForgetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("重置密码？");
        builder.setMessage("请确认您的是邮箱是:"+mAccount.getText().toString()+",如果用户名不是邮箱，请在用户名处"+
        "填写您绑定的邮箱地址，点击确认我们将发送一份重置密码的邮件到该邮箱。");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoadingView.showLoading();
                LogUtils.d("重设密码");
                dialog.dismiss();
                mPresenter.forgotPwd(mAccount.getText().toString());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void login() {
        if(TextUtils.isEmpty(mAccount.getText().toString())||TextUtils.isEmpty(mPwd.getText().toString())){
            KToast.show("用户名和密码不能为空");
            return;
        }
        User user =new User();
        user.setUsername(mAccount.getText().toString());
        user.setPassword(mPwd.getText().toString());
        mLoadingView.showLoading();
        mPresenter.login(user);
    }

    @Override
    public void setAvatar(String url) {
        x.image().bind(mAvatar,url);
    }

    @Override
    public void setUserInfo(User user) {
        mAccount.setText(user.getUsername());
    }

    @Override
    public void doneLogin() {
        mLoadingView.doneLoading();
        Intent intent = new Intent();
        intent.setAction(Constant.LOGIN_SUCCESS);
        mActivity.sendBroadcast(intent);
        mActivity.finish();
    }

    @Override
    public void showErrorMsg(String msg) {
        mLoadingView.doneLoading();
        KToast.show(msg);
    }

    @Override
    public void resetPwdDone() {
        mLoadingView.doneLoading();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("重置密码？");
        builder.setMessage("重设密码邮件发送成功，请尽快修改密码");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void resetPwdError(String msg) {
        mLoadingView.doneLoading();
        KToast.show(msg);
    }
}
