package com.joe.zatuji.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import com.joe.zatuji.utils.LogUtils;

/**
 * Describe
 * Author Joe
 * created at 16/9/5.
 */

public class FabAnimatorHelper {
    private boolean isAnimating = false;
    private static FabAnimatorHelper helper;
    public static synchronized FabAnimatorHelper inst(){
        if(helper==null) helper = new FabAnimatorHelper();
        return helper;
    }
    public void startFabGroupAnimation(boolean dismiss,View...fabs){
        if(isAnimating) return;
        isAnimating = true;
        long delay = 0;
        ObjectAnimator animator = null;
        if(dismiss){
            for(int i = 0;i<fabs.length;i++){
                animator = startDismiss(fabs[i],delay);
                delay = delay+100;
            }
        }else{
            for(int i = fabs.length-1;i>=0;i--){
                animator = startShown(fabs[i],delay);
                delay = delay+100;
            }
        }
        if(animator == null) return;
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
    }
    public ObjectAnimator startShown(View fab, long delay){
        return startAnimation(fab,delay,0f,1.3f,1.1f,1f,0.9f,1f);
    }

    public ObjectAnimator startDismiss(View fab, long delay){
        return startAnimation(fab,delay,1f,1.1f,1.3f,0f);
    }
    public ObjectAnimator startAnimation(final View fab, long delay, float...value){
        ObjectAnimator animator = ObjectAnimator.ofFloat(fab,"dis",value);
        animator.setDuration(500).setStartDelay(delay);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (float) animation.getAnimatedValue();
                fab.setScaleX(cVal);
                fab.setScaleY(cVal);
                fab.setRotation(cVal*720);
                if(!fab.isShown()) fab.setVisibility(View.VISIBLE);
            }
        });
        return animator;
    }
    public abstract class AnimatorListener implements Animator.AnimatorListener{

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public abstract void onAnimationEnd(Animator animation);

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
