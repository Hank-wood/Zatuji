package com.joe.huaban.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joe.huaban.R;
import com.joe.huaban.base.model.BaseData;
import com.joe.huaban.global.utils.LogUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by Joe on 2016/4/16.
 */
public class BaseStagerdAdapter extends RecyclerView.Adapter<DataViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    public BaseStagerdAdapter(Context context) {
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_pic_rv,parent,false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
