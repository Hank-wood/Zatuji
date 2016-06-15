package com.joe.zatuji.module.gallerypage;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joe.zatuji.Constant;
import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.module.picdetailpage.PicDetailActivity;
import com.joe.zatuji.utils.LogUtils;

import java.util.List;

import cc.solart.turbo.OnItemClickListener;

/**
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
                picBean.file.type = "jpeg";
                LogUtils.d("key:"+mGalleryAdapter.getItem(position).img_url.substring(Api.HOST_PIC.length()));
                picBean.file.key = mGalleryAdapter.getItem(position).img_url.substring(Api.HOST_PIC.length());
                i.putExtra(Constant.PIC_DATA, picBean);
                mActivity.startActivity(i);
            }

        });
    }


    @Override
    public void loadData(List<? extends BaseBean> beanList) {
        LogUtils.d("show data");
        doneLoading();
        mRefreshLayout.setRefreshing(false);
        mGalleryAdapter.setNewData((List<MyFavorite>) beanList);
    }

    @Override
    public void refreshData(List<? extends BaseBean> beanList) {
        mRefreshLayout.setRefreshing(false);
        mGalleryAdapter.setNewData((List<MyFavorite>) beanList);
    }

    @Override
    public void addData(List<? extends BaseBean> addList) {

        mRecyclerView.loadMoreComplete(addList);
    }

    @Override
    public void showEmptyView() {
        mGalleryAdapter.setNewData(null);
        mGalleryAdapter.setEmptyView(mActivity.getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(),false));
        mLoadingDialog.dismiss();
        mRefreshLayout.setRefreshing(false);
    }
}
