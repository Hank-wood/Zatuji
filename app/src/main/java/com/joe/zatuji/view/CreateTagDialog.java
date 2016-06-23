package com.joe.zatuji.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.utils.KToast;


/**
 * Created by Joe on 2016/5/1.
 */
public class CreateTagDialog extends Dialog implements View.OnClickListener{

    private EditText mTitle;
    private EditText mPwd;
    private Button mCreate;
    private Context context;
    private TextView mDialogTitle;
    private FavoriteTag mTag;
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
        mDialogTitle = (TextView) findViewById(R.id.tv_dialog_create_title);
        mTitle = (EditText) findViewById(R.id.et_title_tag);
        mPwd = (EditText) findViewById(R.id.et_title_pwd);
        mCreate = (Button) findViewById(R.id.bt_create_tag);
        findViewById(R.id.bt_cancel_tag).setOnClickListener(this);
        mCreate.setOnClickListener(this);
    }

    public void showInfo(FavoriteTag tag){
        this.mTag = tag;
        mDialogTitle.setText("修改图集");
        mTitle.setText(tag.tag);
        mPwd.setText(tag.pwd);

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
        FavoriteTag tag = null;
        boolean isUpdate = false;
        if(mTag!=null) {
            tag = mTag;
            isUpdate = true;
        }else {
            tag = new FavoriteTag();
            tag.user_id = MyApplication.mUser.objectId;
            tag.number = 0;
        }

        String title = mTitle.getText().toString();
        String pwd = mPwd.getText().toString();
        if(TextUtils.isEmpty(title)){
            KToast.show("图集名称不能为空");
            return;
        }
        tag.tag = title;
        if(!TextUtils.isEmpty(pwd)){
            tag.is_lock = true;
            tag.pwd = pwd;
        }else{
            tag.is_lock = false;
        }
        if(isUpdate){
            mCallBack.onUpdate(tag);
        }else {
            mCallBack.OnCreate(tag);
        }
        dismiss();
    }

    private OnCreateCallBack mCallBack;
    public void setOnCreateCallBack(OnCreateCallBack mCallBack){
        this.mCallBack = mCallBack;
    }
    public interface OnCreateCallBack{
        void OnCreate(FavoriteTag tag);
        void onUpdate(FavoriteTag tag);
    }
}
