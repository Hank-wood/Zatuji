package com.joe.zatuji.module.aboutpage;

import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.utils.KToast;

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
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("关于杂图集");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        LinearLayout mContainer = (LinearLayout) findViewById(R.id.ll_container_about);
        View about = new AboutPage(this)
                .isRTL(false)
                .setDescription(getDescribe())
                .setImage(R.drawable.front_default)
                .addItem(getVersion())
                .addEmail("qiaoxiaoxi621@gmail.com")
                .addItem(getCopyRight())
                .create();
        mContainer.addView(about);
    }

    @Override
    protected void initPresenter() {

    }

    private String getDescribe(){
        return "杂图集是一款专注于优质图片的浏览及收藏的应用，图片资源均来自于网络。纷扰生活中"
                +"放慢脚步，欣赏这个世界上你所不知道的美。\n本应用由个人开发者独立开发完成，旨在交流学习。"
                +"如果您对本应用有好的意见或建议，可以通过意见反馈或者直接联系我。"
                +"同时感谢项目中所用到的所有开源项目。";
    }
    private int count = 0;
    private Element getVersion(){
        Element versionElement = new Element();
        versionElement.setTitle("当前版本号:"+MyApplication.getInstance().getVersionName());
        //versionElement.setGravity(Gravity.CENTER);
        versionElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count==20){
                    SettingHelper.setDebug(true);
                    KToast.show("开发者模式已开启，可在设置中关闭");
                }
            }
        });
        return versionElement;
    }

    private Element getCopyRight(){
        Element copyRight = new Element();
        copyRight.setGravity(Gravity.CENTER);
        copyRight.setTitle("Copyright©  2016  Joey");
        return copyRight;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}
