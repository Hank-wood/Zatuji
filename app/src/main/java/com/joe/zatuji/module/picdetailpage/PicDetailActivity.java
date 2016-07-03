package com.joe.zatuji.module.picdetailpage;

import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.joe.zatuji.Event;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.Constant;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.ChooseTagDialog;
import com.joe.zatuji.view.CreateTagDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


/**
 * 大图详情页吗面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity<PicDetailPresenter> implements PicDetailView{
    private TextView tvDesc;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ActionBar mActionBar;
    private boolean dontShowTv=false;
    private String desc;
    private DataBean.PicBean img;
    private MyFavorite mMyFavoriteImg;
    private ViewPager mViewPager;
    private ArrayList<DataBean.PicBean> mPicList;
    private ArrayList<MyFavorite> mGallerys;
    private boolean isFromGallery;
    private PicDetailAdapter mAdapter;
    private int mCurrentPos = 0;
    @Override
    protected int getLayout() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_detail);
        mAdapter = new PicDetailAdapter(mActivity);
        mViewPager.setAdapter(mAdapter);
        getDataFromWhere();
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

    private void getDataFromWhere() {
        img = (DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA);
        isFromGallery = getIntent().getBooleanExtra(Constant.PIC_FROM_GALLERY,false);
        if(isFromGallery){
            mGallerys = (ArrayList<MyFavorite>) getIntent().getSerializableExtra(Constant.PIC_LIST);
            mAdapter.setMyFavorites(mGallerys);
        }else{
            mPicList = (ArrayList<DataBean.PicBean>) getIntent().getSerializableExtra(Constant.PIC_LIST);
            mAdapter.setPics(mPicList);
        }
        mCurrentPos = getIntent().getIntExtra(Constant.PIC_POS,0);
        mViewPager.setCurrentItem(getIntent().getIntExtra(Constant.PIC_POS,0));

    }

    private void initData() {
        setData((DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA));
    }

    private void setData(DataBean.PicBean picBean){
        img = picBean;
        desc = img.raw_text;
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            dontShowTv=true;
        }
//        showBigImage(img.file.type);
        mMyFavoriteImg = new MyFavorite();
        mMyFavoriteImg.desc = desc;
        mMyFavoriteImg.img_url = Api.HOST_PIC+img.file.key;
        mMyFavoriteImg.width = img.file.width;
        mMyFavoriteImg.height = img.file.height;
        mMyFavoriteImg.type = img.file.type;
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
                mPresenter.quiet(mCurrentPos);
                finish();
                break;
            case R.id.action_save://收藏
                MobclickAgent.onEvent(mActivity, Event.EVENT_FAOVRITE);
                if(!MyApplication.isLogin()){
                    showToastMsg("请先登录帐号～");
                }else{
                    showLoading("获取图集...");
                    mPresenter.showTags();
                }
                break;
            case R.id.action_download://保存
                MobclickAgent.onEvent(mActivity, Event.EVENT_DOWNLOAD);
                showLoading("保存图片...");
                mPresenter.saveToPhone(img.file.key,mMyFavoriteImg.type);
                break;
            case R.id.action_share://分享
                MobclickAgent.onEvent(mActivity, Event.EVENT_SHARE);
                LogUtils.d("url:"+mMyFavoriteImg.img_url);
                showLoading("正在分享...");
                mPresenter.share(img.file.key,mMyFavoriteImg.type);
//                ShareHelper.share("分享自杂图集", Uri.parse(mMyFavoriteImg.img_url),mActivity);
//                showToastMsg("开发者还未添加该功能～");
                break;
        }
        return true;
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new PicDetailAdapter.onItemClickListener() {
            @Override
            public void OnItemClicked(int position, DataBean.PicBean picBean) {
                hideOrShowAppBar();
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowAppBar();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == mAdapter.getCount()-1){
                    LogUtils.d("load more");
                    mPresenter.getMoreData();
                }else {
                    setData(mAdapter.getItem(position));
                    mCurrentPos = position;
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void hideOrShowAppBar() {
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

    //让用户选择图集
    @Override
    public void showTag(ArrayList<FavoriteTag> tags) {
        doneLoading();
        if(tags.size()==0){
            CreateTagDialog dialog = new CreateTagDialog(this);
            dialog.setOnCreateCallBack(new CreateTagDialog.OnCreateCallBack() {
                @Override
                public void OnCreate(FavoriteTag tag) {
                    mPresenter.createTags(tag,mMyFavoriteImg);
                    showLoading("收集到:"+tag.tag);
                }

                @Override
                public void onUpdate(FavoriteTag tag, String objectId) {}
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
                    mPresenter.chooseTagToSave(tag,mMyFavoriteImg);
                    dialog.dismiss();
                    showLoading("收集到:"+tag.tag);
                }
            });
            dialog.show();
        }
    }

    @Override
    public void addGallery(ArrayList<MyFavorite> data) {
        mAdapter.addMyFavorites(data);
        doneLoading();
    }

    @Override
    public void addPics(ArrayList<DataBean.PicBean> data) {
        mAdapter.addPics(data);
        doneLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.quiet(mCurrentPos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter=null;
    }

    @Override
    public void showToastMsg(String msg) {
        KToast.show(msg);
        doneLoading();
    }
}
