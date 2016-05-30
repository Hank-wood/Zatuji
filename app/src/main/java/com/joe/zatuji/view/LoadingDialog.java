package com.joe.zatuji.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.view.base.BaseDialog;

/**
 * Created by joe on 16/5/27.
 */
public class LoadingDialog extends BaseDialog {

    private TextView mMsg;

    public LoadingDialog(Context context,String msg) {
        super(context,R.style.dialog_no_title);
        mMsg.setText(msg);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_loading;
    }

    protected void initView() {
        mMsg = (TextView) findViewById(R.id.tv_loading_dialog);
    }

    public void setMessage(String msg){
        mMsg.setText(msg);
    }


}
