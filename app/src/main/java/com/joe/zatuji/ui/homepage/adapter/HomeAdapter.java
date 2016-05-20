package com.joe.zatuji.ui.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.joe.zatuji.R;
import com.joe.zatuji.base.adapter.BaseStaggeredAdapter;
import com.joe.zatuji.base.adapter.DataViewHolder;
import com.joe.zatuji.base.BaseData;
import com.joe.zatuji.Constant;
import com.joe.zatuji.data.bean.PicData;
import com.joe.zatuji.utils.DPUtils;
import com.joe.zatuji.ui.picdetail.PicDetailActivity;

import org.xutils.x;

import java.util.List;
import java.util.Random;

/**
 * Created by Joe on 2016/4/16.
 */
public class HomeAdapter extends BaseStaggeredAdapter {

    private List<PicData.PinsBean> mDatas;


    public HomeAdapter(Context context) {
        super(context);

    }
    public void refreshData(BaseData data,boolean isMore){
         PicData mData = (PicData) data;
        if(isMore){
            mDatas.addAll(((PicData) data).pins);
        }else{
            mDatas=  mData.pins;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        PicData.PinsBean.FileBean picData=mDatas.get(position).file;
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.ivPic.getLayoutParams();
        params.height=picData.height;
        if(picData.height> DPUtils.dip2px(mContext,300)) params.height=DPUtils.dip2px(mContext,300);
        if(picData.height< DPUtils.dip2px(mContext,120)) params.height=DPUtils.dip2px(mContext,120);
        holder.ivPic.setBackgroundColor(mContext.getResources().getColor(getRandomColor()));
        holder.ivPic.setLayoutParams(params);
        //替换为imageloader
        x.image().bind(holder.ivPic, Constant.HOST_PIC+picData.key);
        final int pos=position;
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, PicDetailActivity.class);
                i.putExtra(Constant.PIC_DATA,mDatas.get(pos).file.key);
                i.putExtra(Constant.PIC_DESC,mDatas.get(pos).raw_text);
                i.putExtra(Constant.PIC_WIDTH,mDatas.get(pos).file.width);
                i.putExtra(Constant.PIC_HEIGHT,mDatas.get(pos).file.height);
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
