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
public class InputDialog extends BaseDialog{

    private TextView mTitle;
    private EditText mInput;
    private TextView mCancel;
    private TextView mComplete;

    public InputDialog(Context context,String title,String hint) {
        super(context);
        mTitle.setText(title);
        mInput.setHint(hint);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_input;
    }

    @Override
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title_input_dialog);
        mInput = (EditText) findViewById(R.id.et_input_dialog);
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
                    String input = mInput.getText().toString();
                    mListener.OnComplete(input,InputDialog.this);
                }
            }
        });
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void clearInput(){
        mInput.setText("");
    }

    private OnCompleteListener mListener;
    public void setOnCompleteListener(OnCompleteListener listener){
        this.mListener = listener;
    }
    public interface OnCompleteListener{
        void OnComplete(String input,InputDialog dialog);
    }
}
