package com.joe.zatuji.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.UpdateBean;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.utils.NetWorkUtils;
import com.joe.zatuji.view.base.BaseDialog;

/**
 * Created by joe on 16/6/23.
 */
public class UpdateDialog extends BaseDialog{

    private ImageView mNoWifi;
    private TextView mTitle;
    private TextView mUpdateLog;
    private CheckBox mIgnore;
    private Button mUpdate;
    private Button mCancle;
    private UpdateBean mUpdateBean;
    public UpdateDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_update;
    }

    @Override
    protected void initView() {
        mNoWifi = (ImageView) findViewById(R.id.update_wifi_indicator);
        mTitle = (TextView) findViewById(R.id.update_title);
        mUpdateLog = (TextView) findViewById(R.id.update_content);
        mIgnore = (CheckBox) findViewById(R.id.update_id_ignore);
        mUpdate = (Button) findViewById(R.id.update_id_ok);
        mCancle = (Button) findViewById(R.id.update_id_cancel);
    }

    @Override
    protected void initListener() {
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIgnore.isChecked()) SettingHelper.setIgnoreVersion(mUpdateBean.version_i);
                UpdateDialog.this.dismiss();
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnUpdateListener!=null) mOnUpdateListener.onUpdate(mUpdateBean);
                UpdateDialog.this.dismiss();
            }
        });
    }
    private void showInfo(){
        if(NetWorkUtils.getNetType(mContext)!= NetWorkUtils.TYPE_WIFI) mNoWifi.setVisibility(View.VISIBLE);
        String log = mUpdateBean.update_log.replace(";","\n");
        mTitle.setText(mUpdateBean.version);
        mUpdateLog.setText(log);
    }
    public void setmUpdateBean(UpdateBean mUpdateBean) {
        this.mUpdateBean = mUpdateBean;
        showInfo();
    }

    public void setmOnUpdateListener(onUpdateListener mOnUpdateListener) {
        this.mOnUpdateListener = mOnUpdateListener;
    }

    private onUpdateListener mOnUpdateListener;

    public interface onUpdateListener{
        void onUpdate(UpdateBean updateBean);
    }
}
