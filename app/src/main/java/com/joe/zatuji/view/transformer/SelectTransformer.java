package com.joe.zatuji.view.transformer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Describe
 * Author Joe
 * created at 16/9/5.
 */

public class SelectTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if(position<-1){
            page.setAlpha(1);
        } else if(position<=1){
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float verMargin = pageHeight * (1 - scaleFactor) / 2;
            float horMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horMargin - verMargin / 2);
            } else {
                page.setTranslationX(-horMargin + verMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
//            page.setAlpha(MIN_ALPHA +
//                    (scaleFactor - MIN_SCALE) /
//                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            page.setAlpha(1);
        }else{
            page.setAlpha(1);
        }
    }
}
