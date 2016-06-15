package com.joe.zatuji.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.FavoriteTag;
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
public class BottomMenuDialog extends BaseDialog implements View.OnClickListener{
    private Window window;
    private FavoriteTag mTag;
    public BottomMenuDialog(Context context , FavoriteTag tag) {
        super(context,R.style.dialog_fullscreen);
        this.mTag = tag;
    }

    public BottomMenuDialog(Context context) {
        super(context,R.style.dialog_fullscreen);
    }
    protected int getLayout() {
        window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        return R.layout.dialog_bottom_menu;
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
        findViewById(R.id.tv_edit_dialog).setOnClickListener(this);
        findViewById(R.id.tv_delete_dialog).setOnClickListener(this);
        findViewById(R.id.tv_cancel_dialog).setOnClickListener(this);
    }
    @Override
    protected void initListener() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel_dialog:
                dismiss();
                break;
            case R.id.tv_edit_dialog:
                mListener.onEdit(mTag);
                dismiss();
                break;
            case R.id.tv_delete_dialog:
                break;
        }

    }

    private OnMenuClickListener mListener;
    public void setOnMenuClickListener(OnMenuClickListener mListener){
        this.mListener = mListener;
    }

    public interface OnMenuClickListener{
        void onEdit(FavoriteTag tag);
        void onDelete(FavoriteTag tag);
    }

    public void setTag(FavoriteTag tag){
        this.mTag = tag;
    }

}
