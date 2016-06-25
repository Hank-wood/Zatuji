package com.joe.zatuji.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.utils.LogUtils;
import com.joe.zatuji.view.base.BaseDialog;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;
import cc.solart.turbo.OnItemClickListener;
import cc.solart.turbo.TurboRecyclerView;

/**
 * Created by joe on 16/5/28.
 */
public class DropMenuDialog extends BaseDialog implements View.OnClickListener{
    private TurboRecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private Window window;

    public DropMenuDialog(Context context) {
        super(context,R.style.dialog_fullscreen);
    }//

    protected int getLayout() {
        window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        return R.layout.dialog_drop_menu;
    }
    @Override
    protected void initView() {
//        去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setWindowAnimations(R.style.dialog_anim);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        LogUtils.d("convert dialog");

        mRecyclerView = (TurboRecyclerView)findViewById(R.id.recycler_drop_menu);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        findViewById(R.id.ll_container_drop).setOnClickListener(this);
        mAdapter = new SimpleAdapter(mContext);
        mAdapter.resetData(new TagBean().tagList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreEnabled(false);
    }
    @Override
    protected void initListener() {
        mAdapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                if(mListener!=null) mListener.onMenuClick(mAdapter.getItem(position));
                dismiss();
            }
        });
    }
    private OnMenuClickListener mListener;
    public void setOnMenuClickListener(OnMenuClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        LogUtils.d("click-drop menu");
        if(v.getId()!=R.id.recycler_drop_menu)dismiss();
    }


    public interface OnMenuClickListener{
        void onMenuClick(TagBean.Tag tag);
    }
    class SimpleAdapter extends BaseTurboAdapter<TagBean.Tag,SimpleAdapter.Holder>{
        public SimpleAdapter(Context context) {
            super(context);
        }

        @Override
        protected Holder onCreateDefViewHolder(ViewGroup parent, int viewType) {
            return new Holder(inflateItemView(R.layout.item_drop_menu,null));
        }

        @Override
        protected void convert(Holder holder, TagBean.Tag tag) {
            LogUtils.d("convert dialog");
            holder.textView.setText(tag.name);
        }

        class Holder extends BaseViewHolder{
            TextView textView;
            public Holder(View view) {
                super(view);
                textView = findViewById(R.id.tv_drop_menu);
            }
        }
    }

}
