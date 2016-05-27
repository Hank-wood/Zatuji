package com.joe.zatuji.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.joe.zatuji.R;

/**
 * Created by joe on 16/5/27.
 */
public class LoadingDialog extends Dialog {

    private TextView mMsg;

    public LoadingDialog(Context context,String msg) {
        super(context);
        setContentView(R.layout.dialog_loading);
        initView();
        mMsg.setText(msg);
    }

    private void initView() {
        mMsg = (TextView) findViewById(R.id.tv_loading_dialog);
    }

    public void setMessage(String msg){
        mMsg.setText(msg);
    }


}
