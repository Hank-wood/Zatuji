package com.joe.zatuji.module.homesettingpage.user;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.data.bean.User;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.DoubleClick;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.module.loginpage.LoginActivity;


/**
 * Created by Joe on 2016/4/30.
 */
public class UserFragment extends BaseFragment<UserInfoPresenter> implements UserView,View.OnClickListener{

    private TextView mLogin;
    private CircularImageView mAvatar;
    @Override
    protected int getLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
        mPresenter.getUserInfo();
    }

    @Override
    protected void initView() {
        mLogin = (TextView) mRootView.findViewById(R.id.tv_user_nick);
        mAvatar = (CircularImageView) mRootView.findViewById(R.id.iv_user_avatar);
        mAvatar.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void showError(String errorMsg) {
        KToast.show(errorMsg);
    }

    @Override
    public void onClick(View v) {
        if(DoubleClick.isDoubleClick(v.getId())) return;
        switch (v.getId()){
            case R.id.tv_user_nick:
                startActivity(new Intent(mActivity, LoginActivity.class));
                break;
            case R.id.iv_user_avatar:
                break;
        }
    }


    @Override
    public void setUserInfo(User user) {
        doneLoading();
        ImageHelper.showAvatar(mAvatar,user.avatar);
        mAvatar.setClickable(true);
        mLogin.setText(user.nickname);
        mLogin.setClickable(false);
    }

    @Override
    public void setLoginStyle() {
        doneLoading();
        mAvatar.setImageResource(R.mipmap.ic_launcher);
        mLogin.setText("登陆/注册");
        mAvatar.setClickable(false);
        mLogin.setClickable(true);
    }

    @Override
    public void showToastMsg(String msg) {

    }

//    //public void exit(){
//        mPresenter.loginOut();
//    }
}
