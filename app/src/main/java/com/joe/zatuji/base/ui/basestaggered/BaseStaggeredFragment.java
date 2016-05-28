package com.joe.zatuji.base.ui.basestaggered;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.zatuji.Constant;
import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.base.view.HideFabView;
import com.joe.zatuji.data.BaseBean;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.module.picdetailpage.PicDetailActivity;
import com.joe.zatuji.utils.KToast;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.utils.TUtil;
import com.joe.zatuji.view.LoadingDialog;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import cc.solart.turbo.OnItemClickListener;
import cc.solart.turbo.OnLoadMoreListener;
import cc.solart.turbo.TurboRecyclerView;

/**
 * 瀑布流布局的基类
 * Created by joe on 16/5/27.
 */
public abstract class BaseStaggeredFragment<T extends BaseStaggeredPresenter>  extends Fragment implements BaseStaggeredView {
    RecyclerView.LayoutManager mLayoutManager;
    protected SwipeRefreshLayout mRefreshLayout;//下拉刷新
    protected TurboRecyclerView mRecyclerView;
    protected BaseStaggeredAdapter mAdapter;
    protected Activity mActivity;
    protected View mRootView;
    protected AlertDialog dialog;
    protected MyApplication myApplication;
    protected T mPresenter;
    protected LoadingDialog mLoadingDialog;
    protected boolean isHideFab = false;
    protected HideFabView mHideFabView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity=getActivity();
        this.myApplication= (MyApplication) mActivity.getApplication();
        //initLeakCanary();
    }
    /**内存泄漏*/
    protected void initLeakCanary(){
        RefWatcher refWatcher = MyApplication.getRefWatcher(mActivity);
        refWatcher.watch(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayout(),null);
        initPM();
        initView();
        initPresenter();
        initListener();
        return mRootView;
    }

    protected int getLayout() {
        return R.layout.fragment_staggered_base;
    }

    protected void initPM() {
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) mPresenter.onStart();
    }

    protected void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findView(R.id.swipe_fragment_base);
        mRecyclerView = (TurboRecyclerView) findView(R.id.recycler_fragment_base);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new BaseStaggeredAdapter(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initPresenter() {
        mPresenter.setView(this);
        showLoading(getResources().getString(R.string.load_new_data));
        mPresenter.loadData();
    }

    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.reLoadData();
            }
        });

        mRecyclerView.addOnLoadingMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadingMore() {
                mPresenter.loadMoreData();
            }
        });

        mAdapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                Intent i = new Intent(mActivity, PicDetailActivity.class);
                DataBean.PicBean picBean = mAdapter.getItem(position);
                i.putExtra(Constant.PIC_DATA, picBean);
                mActivity.startActivity(i);
            }

        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0) {
//                    loadingView.hideOrShowFAB(true);
                    onScrollDown(recyclerView,dx,dy);
                }else{
                    onScrollUp(recyclerView,dx,dy);
                }
            }
        });
    }
    /**是否滑动时隐藏fab*/
    protected void setHomeFabHideEnable(HideFabView hideFabView, boolean isHideFavb){
        this.mHideFabView = hideFabView;
        this.isHideFab = isHideFavb;
    }
    protected void onScrollUp(RecyclerView recyclerView, int dx, int dy){
        if(isHideFab && mHideFabView!=null) mHideFabView.hideOrShowFAB(false);
    }

    protected void onScrollDown(RecyclerView recyclerView, int dx, int dy) {
        if(isHideFab && mHideFabView!=null) mHideFabView.hideOrShowFAB(true);
    }

    protected void toGrid() {
        mLayoutManager = new GridLayoutManager(mActivity, 2);
    }

    /************************/
    /**** 以下是操作UI部分 ****/
    /****                ****/
    @Override
    public void loadData(List<? extends BaseBean> beanList) {
        LogUtils.d("show data");
        doneLoading();
        mRefreshLayout.setRefreshing(false);
        mAdapter.setNewData((List<DataBean.PicBean>) beanList);
    }

    @Override
    public void refreshData(List<? extends BaseBean> beanList) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setNewData((List<DataBean.PicBean>) beanList);
    }

    @Override
    public void addData(List<? extends BaseBean> addList) {

        mRecyclerView.loadMoreComplete(addList);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setNewData(null);
        mAdapter.setEmptyView(mActivity.getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(),false));
        mLoadingDialog.dismiss();
        mRefreshLayout.setRefreshing(false);
    }

    /**禁用加载更多*/
    @Override
    public void disableLoadMore(boolean isDisable) {
        mRecyclerView.setLoadMoreEnabled(!isDisable);
    }

    @Override
    public void showToastMsg(String msg) {
        KToast.show(msg);
    }

    public void showLoading(String msg){
        if(mLoadingDialog==null) mLoadingDialog = new LoadingDialog(mActivity,msg);
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }
    public void doneLoading(){
        if(mLoadingDialog!=null&&mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
    }
    /****                ****/
    /********** END *********/


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null) mPresenter.onRemove();
    }


    protected View findView(int id){
        return mRootView.findViewById(id);
    }
}
