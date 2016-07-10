package com.joe.zatuji.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.FavoriteTag;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.view.base.BaseDialog;

/**
 * Created by joe on 16/5/28.
 */
public class GalleryMenuDialog extends BaseDialog implements View.OnClickListener{
    private Window window;
    private MyFavorite mImg;
    public GalleryMenuDialog(Context context , MyFavorite mImg) {
        super(context,R.style.dialog_fullscreen);
        this.mImg = mImg;
    }

    public GalleryMenuDialog(Context context) {
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
        TextView mSetFront = (TextView) findViewById(R.id.tv_edit_dialog);
        mSetFront.setOnClickListener(this);
        mSetFront.setText("设为封面");

        TextView mDelete = (TextView) findViewById(R.id.tv_delete_dialog);
        mDelete.setOnClickListener(this);
        mDelete.setText("移除");
        findViewById(R.id.tv_cancel_dialog).setOnClickListener(this);
        this.setCanceledOnTouchOutside(true);
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
                mListener.onFront(mImg);
                dismiss();
                break;
            case R.id.tv_delete_dialog:
                mListener.onDelete(mImg);
                dismiss();
                break;
        }

    }

    private OnMenuClickListener mListener;
    public void setOnMenuClickListener(OnMenuClickListener mListener){
        this.mListener = mListener;
    }

    public interface OnMenuClickListener{
        void onFront(MyFavorite img);
        void onDelete(MyFavorite img);
    }

    public void setTag(MyFavorite img){
        this.mImg= img;
    }

}
