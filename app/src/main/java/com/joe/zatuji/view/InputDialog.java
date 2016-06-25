package com.joe.zatuji.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

    public InputDialog(Context context){
        super(context);
    }
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
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mWatcher!= null) mWatcher.onChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void setTitleAndHint(String title,String hint){
        mTitle.setText(title);
        mInput.setHint(hint);
    }

    public void setContent(String content){
        mInput.setText(content);
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

    private int mType = -1;
    public void setType(int type){
        mType = type;
    }

    public int getType(){
        return this.mType;
    }

    private OnTextWatcher mWatcher;
    public void setOnTextChangeWatcher(OnTextWatcher watcher){
        this.mWatcher = watcher;
    }
    public interface OnTextWatcher{
        void onChange(String text);
    }
}
