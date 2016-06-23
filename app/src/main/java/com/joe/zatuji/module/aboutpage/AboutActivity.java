package com.joe.zatuji.module.aboutpage;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by joe on 16/6/23.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        LinearLayout mContainer = (LinearLayout) findViewById(R.id.ll_container_about);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.2.0");
        versionElement.setGravity(Gravity.CENTER);
        Element copyRight = new Element();
        copyRight.setTitle("copy right");
        View about = new AboutPage(this)
                .isRTL(false)
                .setDescription("la la la la")
                .setImage(R.drawable.front_default)
                .addItem(versionElement)
                .addEmail("qiaoxiaoxi621@gmail.com")
                .addItem(copyRight)
                .addWebsite("www.baidu.com")
                .addGroup("group")
                .create();
        mContainer.addView(about);
    }

    @Override
    protected void initPresenter() {

    }
}
