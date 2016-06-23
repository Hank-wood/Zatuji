package com.joe.zatuji.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.UpdateBean;
import com.joe.zatuji.view.base.BaseDialog;

/**
 * Created by joe on 16/6/3.
 */
public class MessageDialog extends BaseDialog{

    private TextView mTitle;
    private TextView mContent;
    private TextView mCancel;
    private TextView mComplete;

    public MessageDialog(Context context){
        super(context);
    }
    public MessageDialog(Context context, String title, String content) {
        super(context);
        mTitle.setText(title);
        mContent.setText(content);
    }
    @Override
    protected int getLayout() {
        return R.layout.dialog_show_msg;
    }

    @Override
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title_msg_dialog);
        mContent = (TextView) findViewById(R.id.tv_content_msg_dialog);
        mCancel = (TextView) findViewById(R.id.tv_cancel_msg_dialog);
        mComplete = (TextView) findViewById(R.id.tv_complete_msg_dialog);
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
                if(mOnConfirmListener!=null) mOnConfirmListener.onConfirm();
                dismiss();
            }
        });
    }

    public void setTitleAndContent(String title,String content){
        mTitle.setText(title);
        mContent.setText(content);
    }

    public void disableCancel(){
        mCancel.setVisibility(View.GONE);
    }

    public void setonConfirmListener(onConfirmListener listener) {
        this.mOnConfirmListener = listener;
    }

    private onConfirmListener mOnConfirmListener;

    public interface onConfirmListener{
        void onConfirm();
    }
}
