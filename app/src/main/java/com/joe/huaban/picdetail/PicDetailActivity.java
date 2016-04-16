package com.joe.huaban.picdetail;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseActivity;
import com.joe.huaban.global.Constant;
import com.joe.huaban.global.utils.LogUtils;

import org.xutils.x;

/**
 * 大图详情页吗面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity{
    private ImageView ivPic;
    private TextView tvDesc;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ActionBar mActionBar;

    @Override
    protected int getContent() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initView() {
        ivPic = (ImageView) findViewById(R.id.iv_pic_item);
        tvDesc = (TextView) findViewById(R.id.tv_desc_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        tvDesc.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        appBarLayout.setVisibility(View.INVISIBLE);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initData();
    }
    private void initData() {
        String img=getIntent().getStringExtra(Constant.PIC_DATA);
        String desc=getIntent().getStringExtra(Constant.PIC_DESC);
        countPicSize();
        x.image().bind(ivPic,Constant.HOST_PIC+img);
        if(!TextUtils.isEmpty(desc)) tvDesc.setText(desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initListener() {
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDesc.getVisibility()==View.VISIBLE){
                    tvDesc.setVisibility(View.INVISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                    appBarLayout.setVisibility(View.INVISIBLE);
                }else{
                    tvDesc.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void countPicSize() {
        int width=getIntent().getIntExtra(Constant.PIC_WIDTH,-1);
        int height=getIntent().getIntExtra(Constant.PIC_HEIGHT,-1);
        //获取屏幕宽高
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        LogUtils.d("before:W="+width+" H="+height+" Screen:W="+w_screen+"H="+h_screen);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivPic.getLayoutParams();
        double times= (w_screen+0.0)/(width+0.0);
        //宽与屏幕对齐
        width=w_screen;
        params.width=width;
        LogUtils.d("times="+times);
        params.height= (int) (height*times);
        LogUtils.d("after:W="+width+" H="+params.height);
        ivPic.setLayoutParams(params);
        ivPic.setScaleType(ImageView.ScaleType.FIT_START);
    }
}
