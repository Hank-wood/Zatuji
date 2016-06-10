package com.joe.zatuji.module.loginpage.register;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.R;
import com.joe.zatuji.base.LoadingView;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.DoubleClick;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.data.bean.User;
import com.yongchun.library.view.ImageSelectorActivity;



/**
 * Created by Joe on 2016/5/1.
 */
public class RegisterFragment extends BaseFragment<com.joe.zatuji.module.loginpage.register.RegisterPresenter> implements View.OnClickListener, RegisterView{

    private CircularImageView mAvatar;
    private String mAvatarPath;
    private EditText mAccount;
    private EditText mNickName;
    private EditText mPwd;
    private LoadingView mLoadingView;
    private User mUser ;
    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
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
        if(DoubleClick.isDoubleClick(v.getId())) return;
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
        if(mUser==null) mUser = new User();
        mUser.username =userName;
        mUser.nickname=nickName;
        mUser.password=password;
        if(TextUtils.isEmpty(mUser.avatar)) mUser.avatar = Constant.DEFAULT_AVATAR;//如果没有上传，设置为默认
//        if(userName.contains("@")) mUser.email = userName;
        //showLoading();
        //mLoadingView.showLoading();
        showLoading("注册中...");
        mPresenter.register(mUser);

    }

    public void setAvatar(final String path){
        mAvatarPath = path;
        mAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageHelper.showAvatar(mAvatar,path);
        //上传头像
        showLoading("上传头像...");
        mPresenter.uploadAvatar(path);
    }


    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    public void setUserAvatar(String url,String cdn) {
        doneLoading();
        showToastMsg("上传成功");
        if(mUser==null) mUser = new User();
        mUser.avatar = url;
        mUser.cdn = cdn;
    }
}
