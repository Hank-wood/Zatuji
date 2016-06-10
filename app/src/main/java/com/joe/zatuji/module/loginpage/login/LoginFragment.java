package com.joe.zatuji.module.loginpage.login;

import android.content.DialogInterface;
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
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.module.loginpage.LoginActivity;
import com.joe.zatuji.utils.DoubleClick;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.view.MessageDialog;


/**
 * Created by Joe on 2016/5/1.
 */
public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginView,View.OnClickListener{

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
        mPresenter.setView(this);
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
                if(s.toString().length()<2) return;
                mPresenter.queryUserByName(s.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(DoubleClick.isDoubleClick(v.getId())) return;
        switch (v.getId()){
            case R.id.tv_login_forget:
                showForgetDialog();
                break;
            case R.id.bt_login_sign:
                login();
                break;
            case R.id.tv_login_register:
                LoginActivity loginActivity = (LoginActivity) mActivity;
                loginActivity.changeFragment(null);
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
                showLoading("正在重置");
                LogUtils.d("重设密码");
                dialog.dismiss();
                mPresenter.resetPassword(mAccount.getText().toString());
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
        user.username = mAccount.getText().toString();
        user.password = mPwd.getText().toString();
        showLoading("登录中...");
        mPresenter.login(user);
    }

    @Override
    public void setAvatar(String url) {
        ImageHelper.showAvatar(mAvatar,url);
    }

    @Override
    public void setUserInfo(User user) {
        mAccount.setText(user.username);
        mPwd.setText(user.password);
        mPresenter.queryUserByName(user.username);
    }

    @Override
    public void resetPwdDone() {
        doneLoading();
        MessageDialog dialog =new MessageDialog(mActivity,"重置密码成功","重设密码邮件发送成功，请尽快修改密码");
        dialog.disableCancel();
        dialog.show();
    }


    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }
}
