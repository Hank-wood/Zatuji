package com.joe.zatuji.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.joe.zatuji.R;

/**
 * Created by Joe on 2016/4/16.
 */
public class DataViewHolder extends RecyclerView.ViewHolder{
    public ImageView ivPic;
    //public TextView tvDesc;

    public DataViewHolder(View itemView) {
        super(itemView);
        ivPic= (ImageView) itemView.findViewById(R.id.iv_pic_item);
        //tvDesc = (TextView) itemView.findViewById(R.id.tv_desc_item);
    }
}