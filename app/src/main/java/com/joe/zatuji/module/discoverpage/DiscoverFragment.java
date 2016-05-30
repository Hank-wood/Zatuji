package com.joe.zatuji.module.discoverpage;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.base.view.HideFabView;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;
import com.joe.zatuji.R;
import com.joe.zatuji.module.homepage.HomeFragment;

/**
 * Created by Joe on 2016/4/18.
 */
public class DiscoverFragment extends BaseStaggeredFragment<DiscoverPresenter>{
    private String mTag = MyApplication.getInstance().mDefaultTag.requestName;
    private static DiscoverFragment mInstance;
    public static synchronized DiscoverFragment getInstance(){
        if(mInstance==null){
            mInstance = new DiscoverFragment();
        }
        return mInstance;
    }
    @Override
    protected void initView() {
        super.initView();
        setHomeFabHideEnable((HideFabView) mActivity,true);

    }

    @Override
    protected void initPresenter() {
        mPresenter.setView(this);
        showLoading(getResources().getString(R.string.load_new_discover));
        mPresenter.setTag(mTag);
        mPresenter.loadData();
    }
    public void loadAnotherTagData(String tag){
        showLoading(getResources().getString(R.string.load_new_discover));
        mRecyclerView.scrollToPosition(0);
        mTag = tag;
        mPresenter.setTag(mTag);
        mPresenter.loadData();
    }

    //    private SwipeRefreshLayout mRefreshLayout;
//
//    private RecyclerView mRecyclerView;
//    private HomeAdapter mAdapter;
//    private StaggeredGridLayoutManager mLayoutManager;
//    private DiscoverPresenter mPresenter;
//    private HomeActivity loadingView;
//
//    @Override
//    protected int getLayout() {
//        return R.layout.fragment_stagerd_base;
//    }
//
//    @Override
//    protected void initPresenter() {
//        loadingView = (HomeActivity) mActivity;
//        mPresenter = new DiscoverPresenter(this, loadingView,myApplication);
//        mPresenter.getInitData();
//    }
//
//    @Override
//    protected void initView() {
//        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_fragment_base);
//        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_fragment_base);
//        mAdapter = new HomeAdapter(mActivity);
//        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//    }
//    @Override
//    protected void initListener() {
//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.getInitData();
//            }
//        });
//
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int[] visibleItems = mLayoutManager.findLastVisibleItemPositions(null);
//                int lastitem = Math.max(visibleItems[0], visibleItems[1]);
//                if(dy>0) {
//                    loadingView.hideOrShowFAB(true);
//                }else{
//                    loadingView.hideOrShowFAB(false);
//                }
//                if (dy > 0 && lastitem > mAdapter.getItemCount() - 5) {
//                    mPresenter.loadMore();
//                }
//            }
//        });
//    }
//    @Override
//    public void refreshData(PicData data) {
//        mRefreshLayout.setRefreshing(false);
//        mAdapter.refreshData(data,false);
//    }
//
//    @Override
//    public void loadMore(PicData data) {
//        mAdapter.refreshData(data,true);
//    }
//
//    @Override
//    public void stopRefresh() {
//        mRefreshLayout.setRefreshing(false);
//    }
//

}
