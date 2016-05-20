package com.joe.zatuji.ui.loginpager.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.R;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.BaseFragment;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.ui.loginpager.model.User;
import com.joe.zatuji.ui.loginpager.presenter.RegisterPresenter;
import com.joe.zatuji.ui.loginpager.view.RegisterView;
import com.yongchun.library.view.ImageSelectorActivity;

import org.xutils.x;


/**
 * Created by Joe on 2016/5/1.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener,RegisterView{

    private CircularImageView mAvatar;
    private String mAvatarPath;
    private EditText mAccount;
    private EditText mNickName;
    private EditText mPwd;
    private RegisterPresenter mPresenter;
    private LoadingView mLoadingView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterPresenter(mActivity,this);
    }

    @Override
    protected void initView() {
        mLoadingView = (LoadingView) mActivity;
        mAvatar = (CircularImageView) mRootView.findViewById(R.id.iv_register_avatar);
        mAvatar.setOnClickListener(this);
        mAccount = (EditText) mRootView.findViewById(R.id.et_register_user);
        mNickName = (EditText) mRootView.findViewById(R.id.et_register_nick);
        mPwd = (EditText) mRootView.findViewById(R.id.et_register_pwd);
        mRootView.findViewById(R.id.bt_register_register).setOnClickListener(this);
        mAvatarPath = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_register_avatar:
                ImageSelectorActivity.start(mActivity, 1, ImageSelectorActivity.MODE_SINGLE, true,true,true);
                break;
            case R.id.bt_register_register:
                register();
                break;
        }
    }

    private void register() {
        String userName = mAccount.getText().toString();
        String nickName = mNickName.getText().toString();
        String password = mPwd.getText().toString();
        if(TextUtils.isEmpty(userName)){
            KToast.show("用户名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(nickName)){
            KToast.show("昵称不能为空！");
            return;
        }
        if(TextUtils.isEmpty(password)){
            KToast.show("密码不能为空！");
            return;
        }
        User user = new User();
        user.setUsername(userName);
        user.setNickname(nickName);
        user.setPassword(password);
        if(userName.contains("@")) user.setEmail(userName);
        //showLoading();
        mLoadingView.showLoading();
        mPresenter.register(user,mAvatarPath);

    }

    public void setAvatar(String path){
        mAvatarPath = path;
        mAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        x.image().bind(mAvatar,path);
    }

    @Override
    public void onSuccess() {
        //注册成功自动登陆
        mLoadingView.doneLoading();
        Intent intent = new Intent();
        intent.setAction(Constant.LOGIN_SUCCESS);
        mActivity.sendBroadcast(intent);
        mActivity.finish();
    }

    @Override
    public void onError(String msg) {
        mLoadingView.doneLoading();
        KToast.show(msg);
    }
}
