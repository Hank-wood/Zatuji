package com.joe.zatuji.gallerypage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.joe.zatuji.R;
import com.joe.zatuji.base.adapter.BaseStaggeredAdapter;
import com.joe.zatuji.base.adapter.DataViewHolder;
import com.joe.zatuji.base.model.BaseData;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.favoritepage.model.MyFavorite;
import com.joe.zatuji.global.Constant;
import com.joe.zatuji.global.utils.DPUtils;
import com.joe.zatuji.picdetail.PicDetailActivity;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Joe on 2016/4/16.
 */
public class GalleryAdapter extends BaseStaggeredAdapter {

    private ArrayList<MyFavorite> mDatas;


    public GalleryAdapter(Context context) {
        super(context);

    }
    public void refreshData(ArrayList<MyFavorite> data,boolean isMore){
        if(isMore){
            mDatas.addAll(data);
        }else{
            mDatas =  data;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        MyFavorite picData=mDatas.get(position);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.ivPic.getLayoutParams();
        params.height=picData.getHeight();
        if(picData.getHeight()> DPUtils.dip2px(mContext,300)) params.height=DPUtils.dip2px(mContext,300);
        if(picData.getHeight()< DPUtils.dip2px(mContext,120)) params.height=DPUtils.dip2px(mContext,120);
        holder.ivPic.setBackgroundColor(mContext.getResources().getColor(getRandomColor()));
        holder.ivPic.setLayoutParams(params);
        //替换为imageloader
        x.image().bind(holder.ivPic,picData.getImg_url());
        final int pos=position;
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, PicDetailActivity.class);
                i.putExtra(Constant.PIC_DATA,mDatas.get(pos).getImg_url().replace(Constant.HOST_PIC,""));
                i.putExtra(Constant.PIC_DESC,mDatas.get(pos).getDesc());
                i.putExtra(Constant.PIC_WIDTH,mDatas.get(pos).getWidth());
                i.putExtra(Constant.PIC_HEIGHT,mDatas.get(pos).getHeight());
                mContext.startActivity(i);
            }
        });
    }
    private int[] defaultColor=new int[]{R.color.DefaultGreen,R.color.DefaultBlue,
            R.color.DefaultRed,R.color.DefaultPurple};
    private int getRandomColor() {
        Random random=new Random();
        return defaultColor[ random.nextInt(4)];
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }
}
