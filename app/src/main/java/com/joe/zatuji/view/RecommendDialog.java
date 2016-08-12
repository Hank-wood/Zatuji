package com.joe.zatuji.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.joe.zatuji.MyApplication;
import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.WelcomeCover;
import com.joe.zatuji.view.base.BaseDialog;

import static android.R.id.input;

/**
 * Created by joe on 16/6/3.
 */
public class RecommendDialog extends BaseDialog {

    private TextView mTitle;
    private EditText mRecommend;
    private TextView mCancel;
    private TextView mComplete;
    private EditText mWeiBo;
    private String mUrl;

    public RecommendDialog(Context context){
        super(context);
    }
    public RecommendDialog(Context context, String title, String hint) {
        super(context);
        mTitle.setText(title);
        mRecommend.setHint(hint);
    }
    @Override
    protected int getLayout() {
        return R.layout.dialog_recommend;
    }

    @Override
    protected void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title_input_dialog);
        mRecommend = (EditText) findViewById(R.id.et_reason_recommend_dialog);
        mWeiBo = (EditText) findViewById(R.id.et_weibo_recommend_dialog);
        mCancel = (TextView) findViewById(R.id.tv_cancel_input_dialog);
        mComplete = (TextView) findViewById(R.id.tv_complete_input_dialog);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRecommend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mWatcher!= null) mWatcher.onChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    String reason = mRecommend.getText().toString();
                    if(TextUtils.isEmpty(reason)){
                        mListener.onEmptyReason();
                        return;
                    }
                    String weibo = mWeiBo.getText().toString();
                    WelcomeCover cover = new WelcomeCover();
                    cover.reason = reason;
                    cover.url = mUrl;
                    cover.weiBo = weibo;
                    cover.by = MyApplication.mUser.nickname;
                    cover.using = false;
                    mListener.onComplete(cover,RecommendDialog.this);
                }
            }
        });
    }
    public void setImage(String url){
        this.mUrl = url;
    }

    public void setTitleAndHint(String title,String hint){
        mTitle.setText(title);
        mRecommend.setHint(hint);
    }

    public void setContent(String content){
        mRecommend.setText(content);
    }
    public void clearInput(){
        mRecommend.setText("");
    }

    private OnCompleteListener mListener;
    public void setOnCompleteListener(OnCompleteListener listener){
        this.mListener = listener;
    }
    public interface OnCompleteListener{
        void onComplete(WelcomeCover input, RecommendDialog dialog);
        void onEmptyReason();
    }

    private int mType = -1;
    public void setType(int type){
        mType = type;
    }

    public int getType(){
        return this.mType;
    }

    private OnTextWatcher mWatcher;
    public void setOnTextChangeWatcher(OnTextWatcher watcher){
        this.mWatcher = watcher;
    }
    public interface OnTextWatcher{
        void onChange(String text);
    }
}
