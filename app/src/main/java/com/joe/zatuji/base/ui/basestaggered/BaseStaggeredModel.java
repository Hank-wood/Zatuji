package com.joe.zatuji.base.ui.basestaggered;

import android.text.TextUtils;

import com.joe.zatuji.base.model.BaseModel;
import com.joe.zatuji.dao.CacheDao;
import com.joe.zatuji.data.bean.DataBean;
import com.joe.zatuji.utils.LogUtils;


/**
 * Created by joe on 16/5/28.
 */
public class BaseStaggeredModel  implements BaseModel{
    public void saveToCache(String max,DataBean dataBean,String tag){
        CacheDao cacheDao = new CacheDao();
        LogUtils.d("max:"+max);
        if(TextUtils.isEmpty(max)) cacheDao.clearTagCache(tag);
        if(dataBean!=null && dataBean.pins.size()>0)cacheDao.intoCache(dataBean.pins,tag);
    }
}
