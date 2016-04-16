package com.joe.huaban.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joe.huaban.R;

/**
 * Created by Joe on 2016/4/16.
 */
public class BaseStagerdAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DataViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPic;

        public DataViewHolder(View itemView) {
            super(itemView);
            ivPic= (ImageView) itemView.findViewById(R.id.iv_pic_item);
        }
    }
}
