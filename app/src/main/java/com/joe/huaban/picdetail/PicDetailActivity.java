package com.joe.huaban.picdetail;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joe.huaban.R;
import com.joe.huaban.base.ui.BaseActivity;
import com.joe.huaban.global.Constant;
import com.joe.huaban.global.utils.LogUtils;
import com.joe.huaban.picdetail.presenter.PicDetailPresenter;

import org.xutils.x;

/**
 * 大图详情页吗面
 * Created by Joe on 2016/4/16.
 */
public class PicDetailActivity extends BaseActivity{
    private ImageView ivPic;
    private TextView tvDesc;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ActionBar mActionBar;
    private boolean dontShowTv=false;
    private PicDetailPresenter mPresenter;
    private String img;
    private String desc;
    private int width;
    private int height;
    private ListView mList;

    @Override
    protected int getContent() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PicDetailPresenter(mApplication,this);
    }

    @Override
    protected void initView() {
        tvDesc = (TextView) findViewById(R.id.tv_desc_item);
        mList = (ListView) findViewById(R.id.list_pic_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        img = getIntent().getStringExtra(Constant.PIC_DATA);
        desc = getIntent().getStringExtra(Constant.PIC_DESC);
        if(!TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }else{
            dontShowTv=true;
        }
        PicDetailAdapter mAdapter=new PicDetailAdapter();
        mList.setAdapter(mAdapter);
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
                mPresenter.saveToFavorite(img,desc,width,height);
                break;
            case R.id.action_download://保存
                mPresenter.saveToPhone(img);
                break;
            case R.id.action_share://分享
                break;
        }
        return true;
    }

    @Override
    protected void initListener() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    class PicDetailAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=new ImageView(mApplication);
                holder.mIv= (ImageView) convertView;
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            AbsListView.LayoutParams params= new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.mIv.setLayoutParams(params);
            holder.mIv.setScaleType(ImageView.ScaleType.FIT_XY);
            countPicSize(holder.mIv);
            x.image().bind(holder.mIv,Constant.HOST_PIC+ img);
            return convertView;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 1;
        }
    }
    class ViewHolder{
        ImageView mIv;
    }
    private void countPicSize(ImageView mIv) {
        width = getIntent().getIntExtra(Constant.PIC_WIDTH,-1);
        height = getIntent().getIntExtra(Constant.PIC_HEIGHT,-1);
        //获取屏幕宽高
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        LogUtils.d("before:W="+ width +" H="+ height +" Screen:W="+w_screen+"H="+h_screen);
        AbsListView.LayoutParams params= (AbsListView.LayoutParams) mIv.getLayoutParams();
        double times= (w_screen+0.0)/(width +0.0);
        //宽与屏幕对齐
        params.width=w_screen;
        LogUtils.d("times="+times);
        params.height= (int) (height *times);
        LogUtils.d("after:W="+ width +" H="+params.height);
        mIv.setLayoutParams(params);
        mIv.setScaleType(ImageView.ScaleType.FIT_START);
    }


}
