package com.joe.huaban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joe.huaban.ImageDialog;
import com.joe.huaban.R;
import com.joe.huaban.spider.Picture;
import com.joe.huaban.utils.LogUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by Joe on 2016/3/11.
 */
public class PicAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private List<Picture> mPics;
    private LayoutInflater mInflater;
    public PicAdapter(Context context,List<Picture> pics) {
        this.mContext=context;
        this.mPics=pics;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=mInflater.inflate(R.layout.item_pic_rv,parent,false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    public void addData(List<Picture> newPics){
        mPics.addAll(newPics);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picture pic=mPics.get(position);
        if(TextUtils.isEmpty(pic.getSrc())){
            holder.img.setVisibility(View.GONE);
            holder.desc.setVisibility(View.GONE);
            return;
        }
        x.image().bind(holder.img,pic.getSrc());
        if(TextUtils.isEmpty(pic.getDesc())){
            holder.desc.setVisibility(View.GONE);
            return;
        }
        holder.desc.setText(pic.getDesc());
        holder.img.setVisibility(View.VISIBLE);
        holder.desc.setVisibility(View.VISIBLE);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigPicture(position);
            }
        });
    }

    private void showBigPicture(int position) {
        new ImageDialog(mContext,mPics,position).show();
    }

    @Override
    public int getItemCount() {
        LogUtils.Logout("传入Adapter数据长度："+mPics.size());
        return mPics.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView desc;
    ImageView img;
    public MyViewHolder(View itemView) {
        super(itemView);
        desc= (TextView) itemView.findViewById(R.id.tv_desc_item);
        img= (ImageView) itemView.findViewById(R.id.iv_pic_item);
    }
}