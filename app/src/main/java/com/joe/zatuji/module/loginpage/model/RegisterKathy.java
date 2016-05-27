package com.joe.zatuji.module.loginpage.model;

import android.content.Context;

import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.loginpage.presenter.RegisterListener;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Joe on 2016/5/1.
 */
public class RegisterKathy {
    private Context mContext;
    private User mUser;
    private RegisterListener mListener;

    public RegisterKathy(Context mContext, RegisterListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public void register(User user, String avatarPath){
        mUser=user;
        LogUtils.d("开始注册");
        upLoadAvatar(avatarPath);
    }

    public void upLoadAvatar( String avatarPath){
        final BmobFile bmobFile = new BmobFile(new File(avatarPath));
        bmobFile.upload(mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                //文件的完整路径
                mUser.setAvatar(bmobFile.getFileUrl(mContext));
                LogUtils.d("上传文件成功"+bmobFile.getFileUrl(mContext));
                signUp(mUser);

            }

            @Override
            public void onFailure(int i, String s) {
                LogUtils.d("上传文失败");
                signUp(mUser);
            }
        });
    }

    public void signUp(User user){
        user.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtils.d("注册成功");
                mListener.onRegisterSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtils.d("注册失败");
                mListener.onRegisterError(s);
            }
        });
    }
}
