package com.joe.zatuji.base.ui.basestaggered;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.DPUtils;

import java.util.Random;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by joe on 16/5/27.
 */
public class BaseStaggeredAdapter extends BaseTurboAdapter<DataBean.PicBean,BaseStaggeredAdapter.BaseStaggeredHolder> {

    public BaseStaggeredAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseStaggeredHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new BaseStaggeredHolder(inflateItemView(R.layout.item_pic_rv,parent));
    }

    @Override
    protected void convert(BaseStaggeredHolder holder, final DataBean.PicBean picBean) {
        final DataBean.PicBean.FileBean fileBean= picBean.file;
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.ivPic.getLayoutParams();
        params.height=fileBean.height;
        if(fileBean.height> DPUtils.dip2px(mContext,300)) params.height=DPUtils.dip2px(mContext,300);
        if(fileBean.height< DPUtils.dip2px(mContext,120)) params.height=DPUtils.dip2px(mContext,120);
        holder.ivPic.setBackgroundColor(mContext.getResources().getColor(getRandomColor()));
        holder.ivPic.setLayoutParams(params);
        //替换为Glide
        ImageHelper.showSmall(holder.ivPic,fileBean.key,fileBean.type);

    }

    class BaseStaggeredHolder extends BaseViewHolder{
        public ImageView ivPic;
        public BaseStaggeredHolder(View view) {
            super(view);
            ivPic = findViewById(R.id.iv_pic_item);
        }
    }

    private int[] defaultColor=new int[]{R.color.DefaultGreen,R.color.DefaultBlue,
            R.color.DefaultRed,R.color.DefaultPurple};
    private int getRandomColor() {
        Random random=new Random();
        return defaultColor[ random.nextInt(4)];
    }
}

