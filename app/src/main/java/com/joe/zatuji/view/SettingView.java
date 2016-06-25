package com.joe.zatuji.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by joe on 16/6/25.
 */
public class SettingView extends FrameLayout{
    private TextView mTitle;
    private TextView mDesc;
    private TextView mHint;
    private SwitchButton mSwitch;
    private final String NAMESPACE="http://schemas.android.com/apk/res-auto";
    private RelativeLayout mRoot;

    public SettingView(Context context) {
        super(context);
        initView();
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        String title = attrs.getAttributeValue(NAMESPACE,"settingTitle");
        String desc = attrs.getAttributeValue(NAMESPACE,"settingDesc");
        String hint = attrs.getAttributeValue(NAMESPACE,"setHint");
        boolean showSwitch = attrs.getAttributeBooleanValue(NAMESPACE,"showSettingSwitch",false);
        boolean showArrow = attrs.getAttributeBooleanValue(NAMESPACE,"showSettingArrow",false);
        mDesc.setVisibility(TextUtils.isEmpty(desc)?GONE:VISIBLE);
        mHint.setVisibility(TextUtils.isEmpty(hint)?GONE:VISIBLE);
        mSwitch.setVisibility(showSwitch?VISIBLE:GONE);
        mTitle.setText(title);
        mDesc.setText(desc);
        mHint.setText(hint);
    }


    private void initView() {
        inflate(getContext(), R.layout.view_setting,this);
        mRoot = (RelativeLayout) findViewById(R.id.setting_root);
        mTitle = (TextView) findViewById(R.id.setting_title);
        mDesc = (TextView) findViewById(R.id.setting_desc);
        mHint = (TextView) findViewById(R.id.setting_show);
        mSwitch = (SwitchButton) findViewById(R.id.setting_switch);
        initListener();
    }

    private void initListener() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mListener!=null) mListener.onChecked(getId(),buttonView,isChecked);
            }
        });
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setDesc(String desc){
        mDesc.setText(desc);
        mDesc.setVisibility(VISIBLE);
    }

    public void setHint(String hint){
        mHint.setText(hint);
        mHint.setVisibility(VISIBLE);
        mSwitch.setVisibility(GONE);
    }

    public void setCheckedImmediately(boolean checked){
        mSwitch.setCheckedImmediately(checked);
        mSwitch.setVisibility(VISIBLE);
        mHint.setVisibility(GONE);
    }

    public boolean isChecked(){
        if(mSwitch.getVisibility()!=VISIBLE) return false;
        return mSwitch.isChecked();
    }
    OnCheckedChangeListener mListener;
    public interface OnCheckedChangeListener{
        void onChecked(int id,CompoundButton buttonView, boolean isChecked);
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        this.mListener = onCheckedChangeListener;
    }
}
