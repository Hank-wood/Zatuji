package com.joe.zatuji.module.settingpage;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.zatuji.module.homepage.HomeActivity;
import com.joe.zatuji.R;
import com.joe.zatuji.base.ui.BaseFragment;
import com.joe.zatuji.module.settingpage.user.UserFragment;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.module.settingpage.presenter.SettingPresenter;
import com.joe.zatuji.module.settingpage.view.SettingView;

import org.xutils.x;

import java.io.File;

/**
 * Created by Joe on 2016/4/18.
 */
public class SettingFragment extends BaseFragment implements SettingView,View.OnClickListener{

    private RelativeLayout mClearCache;
    private TextView tvCache;
    private RelativeLayout mExit;
    private UserFragment userFragment;
    private SettingPresenter mPresenter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SettingPresenter(mActivity);
    }


    @Override
    protected void initView() {
        initUserInfo();
        mClearCache = (RelativeLayout) mRootView.findViewById(R.id.card_clear_cache);
        tvCache = (TextView) mRootView.findViewById(R.id.tv_cache);
        mRootView.findViewById(R.id.card_about).setOnClickListener(this);
        mRootView.findViewById(R.id.card_feedback).setOnClickListener(this);
        mRootView.findViewById(R.id.card_update).setOnClickListener(this);
        mExit = (RelativeLayout) mRootView.findViewById(R.id.card_exit);
        mExit.setOnClickListener(this);
        getCache();
    }

    /**
     * 加载用户信息
     */
    private void initUserInfo() {
        userFragment = new UserFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_container_user, userFragment).commit();
    }

    public void getCache() {
        File cacheDir=x.app().getCacheDir();
        File exCacheDir=x.app().getExternalCacheDir();
        LogUtils.d(""+cacheDir.length());
        LogUtils.d(""+Formatter.formatFileSize(mActivity,cacheDir.length()));
        long size=0;
        try {
            size=getFolderSize(cacheDir)+getFolderSize(exCacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvCache.setText(Formatter.formatFileSize(mActivity,size));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        final HomeActivity activity= (HomeActivity) mActivity;
        mClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showLoading();
                x.image().clearCacheFiles();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvCache.setText("0B");
                        activity.doneLoading();
                    }
                },2000);
            }
        });
    }
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_exit:
                userFragment.exit();
                break;
            case R.id.card_update:
                mPresenter.checkUpdate();
                break;
        }
    }


    @Override
    public void showExit() {
        mExit.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExit() {
        mExit.setVisibility(View.GONE);
    }

}
