package com.joe.zatuji.module.userpage;

import android.text.TextUtils;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.api.exception.ResultException;
import com.joe.zatuji.base.model.RxJavaManager;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.base.view.BaseView;
import com.joe.zatuji.data.BaseBmobBean;
import com.joe.zatuji.helper.BmobSubscriber;
import com.joe.zatuji.helper.UserHelper;
import com.joe.zatuji.utils.KToast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by joe on 16/6/2.
 */
public class ChangePasswordActivity extends BaseActivity implements BaseView{

    private RxJavaManager mRxJavaManager;

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
    /**检验是否符合修改条件*/
    private void check(){
        String oldPassword = "";
        String newPassword = "";
        String certify = "";
        if(TextUtils.isEmpty(oldPassword)) {
            showToastMsg("密码不能为空！");
            return;
        }else if (TextUtils.isEmpty(newPassword)||TextUtils.isEmpty(certify)){
            showToastMsg("新密码不能为空！");
            return;
        }else if(!newPassword.equals(certify)){
            showToastMsg("两次输入不一致！");
            return;
        }else if(!MyApplication.mUser.password.equals(oldPassword)){
            showToastMsg("原密码错误！");
            return;
        }
        changePwd(newPassword);
    }
    private void changePwd(final String newPassword){
        String userId=MyApplication.mUser.objectId;
        mRxJavaManager = new RxJavaManager();
        mRxJavaManager.add(new UserModel().changePassword(userId,newPassword)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BmobSubscriber<BaseBmobBean>() {
            @Override
            public void onError(ResultException e) {
                showToastMsg(e.getError());
            }

            @Override
            public void onNext(BaseBmobBean baseBmobBean) {
                MyApplication.mUser.password = newPassword;
                UserHelper.saveCurrentPwd(newPassword);
                showToastMsg("修改成功");
                mActivity.finish();
            }
        }));
    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxJavaManager.remove();
    }
}
