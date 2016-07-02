package com.joe.zatuji.module.discoverpage;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.base.view.HideFabView;
import com.joe.zatuji.base.ui.basestaggered.BaseStaggeredFragment;
import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.helper.SettingHelper;
import com.joe.zatuji.module.homepage.HomeFragment;

/**
 * Created by Joe on 2016/4/18.
 */
public class DiscoverFragment extends BaseStaggeredFragment<DiscoverPresenter>{
    private TagBean.Tag mTag = MyApplication.getInstance().mDefaultTag;
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
        mPresenter.setTag(mTag.requestName);
        mPresenter.loadData();
    }
    public void loadAnotherTagData(TagBean.Tag tag){
        showLoading(getResources().getString(R.string.load_new_discover));
        mRecyclerView.scrollToPosition(0);
        mTag = tag;
        mPresenter.setTag(mTag.requestName);
        mPresenter.loadData();
    }

    public String getTagName(){
        return mTag.name;
    }
}
