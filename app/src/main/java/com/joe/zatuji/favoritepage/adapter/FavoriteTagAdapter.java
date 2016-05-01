package com.joe.zatuji.favoritepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.joe.zatuji.R;
import com.joe.zatuji.base.adapter.DataViewHolder;
import com.joe.zatuji.base.model.BaseData;
import com.joe.zatuji.base.model.PicData;
import com.joe.zatuji.favoritepage.model.FavoriteTag;
import com.joe.zatuji.global.Constant;
import com.joe.zatuji.global.utils.DPUtils;
import com.joe.zatuji.picdetail.PicDetailActivity;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Joe on 2016/4/16.
 */
public class FavoriteTagAdapter extends RecyclerView.Adapter<DataViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    private ArrayList<FavoriteTag> mTags;
    public FavoriteTagAdapter(Context context) {
        this.mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mTags = new ArrayList<FavoriteTag>();
    }
    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.item_tag_rv,parent,false);
        return new DataViewHolder(itemView);
    }



    public void refreshData(ArrayList<FavoriteTag> tags, boolean isMore){
        if(isMore){
            this.mTags.addAll(tags);
        }else{
            this.mTags=tags;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        holder.ivPic.setBackgroundColor(mContext.getResources().getColor(getRandomColor()));
        FavoriteTag tag=null;

        boolean isCreate=false;
        if(position == mTags.size()){
            holder.tvDesc.setText("新建图集");
            isCreate=true;
        }else{
            tag = mTags.get(position);
            holder.tvDesc.setText(tag.getTag()+"("+tag.getNumber()+")");
            x.image().bind(holder.ivPic,tag.getFront());
            //替换为imageloader
        }
        final int pos=position;
        final FavoriteTag finalTag = tag;
        final boolean finalIsCreate = isCreate;
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClickListener(pos, finalTag, finalIsCreate);
                }
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
        return mTags==null?1:mTags.size()+1;
    }

    private ItemClickListener mListener;
    public void setOnItemClickListener(ItemClickListener listener){
        this.mListener = listener;
    }

    public  interface ItemClickListener{
        void onItemClickListener(int position,FavoriteTag tag,boolean isCreate);
    }
}
