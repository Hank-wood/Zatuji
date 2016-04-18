package com.joe.huaban.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.joe.huaban.base.adapter.BaseStaggeredAdapter;
import com.joe.huaban.base.adapter.DataViewHolder;
import com.joe.huaban.base.model.BaseData;
import com.joe.huaban.global.Constant;
import com.joe.huaban.base.model.PicData;
import com.joe.huaban.picdetail.PicDetailActivity;

import org.xutils.x;

import java.util.List;

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
        //替换为imageloader
        x.image().bind(holder.ivPic, Constant.HOST_PIC+mDatas.get(position).file.key);
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

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }
}
