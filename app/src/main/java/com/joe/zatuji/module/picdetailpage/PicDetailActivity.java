package com.joe.zatuji.module.picdetailpage;

import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.BaseActivity;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.Constant;
import com.joe.zatuji.helper.ShareHelper;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.ChooseTagDialog;
import com.joe.zatuji.view.CreateTagDialog;

import java.io.File;
import java.util.ArrayList;


/**
 * 大图详情页吗面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity<PicDetailPresenter> implements PicDetailView{
    private SubsamplingScaleImageView ivPic;
    private TextView tvDesc;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ActionBar mActionBar;
    private boolean dontShowTv=false;
    private String desc;
    private DataBean.PicBean img;
    private MyFavorite mMyFavoriteImg;

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
        img = (DataBean.PicBean) getIntent().getSerializableExtra(Constant.PIC_DATA);
        tvDesc = (TextView) findViewById(R.id.tv_desc_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivPic = (SubsamplingScaleImageView) findViewById(R.id.iv_pic);
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
        desc = img.raw_text;
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            dontShowTv=true;
        }
//        ImageHelper.showBig(ivPic, img);
        ImageHelper.showScaleBig(ivPic,img);
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
                finish();
                break;
            case R.id.action_save://收藏
                if(!MyApplication.isLogin()){
                    showToastMsg("请先登录帐号～");
                }else{
                    showLoading("获取图集...");
                    mPresenter.showTags();
                }
                break;
            case R.id.action_download://保存
                showLoading("保存图片...");
                mPresenter.saveToPhone(img.file.key,mMyFavoriteImg.type);
                break;
            case R.id.action_share://分享
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
