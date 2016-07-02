package com.joe.zatuji.module.picdetailpage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joe.zatuji.R;
import com.joe.zatuji.api.Api;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.data.bean.MyFavorite;
import com.joe.zatuji.helper.ImageHelper;
import com.joe.zatuji.utils.LogUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by joe on 16/7/2.
 */
public class PicDetailAdapter extends PagerAdapter {
    private ArrayList<DataBean.PicBean> mPics = new ArrayList<>();
    private ArrayList<MyFavorite> myFavorites = new ArrayList<>();
    private boolean isGallery;
    private Context context;

    public PicDetailAdapter(Context context) {
        this.context = context;
    }

    public void setMyFavorites(ArrayList<MyFavorite> myFavorites){
        if(myFavorites!=null){
            this.myFavorites = myFavorites;
            isGallery = true;
            notifyDataSetChanged();
        }
    }

    public void setPics(ArrayList<DataBean.PicBean> mPics){
        if(mPics!=null){
            this.mPics = mPics;
            isGallery = false;
            notifyDataSetChanged();
        }
    }

    public void addMyFavorites(ArrayList<MyFavorite> myFavorites){
        if(myFavorites!=null){
            this.myFavorites.addAll(myFavorites);
            notifyDataSetChanged();
        }
    }

    public void addPics(ArrayList<DataBean.PicBean> mPics){
        if(mPics!=null){
            this.mPics.addAll(mPics);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {

        return myFavorites.size()==0?mPics.size():myFavorites.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(context, R.layout.item_pic_detail_viewpager,null);
        container.addView(view);
        ImageView image = (ImageView) view.findViewById(R.id.iv_pic_gif);
        PhotoViewAttacher attacher = null;
        if(isGallery) {
            attacher = showPic(image, gallery2PicBean(myFavorites.get(position)));
        }else {
            attacher = showPic(image,mPics.get(position));
        }
        if(attacher!=null){
            attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    if(isGallery){
                        mListener.OnItemClicked(position, gallery2PicBean(myFavorites.get(position)));
                    }else {
                        mListener.OnItemClicked(position,mPics.get(position));
                    }
                }
                @Override
                public void onOutsidePhotoTap() {}
            });
        }else{
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isGallery){
                        mListener.OnItemClicked(position, gallery2PicBean(myFavorites.get(position)));
                    }else {
                        mListener.OnItemClicked(position,mPics.get(position));
                    }
                }
            });
        }
        return view;
    }

    private PhotoViewAttacher showPic(ImageView gif, DataBean.PicBean picBean) {
            return ImageHelper.showBig(gif,picBean);
    }

    private DataBean.PicBean gallery2PicBean(MyFavorite favorite) {
        DataBean.PicBean picBean = new DataBean.PicBean();
        picBean.file = new DataBean.PicBean.FileBean();
        picBean.raw_text = favorite.desc;
        picBean.file.height = favorite.height;
        picBean.file.width = favorite.width;
        picBean.file.type =favorite.type;
        picBean.file.key =favorite.img_url.substring(Api.HOST_PIC.length());
        return picBean;
    }
    public DataBean.PicBean getItem(int positon){
        if(isGallery) return (gallery2PicBean(myFavorites.get(positon)));
        return mPics.get(positon);
    }
    public void setOnItemClickListener(onItemClickListener mListener) {
        this.mListener = mListener;
    }
    private onItemClickListener mListener;

    public interface onItemClickListener{
        void OnItemClicked(int position, DataBean.PicBean picBean);
    }

}
