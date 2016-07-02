package com.joe.zatuji.module.gallerypage;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joe.zatuji.Constant;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredView;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.module.picdetailpage.PicDetailActivity;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.GalleryMenuDialog;
import com.joe.zatuji.view.MessageDialog;

import java.util.List;

import cc.solart.turbo.OnItemClickListener;
import cc.solart.turbo.OnItemLongClickListener;

/**
 * 收藏详情页
 * Created by joe on 16/6/12.
 */
public class GalleryFragment extends BaseStaggeredFragment<GalleryPresenter> implements GalleryView{
    private FavoriteTag mTag;
    private GalleryAdapter mGalleryAdapter;
    @Override
    protected void initView() {
        super.initView();
        mGalleryAdapter = new GalleryAdapter(mActivity);
        mRecyclerView.setAdapter(mGalleryAdapter);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mPresenter.loadData(mTag);
    }

    public void setTag(FavoriteTag tag){
        this.mTag =tag;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mGalleryAdapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                Intent i = new Intent(mActivity, PicDetailActivity.class);
                DataBean.PicBean picBean = new DataBean.PicBean();
                picBean.file = new DataBean.PicBean.FileBean();
                picBean.raw_text = mGalleryAdapter.getItem(position).desc;
                picBean.file.height =  mGalleryAdapter.getItem(position).height;
                picBean.file.width =  mGalleryAdapter.getItem(position).width;
                picBean.file.type = mGalleryAdapter.getItem(position).type;
                LogUtils.d("key:"+mGalleryAdapter.getItem(position).img_url.substring(Api.HOST_PIC.length()));
                picBean.file.key = mGalleryAdapter.getItem(position).img_url.substring(Api.HOST_PIC.length());
                i.putExtra(Constant.PIC_DATA, picBean);
                i.putExtra(Constant.PIC_POS,position);
                i.putExtra(Constant.PIC_FROM_GALLERY,true);
                i.putExtra(Constant.PIC_LIST,mGalleryAdapter.getAllItem());
                mActivity.startActivity(i);
                mPresenter.subcribeForPicDetail();
            }
        });
        mGalleryAdapter.addOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh, final int position) {
                GalleryMenuDialog dialog = new GalleryMenuDialog(mActivity,mGalleryAdapter.getItem(position));
                dialog.setOnMenuClickListener(new GalleryMenuDialog.OnMenuClickListener() {
                    @Override
                    public void onFront(MyFavorite img) {
                        showConfirmDialog(img,CONFIRM_FRONT);
                    }

                    @Override
                    public void onDelete(MyFavorite img) {
                        mRemovePos = position;
                        showConfirmDialog(img,CONFIRM_DELETE);
                    }
                });
                dialog.show();
            }
        });
    }
    private final int CONFIRM_FRONT = 0;
    private final int CONFIRM_DELETE = 1;
    private int mRemovePos = -1;//当前被删除的条目
    private void showConfirmDialog(final MyFavorite img, final int type) {
        final MessageDialog dialog = new MessageDialog(mActivity);
        switch (type){
            case CONFIRM_FRONT:
                dialog.setTitleAndContent("设为封面","确定将该图片设为封面吗？");
                break;
            case CONFIRM_DELETE:
                dialog.setTitleAndContent("取消收藏","您确定要将该图片移除图集么？");
                break;
        }
        dialog.setonConfirmListener(new MessageDialog.onConfirmListener() {
            @Override
            public void onConfirm() {
                switch (type){
                    case CONFIRM_FRONT:
                        showLoading("设置封面");
                        mPresenter.setAsFront(mTag,img.img_url);
                        break;
                    case CONFIRM_DELETE:
                        showLoading("移除图片");
                        mPresenter.removeImg(mTag,img);
                        break;
                }
            }
        });
        dialog.show();
    }


    @Override
    public void loadData(List<? extends BaseBean> beanList) {
        LogUtils.d("show data");
        doneLoading();
        mRefreshLayout.setRefreshing(false);
        mGalleryAdapter.resetData((List<MyFavorite>) beanList);
    }

    @Override
    public void refreshData(List<? extends BaseBean> beanList) {
        mRefreshLayout.setRefreshing(false);
        mGalleryAdapter.resetData((List<MyFavorite>) beanList);
    }

    @Override
    public void addData(List<? extends BaseBean> addList) {
        int before= mGalleryAdapter.getAllItem().size();
        mRecyclerView.loadMoreComplete(addList);
        if(before== mGalleryAdapter.getAllItem().size()){
            mGalleryAdapter.addData((List<MyFavorite>) addList);
        }
    }

    @Override
    public void showToastMsg(String msg) {
        doneLoading();
        KToast.show(msg);
    }

    @Override
    public void showEmptyView() {
        mGalleryAdapter.resetData(null);
        mGalleryAdapter.setEmptyView(mActivity.getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(),false));
        mLoadingDialog.dismiss();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void removeItem(boolean isRemove) {
        if(!isRemove) mRemovePos = -1;
        if(mRemovePos!=-1){
            mGalleryAdapter.remove(mRemovePos);
            mRemovePos = -1;
        }
    }
}
