package com.joe.huaban.pager;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.joe.huaban.R;
import com.joe.huaban.adapter.PicAdapter;
import com.joe.huaban.spider.Picture;
import com.joe.huaban.spider.Spider;

import java.util.List;

/**
 * Created by Joe on 2016/3/11.
 */
public abstract class BasePager {
    protected Activity mActivity;
    protected RecyclerView mRecyclerView;
    public View mRootView;
    protected String mUrl;
    protected PicAdapter mAdapter;
    protected StaggeredGridLayoutManager mLayoutManager;
    protected boolean isLoadingMore=false;
    protected int currentPage=1;
    public String mTitle;

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        initView();
        initUrl();
    }

    protected void initView() {
        mRootView = View.inflate(mActivity, R.layout.pager_base, null);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rcv_home);
    }

    protected abstract void initUrl() ;

    public void initData() {
        new Thread(){
            @Override
            public void run() {
                currentPage=1;
                Spider spider=new Spider();
                List<Picture> list=spider.getPictureFromDouBan(mUrl,currentPage);
                Message msg=Message.obtain();
                msg.obj=list;
                msg.what=1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //没有数据
                    Toast.makeText(mActivity, "妹纸加载完了！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //第一次加载数据
                    List<Picture> pics= (List<Picture>) msg.obj;
                    mAdapter = new PicAdapter(mActivity,pics);
                    initAdapter();
                    //pics.clear();
                    break;
                case 2:
                    //加载更多
                    List<Picture> pic= (List<Picture>) msg.obj;
                    mAdapter.addData(pic);
                    //pic.clear();
                    isLoadingMore=false;
                    break;
            }

        }
    };

    protected void initAdapter() {
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initListener();
    }

    protected void initListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] visibleItems = mLayoutManager.findLastVisibleItemPositions(null);
                int lastitem = Math.max(visibleItems[0], visibleItems[1]);

                if (dy > 0 && lastitem > mAdapter.getItemCount() - 5 && !isLoadingMore) {
                    isLoadingMore = true;
                    loadingMore();
                }
            }
        });
    }

    protected void loadingMore() {
        new Thread(){
            @Override
            public void run() {
                currentPage++;
                Spider spider=new Spider();
                List<Picture> list=spider.getPictureFromDouBan(mUrl,currentPage);
                Message msg=Message.obtain();
                if(list.size()<=0){
                    currentPage--;
                    handler.sendEmptyMessage(0);
                    return;
                }
                msg.obj=list;
                msg.what=2;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
