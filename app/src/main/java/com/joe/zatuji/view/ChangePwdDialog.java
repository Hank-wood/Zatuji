package com.joe.zatuji.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.view.base.BaseDialog;

/**
 * Created by joe on 16/6/3.
 */
public class ChangePwdDialog extends BaseDialog{

    private EditText mOld;
    private TextView mCancel;
    private TextView mComplete;
    private EditText mNew;
    private EditText mConfirm;

    public ChangePwdDialog(Context context){
        super(context);
    }
    @Override
    protected int getLayout() {
        return R.layout.dialog_change_pwd;
    }

    @Override
    protected void initView() {
        mOld = (EditText) findViewById(R.id.et_old_pwd_dialog);
        mNew = (EditText) findViewById(R.id.et_new_pwd_dialog);
        mConfirm = (EditText) findViewById(R.id.et_confirm_dialog);
        mCancel = (TextView) findViewById(R.id.tv_cancel_input_dialog);
        mComplete = (TextView) findViewById(R.id.tv_complete_input_dialog);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    String input = mOld.getText().toString();
                    String newPwd = mNew.getText().toString();
                    String confirm = mConfirm.getText().toString();
                    mListener.OnComplete(input,newPwd,confirm,ChangePwdDialog.this);
                }
            }
        });
    }

    public void setTitleAndHint(String title,String hint){
        mOld.setHint(hint);
    }


    public void clearInput(){
        mOld.setText("");
        mNew.setText("");
        mConfirm.setText("");
    }

    private OnCompleteListener mListener;
    public void setOnCompleteListener(OnCompleteListener listener){
        this.mListener = listener;
    }
    public interface OnCompleteListener{
        void OnComplete(String old,String newPwd,String confirm, ChangePwdDialog dialog);
    }

    private int mType = -1;
    public void setType(int type){
        mType = type;
    }

    public int getType(){
        return this.mType;
    }
}
