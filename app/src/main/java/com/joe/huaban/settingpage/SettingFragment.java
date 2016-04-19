package com.joe.huaban.settingpage;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.joe.huaban.HomeActivity;
import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseFragment;
import com.joe.huaban.global.utils.LogUtils;

import org.xutils.x;

import java.io.File;
import java.text.Format;

/**
 * Created by Joe on 2016/4/18.
 */
public class SettingFragment extends BaseFragment{

    private CardView mClearCache;
    private TextView tvCache;

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected void initView() {
        mClearCache = (CardView) mRootView.findViewById(R.id.card_clear_cache);
        tvCache = (TextView) mRootView.findViewById(R.id.tv_cache);
        getCache();
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
}
