package com.joe.zatuji.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.joe.zatuji.R;
import com.joe.zatuji.data.bean.FavoriteTag;

import java.util.ArrayList;

/**
 * Created by Joe on 2016/5/1.
 */
public class ChooseTagDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private ListView mList;
    private Button mCreate;
    private FavoriteTag mTag;
    private ArrayList<FavoriteTag> tags;
    private MyAdapter mAdapter;

    public ChooseTagDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ChooseTagDialog(Context context, ArrayList<FavoriteTag> tags) {
        this(context,R.style.dialog_theme);
        this.context = context;
        this.tags = tags;
        setContentView(R.layout.dialog_choose_tag);
        initView();
    }


    private void initView() {
        mList = (ListView) findViewById(R.id.lv_choose_dialog);
        mCreate = (Button) findViewById(R.id.bt_create_tag);
        findViewById(R.id.bt_cancel_tag).setOnClickListener(this);
        mCreate.setOnClickListener(this);
        findViewById(R.id.tv_create_choose).setOnClickListener(this);

        mAdapter = new MyAdapter();
        mList.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTag = mAdapter.getItem(position);
                mAdapter.setCheck(position);
            }
        });
        mTag = tags.get(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_create_tag:
                chooseTag();
                break;
            case R.id.bt_cancel_tag:
                this.dismiss();
                break;
            case R.id.tv_create_choose:
                mCreateTag.onCreateTag(this);
                break;
        }
    }

    private void chooseTag() {
        if(mOnchooseTag!=null) mOnchooseTag.onChooseTag(mTag,this);
    }
    private OnChooseTag mOnchooseTag;
    public void setOnChooseTag(OnChooseTag mOnchooseTag){
        this.mOnchooseTag = mOnchooseTag;
    }
    public interface OnChooseTag{
        void onChooseTag(FavoriteTag tag, ChooseTagDialog chooseTagDialog);
    }

    private CreateTag mCreateTag;
    public void setCreateTag(CreateTag mCreateTag){
        this.mCreateTag = mCreateTag;
    }
    public interface CreateTag{
        void onCreateTag(ChooseTagDialog dialog);
    }

    class MyAdapter extends BaseAdapter{
        private int currentCheck = 0;
        @Override
        public int getCount() {
            return tags.size();
        }

        @Override
        public FavoriteTag getItem(int position) {
            return tags.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FavoriteTag tag = tags.get(position);
            convertView = View.inflate(context,R.layout.item_tag_choose,null);
            RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.rb_tag_item);
            radioButton.setText(tag.tag+"("+tag.number+")");
            if(position!=currentCheck) {
                radioButton.setChecked(false);
            }else{
                radioButton.setChecked(true);
            }
            return convertView;
        }

        public void setCheck(int position) {
            currentCheck = position;
            notifyDataSetChanged();
        }
    }
}
