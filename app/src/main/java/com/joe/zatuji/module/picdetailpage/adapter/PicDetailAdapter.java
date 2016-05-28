package com.joe.zatuji.module.picdetailpage.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.PicData;
import com.joe.zatuji.Constant;

import org.xutils.x;

import java.util.List;

/**
 * Created by Joe on 2016/4/16.
 */
public class PicDetailAdapter extends PagerAdapter {
    private List<PicData.PinsBean> mDatas;
    private Activity mActivity;
    public PicDetailAdapter(Activity activity) {
        this.mActivity=activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = View.inflate(mActivity, R.layout.activity_pic_detail,null);
        ImageView ivPic = (ImageView) itemView.findViewById(R.id.iv_pic_item);
        TextView tvDesc = (TextView) itemView.findViewById(R.id.tv_desc_item);
        PicData.PinsBean data=mDatas.get(position);
        tvDesc.setVisibility(View.VISIBLE);
        x.image().bind(ivPic, Constant.HOST_PIC+data.file.key);
        if(!TextUtils.isEmpty(data.raw_text)) {
            tvDesc.setText(data.raw_text);
        }else {
            tvDesc.setVisibility(View.GONE);
        }
        container.addView(itemView);
        itemView.setId(position);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public void setData(PicData data){
        mDatas=data.pins;
    }
}
