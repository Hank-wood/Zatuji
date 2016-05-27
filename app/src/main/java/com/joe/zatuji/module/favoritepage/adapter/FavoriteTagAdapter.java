package com.joe.zatuji.module.favoritepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.zatuji.R;
import com.joe.zatuji.base.adapter.DataViewHolder;
import com.joe.zatuji.module.favoritepage.model.FavoriteTag;

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
        boolean isCreate=false;
        FavoriteTag tag = mTags.get(position);
        holder.tvDesc.setText(tag.getTag()+"("+tag.getNumber()+")");
        x.image().bind(holder.ivPic,tag.getFront());
        //替换为imageloader
        final int pos=position;
        final FavoriteTag finalTag = tag;
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onItemClickListener(pos, finalTag);
                }
            }
        });
        holder.ivPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongListener.onItemLongClickListener(pos,finalTag);
                return true;
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
        return mTags==null?0:mTags.size();
    }

    private ItemClickListener mListener;
    public void setOnItemClickListener(ItemClickListener listener){
        this.mListener = listener;
    }

    public  interface ItemClickListener{
        void onItemClickListener(int position,FavoriteTag tag);
    }

    private ItemLongClickListener mLongListener;
    public void setOnItemLongClickListener(ItemLongClickListener listener){
        this.mLongListener = listener;
    }

    public  interface ItemLongClickListener{
        void onItemLongClickListener(int position,FavoriteTag tag);
    }
}
