package com.joe.zatuji.favoritepage.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.joe.zatuji.R;
import com.joe.zatuji.favoritepage.model.FavoriteTag;
import com.joe.zatuji.global.utils.KToast;
import com.joe.zatuji.loginpager.model.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by Joe on 2016/5/1.
 */
public class CreateTagDialog extends Dialog implements View.OnClickListener{

    private EditText mTitle;
    private EditText mPwd;
    private Button mCreate;
    private Context context;

    public CreateTagDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CreateTagDialog(Context context) {
        this(context,R.style.dialog_theme);
        this.context = context;
        setContentView(R.layout.dialog_create_tag);

        initView();
    }


    private void initView() {
        mTitle = (EditText) findViewById(R.id.et_title_tag);
        mPwd = (EditText) findViewById(R.id.et_title_pwd);
        mCreate = (Button) findViewById(R.id.bt_create_tag);
        findViewById(R.id.bt_cancel_tag).setOnClickListener(this);
        mCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_create_tag:
                createTag();
                break;
            case R.id.bt_cancel_tag:
                this.dismiss();
                break;
        }
    }

    private void createTag() {
        FavoriteTag tag = new FavoriteTag();
        User user = BmobUser.getCurrentUser(context,User.class);
        String title = mTitle.getText().toString();
        String pwd = mPwd.getText().toString();
        if(TextUtils.isEmpty(title)){
            KToast.show("图集名称不能为空");
            return;
        }
        tag.setTag(title);
        tag.setBelong(user);
        tag.setNumber(0);
        if(!TextUtils.isEmpty(pwd)){
            tag.setIs_lock(true);
            tag.setPwd(pwd);
        }else{
            tag.setIs_lock(false);
        }
        mCallBack.OnCreate(tag);
        dismiss();
    }

    private OnCreateCallBack mCallBack;
    public void setOnCreateCallBack(OnCreateCallBack mCallBack){
        this.mCallBack = mCallBack;
    }
    public interface OnCreateCallBack{
        void OnCreate(FavoriteTag tag);
    }
}
