package com.joe.zatuji.module.picdetailpage;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
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
import com.joe.zatuji.helper.FabAnimatorHelper;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.ChooseTagDialog;
import com.joe.zatuji.view.CreateTagDialog;
import com.joe.zatuji.view.ScaleViewPager;
import com.joe.zatuji.view.transformer.SelectTransformer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


/**
 * 大图详情页面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity<PicDetailPresenter> implements PicDetailView, View.OnClickListener {
    private TextView tvDesc;
    private String desc;
    private DataBean.PicBean img;
    private MyFavorite mMyFavoriteImg;
    private ScaleViewPager mViewPager;
    private ArrayList<DataBean.PicBean> mPicList;
    private ArrayList<MyFavorite> mGallerys;
    private boolean isFromGallery;
    private PicDetailAdapter mAdapter;
    private int mCurrentPos = 0;
    private FloatingActionButton mFabBack;
    private FloatingActionButton mFabDownload;
    private FloatingActionButton mFabShare;
    private FloatingActionButton mFabFavorite;
    private ProgressBar mLoadingBar;

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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = (ScaleViewPager) findViewById(R.id.viewpager_detail);
        mLoadingBar = findView(R.id.loading_detail);
        mAdapter = new PicDetailAdapter(mActivity);
        mViewPager.setPageTransformer(true,new SelectTransformer());
        mViewPager.setAdapter(mAdapter);
        getDataFromWhere();
        tvDesc = (TextView) findViewById(R.id.tv_desc_item);
        tvDesc.setVisibility(View.INVISIBLE);
        initFab();
        initData();
    }

    private void initFab() {
        mFabBack = findView(R.id.fab_back);
        mFabDownload = findView(R.id.fab_download);
        mFabShare = findView(R.id.fab_share);
        mFabFavorite = findView(R.id.fab_favorite);
        mFabBack.setOnClickListener(this);
        mFabDownload.setOnClickListener(this);
        mFabShare.setOnClickListener(this);
        mFabFavorite.setOnClickListener(this);
    }

    private void getDataFromWhere() {
        img = (DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA);
        isFromGallery = getIntent().getBooleanExtra(Constant.PIC_FROM_GALLERY, false);
        if (isFromGallery) {
            mGallerys = (ArrayList<MyFavorite>) getIntent().getSerializableExtra(Constant.PIC_LIST);
            mAdapter.setMyFavorites(mGallerys);
        } else {
            mPicList = (ArrayList<DataBean.PicBean>) getIntent().getSerializableExtra(Constant.PIC_LIST);
            mAdapter.setPics(mPicList);
        }
        mCurrentPos = getIntent().getIntExtra(Constant.PIC_POS, 0);
        mViewPager.setCurrentItem(getIntent().getIntExtra(Constant.PIC_POS, 0));

    }

    private void initData() {
        setData((DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA));
    }

    private void setData(DataBean.PicBean picBean) {
        img = picBean;
        desc = img.raw_text;
        if (!TextUtils.isEmpty(desc)) {
            tvDesc.setText(desc);
        } else {
            tvDesc.setText("");
        }
//        showBigImage(img.file.type);
        mMyFavoriteImg = new MyFavorite();
        mMyFavoriteImg.desc = desc;
        mMyFavoriteImg.img_url = Api.HOST_PIC + img.file.key;
        mMyFavoriteImg.width = img.file.width;
        mMyFavoriteImg.height = img.file.height;
        mMyFavoriteImg.type = img.file.type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_back:
                mPresenter.quiet(mCurrentPos);
                finish();
                break;
            case R.id.fab_favorite://收藏
                MobclickAgent.onEvent(mActivity, Event.EVENT_FAVORITE);
                if (!MyApplication.isLogin()) {
                    showToastMsg("请先登录帐号～");
                } else {
                    showLoading("获取图集...");
                    mPresenter.showTags();
                }
                break;
            case R.id.fab_download://保存
                MobclickAgent.onEvent(mActivity, Event.EVENT_DOWNLOAD);
                showLoading("保存图片...");
                mPresenter.saveToPhone(img.file.key, mMyFavoriteImg.type);
                break;
            case R.id.fab_share://分享
                MobclickAgent.onEvent(mActivity, Event.EVENT_SHARE);
                LogUtils.d("url:" + mMyFavoriteImg.img_url);
                showLoading("正在分享...");
                mPresenter.share(img.file.key, mMyFavoriteImg.type);
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new PicDetailAdapter.onItemClickListener() {
            @Override
            public void OnItemClicked(int position, DataBean.PicBean picBean) {
                hideOrShowMenu();
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowMenu();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mAdapter.getCount() - 1) {
                    mPresenter.getMoreData();
                }
                setData(mAdapter.getItem(position));
                mCurrentPos = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private boolean isMenuHide = true;
    private void hideOrShowMenu() {
        if(isMenuHide){
            FabAnimatorHelper.inst().startFabGroupAnimation(false,mFabFavorite,mFabShare,mFabDownload,mFabBack);
            isMenuHide = false;
        }else{
            FabAnimatorHelper.inst().startFabGroupAnimation(true,mFabFavorite,mFabShare,mFabDownload,mFabBack);
            isMenuHide = true;
        }
    }

    //让用户选择图集
    @Override
    public void showTag(ArrayList<FavoriteTag> tags) {
        doneLoading();
        if (tags.size() == 0) {
            CreateTagDialog dialog = new CreateTagDialog(this);
            dialog.setOnCreateCallBack(new CreateTagDialog.OnCreateCallBack() {
                @Override
                public void OnCreate(FavoriteTag tag) {
                    mPresenter.createTags(tag, mMyFavoriteImg);
                    showLoading("收集到:" + tag.tag);
                }

                @Override
                public void onUpdate(FavoriteTag tag, String objectId) {
                }
            });
            dialog.show();
        } else {
            //从列表中选
            ChooseTagDialog dialog = new ChooseTagDialog(mActivity, tags);
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
                    mPresenter.chooseTagToSave(tag, mMyFavoriteImg);
                    dialog.dismiss();
                    showLoading("收集到:" + tag.tag);
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
        mPresenter = null;
    }

    @Override
    public void showToastMsg(String msg) {
        KToast.show(msg);
        doneLoading();
    }
}
