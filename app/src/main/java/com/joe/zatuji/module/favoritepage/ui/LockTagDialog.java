package com.joe.zatuji.module.favoritepage.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.module.favoritepage.model.FavoriteTag;
import com.joe.zatuji.utils.KToast;

/**
 * Created by Joe on 2016/5/1.
 */
public class LockTagDialog extends Dialog implements View.OnClickListener{

    private TextView mTitle;
    private EditText mPwd;
    private Button mCreate;
    private Context context;
    private FavoriteTag mTag;
    public LockTagDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LockTagDialog(Context context,FavoriteTag tag) {
        this(context,R.style.dialog_theme);
        this.context = context;
        this.mTag = tag;
        setContentView(R.layout.dialog_lock_tag);

        initView();
    }


    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title_tag);
        mTitle.setText(mTag.getTag());
        mPwd = (EditText) findViewById(R.id.et_pwd_tag);
        mCreate = (Button) findViewById(R.id.bt_create_tag);
        findViewById(R.id.bt_cancel_tag).setOnClickListener(this);
        mCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_create_tag:
                checkPassword();
                break;
            case R.id.bt_cancel_tag:
                this.dismiss();
                break;
        }
    }

    private void checkPassword() {
        String pwd = mPwd.getText().toString();
        if(pwd.equals(mTag.getPwd())){
            mListener.OnSuccess(this);
        }else{
            KToast.show("密码错误");
        }
    }

    private OnPwdListener mListener;
    public void setOnPwdListener(OnPwdListener mListener){
        this.mListener = mListener;
    }
    public interface OnPwdListener {
        void OnSuccess(LockTagDialog dialog);
    }
}
