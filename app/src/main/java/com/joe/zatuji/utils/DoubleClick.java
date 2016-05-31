package com.joe.zatuji.utils;

/**
 * 1.simply use DoubleClick.isDoubleClick(viewID) to handle double click event with default duration,
 *   in that case the default duration is 500 ms;
 * 2.using setDefaultDuration(duration) to redefine the default duration,
 *   you have to notice that default duration is a global variable,once the redefine code has been
 *   ran in one class,anyplace where used isDoubleClick(viewID) would use the new default duration
 *   so be careful when handle it;
 * 3.use DoubleClick.isDoubleClick(viewID,duration) to handle double click event with custom duration,
 *   this duration has no inflect to default duration
 *
 * Created by joe on 16/5/17.
 */
public class DoubleClick {
    private static DoubleClick instance;
    private int mTag = -1;
    private static long mLastClick = 0;
    private static long deDuration = 1000;

    /**
     * init the instance of DoubleClick
    */
    private static void initInstance(int viewId){
        if(instance == null) instance = new DoubleClick();
        if(instance.getTag() != viewId){
            instance.setTag(viewId);
            instance.mLastClick = 0;
        }
    }

    /**
     * default duration
     * params viewID = R.id.xxx
    */
    public static boolean isDoubleClick(int viewId){
        initInstance(viewId);
        return compare(deDuration);
    }

    /**
     * custom duration
     * params duration = 500
    */
    public static boolean isDoubleClick(int viewId,long duration){
        initInstance(viewId);
        return compare(duration);
    }

    /**
     * compare the duration between two clicks
     */
    private static boolean compare(long duration) {
        long currentClick = System.currentTimeMillis();
        boolean isDouble = (currentClick - mLastClick)<duration?true:false;
        mLastClick = currentClick;
        return isDouble;
    }
    /**
     * set the default duration
     */
    public static void setDefaultDuration(long duration){
        deDuration = duration;
    }

    /**
     * destroy the instance
     */
    public static void clear(){
        if (instance!=null) instance = null;
    }

    private int getTag() {
        return mTag;
    }

    private void setTag(int mTag) {
        this.mTag = mTag;
    }
}
