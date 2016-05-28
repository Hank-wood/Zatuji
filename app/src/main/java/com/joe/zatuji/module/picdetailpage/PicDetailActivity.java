package com.joe.zatuji.module.picdetailpage;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.module.favoritepage.model.FavoriteTag;
import com.joe.zatuji.module.favoritepage.presenter.FavoritePresenter;
import com.joe.zatuji.module.favoritepage.ui.CreateTagDialog;
import com.joe.zatuji.module.favoritepage.view.TagView;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.picdetailpage.presenter.PicDetailPresenter;
import com.joe.zatuji.module.picdetailpage.ui.ChooseTagDialog;

import java.util.ArrayList;

/**
 * 大图详情页吗面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity implements TagView{
    private ImageView ivPic;
    private TextView tvDesc;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ActionBar mActionBar;
    private boolean dontShowTv=false;
    private PicDetailPresenter mPresenter;
    private String desc;
    private int width;
    private int height;
    private FavoritePresenter mFavoritePresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PicDetailPresenter(mApplication,this);
        mFavoritePresenter = new FavoritePresenter(this,this,mActivity);
    }

    @Override
    protected void initView() {
        tvDesc = (TextView) findViewById(R.id.tv_desc_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
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
        DataBean.PicBean img = (DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA);
        desc = img.raw_text;
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            dontShowTv=true;
        }
        ImageHelper.showBig(ivPic,img);
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
            case R.id.action_save://收藏
                mFavoritePresenter.getFavoriteTag();
                break;
            case R.id.action_download://保存
//                mPresenter.saveToPhone(img);
                break;
            case R.id.action_share://分享
                break;
        }
        return true;
    }

    @Override
    protected void initListener() {
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appBarLayout.getVisibility()==View.VISIBLE){
                    tvDesc.setVisibility(View.INVISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                    appBarLayout.setVisibility(View.INVISIBLE);
                }else{
                    toolbar.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    if(!dontShowTv) tvDesc.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //让用户选择图集
    @Override
    public void showTag(ArrayList<FavoriteTag> tags) {
        if(tags.size()==0){
            CreateTagDialog dialog = new CreateTagDialog(this);
            dialog.setOnCreateCallBack(new CreateTagDialog.OnCreateCallBack() {
                @Override
                public void OnCreate(FavoriteTag tag) {
                    mFavoritePresenter.createTag(tag);
                }
            });
            dialog.show();
        }else{
            //从列表中选
            ChooseTagDialog dialog = new ChooseTagDialog(mActivity,tags);
            dialog.setCreateTag(new ChooseTagDialog.CreateTag() {
                @Override
                public void onCreateTag(ChooseTagDialog dialog) {
                    dialog.dismiss();
                    showTag(new ArrayList<FavoriteTag>());
                }
            });
            dialog.setOnChooseTag(new ChooseTagDialog.OnChooseTag() {
                @Override
                public void onChooseTag(FavoriteTag tag, ChooseTagDialog dialog) {
                    dialog.dismiss();
//                    mPresenter.saveToFavorite(img,desc,width,height,tag);
                }
            });
            dialog.show();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        KToast.show(msg);
    }

    @Override
    public void showNotSign() {
        KToast.show("请先登录账号");
    }

    @Override
    public void addTag(ArrayList<FavoriteTag> tags) {
//        mPresenter.saveToFavorite(img,desc,width,height,tags.get(0));
    }

    private void countPicSize(ImageView mIv) {
        width = getIntent().getIntExtra(Constant.PIC_WIDTH,-1);
        height = getIntent().getIntExtra(Constant.PIC_HEIGHT,-1);
        //获取屏幕宽高
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        LogUtils.d("before:W="+ width +" H="+ height +" Screen:W="+w_screen+"H="+h_screen);
        AbsListView.LayoutParams params= (AbsListView.LayoutParams) mIv.getLayoutParams();
        double times= (w_screen+0.0)/(width +0.0);
        //宽与屏幕对齐
        params.width=w_screen;
        LogUtils.d("times="+times);
        params.height= (int) (height *times);
        LogUtils.d("after:W="+ width +" H="+params.height);
        mIv.setLayoutParams(params);
        mIv.setScaleType(ImageView.ScaleType.FIT_START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter=null;
    }
}
