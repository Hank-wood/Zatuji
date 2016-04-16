package com.joe.huaban.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.joe.huaban.base.adapter.BaseStagerdAdapter;
import com.joe.huaban.base.adapter.DataViewHolder;
import com.joe.huaban.base.model.BaseData;
import com.joe.huaban.global.Constant;
import com.joe.huaban.homepage.model.HomeData;

import org.xutils.x;

import java.util.List;

/**
 * Created by Joe on 2016/4/16.
 */
public class HomeAdapter extends BaseStagerdAdapter {

    private List<HomeData.PinsBean> mDatas;
    public HomeAdapter(Context context) {
        super(context);

    }
    public void refreshData(BaseData data,boolean isMore){
        HomeData mData= (HomeData) data;
        if(isMore){
            mDatas.addAll(((HomeData) data).pins);
        }else{
            mDatas=  mData.pins;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.tvDesc.setVisibility(View.VISIBLE);
        x.image().bind(holder.ivPic, Constant.HOST_PIC+mDatas.get(position).file.key);
        if(TextUtils.isEmpty((mDatas.get(position).raw_text))){
            holder.tvDesc.setVisibility(View.GONE);
        }
        holder.tvDesc.setText(mDatas.get(position).raw_text);
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }
}
