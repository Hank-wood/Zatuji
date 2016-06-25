package com.joe.zatuji.module.settingpage;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.DropMenuDialog;
import com.joe.zatuji.view.SettingView;

/**
 * Created by joe on 16/6/25.
 */
public class SettingActivity extends BaseActivity implements SettingView.OnCheckedChangeListener{

    private SettingView mTagSetting;
    private SettingView mClearSetting;
    private SettingView mUpdateSetting;
    private SettingView mNoWifiSetting;
    private SettingView mNotify;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        ActionBar mActionbar = getSupportActionBar();
        mActionbar.setTitle("偏好设置");
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mTagSetting = (SettingView) findViewById(R.id.setting_tag);
        mClearSetting = (SettingView) findViewById(R.id.setting_cache);
        mUpdateSetting = (SettingView) findViewById(R.id.setting_update);
        mNoWifiSetting = (SettingView) findViewById(R.id.setting_update_wifi);
        mNotify = (SettingView) findViewById(R.id.setting_no_wifi);
        TagBean mTag = new TagBean();
        mTagSetting.setHint(mTag.tagList.get(SettingHelper.getDefaultTag()).name);
        mClearSetting.setCheckedImmediately(SettingHelper.getAutoClear());
        mUpdateSetting.setCheckedImmediately(SettingHelper.isCheckUpdate());
        mNoWifiSetting.setCheckedImmediately(SettingHelper.isCheckUpdateWithNoWifi());
        mNotify.setCheckedImmediately(SettingHelper.isNotifyNoWifi());
    }

    @Override
    protected void initListener() {
        mTagSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagChoose();
            }
        });
        mClearSetting.setOnCheckedChangeListener(this);
        mUpdateSetting.setOnCheckedChangeListener(this);
        mNoWifiSetting.setOnCheckedChangeListener(this);
        mNotify.setOnCheckedChangeListener(this);
    }

    private void showTagChoose() {
        DropMenuDialog dialog = new DropMenuDialog(mActivity);
        dialog.setOnMenuClickListener(new DropMenuDialog.OnMenuClickListener() {
            @Override
            public void onMenuClick(TagBean.Tag tag) {
                SettingHelper.setDefaultTag(tag.position);
                MyApplication.getInstance().setDefaultTag();
                mTagSetting.setHint(tag.name);
            }
        });
        dialog.show();
    }

    @Override
    public void onChecked(int id, CompoundButton buttonView, boolean isChecked) {
        switch (id){
            case R.id.setting_cache:
                SettingHelper.setAutoClear(isChecked);
                break;
            case R.id.setting_update:
                SettingHelper.setCheckUpdate(isChecked);
                break;
            case R.id.setting_update_wifi:
                SettingHelper.setCheckUpdateWithNoWifi(isChecked);
                break;
            case R.id.setting_no_wifi:
                SettingHelper.setNotifyNoWifi(isChecked);
                break;
        }
    }
    @Override
    protected void initPresenter() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
