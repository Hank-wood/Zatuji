package com.joe.zatuji.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.TagBean;
import com.joe.zatuji.utils.LogUtils;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;
import cc.solart.turbo.OnItemClickListener;
import cc.solart.turbo.TurboRecyclerView;

/**
 * Created by joe on 16/5/28.
 */
public class DropMenuDialog extends PopupWindow {
    private Context  mContext;
    private TurboRecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private Window window;
    private View mRootView;

    public DropMenuDialog(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.dialog_drop_menu,null);
        setContentView(mRootView);
        initView();
        initListener();
    }

    protected int getLayout() {
//        window = this.getWindow();
//        window.requestFeature(Window.FEATURE_NO_TITLE);

        return R.layout.dialog_drop_menu;
    }

    protected void initView() {
        //去掉dialog默认的padding
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        //设置dialog的位置在底部
//        lp.gravity = Gravity.BOTTOM;
//
//        window.setAttributes(lp);
        LogUtils.d("convert dialog");
        mRecyclerView = (TurboRecyclerView) mRootView.findViewById(R.id.recycler_drop_menu);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        mAdapter = new SimpleAdapter(mContext);
        mAdapter.setNewData(new TagBean().tagList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreEnabled(false);
    }

    protected void initListener() {
        mAdapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                dismiss();
            }
        });
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
