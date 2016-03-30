package com.joe.huaban;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.joe.huaban.spider.Picture;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Joe on 2016/3/11.
 */
public class ImageDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;
    private List<Picture> mPics;
    private int mPosition;
    private ViewPager mViewPager;

    public ImageDialog(Context context, List<Picture> mPics, int position) {
        super(context);
        this.context = context;
        this.mPics = mPics;
        this.mPosition = position;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_content_viewpager);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_show_pic);
        setParams();
        initViewPager();
    }

    private void setParams() {
        WindowManager.LayoutParams lay = this.getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }


    private void initViewPager() {
        MyApdater apdater=new MyApdater();
        mViewPager.setOffscreenPageLimit(-1);
        mViewPager.setAdapter(apdater);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mPics.get(position).getDesc());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mPosition);
        setTitle(mPics.get(mPosition).getDesc());
    }


    class MyApdater extends PagerAdapter{

        @Override
        public int getCount() {
            return mPics.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=View.inflate(context,R.layout.dialog_show_pic,null);
            ImageView showPic= (ImageView) v.findViewById(R.id.iv_show_pic);
            showPic.setOnClickListener(ImageDialog.this);
            ImageLoader.getInstance().displayImage(mPics.get(position).getSrc(), showPic);

            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }
}
