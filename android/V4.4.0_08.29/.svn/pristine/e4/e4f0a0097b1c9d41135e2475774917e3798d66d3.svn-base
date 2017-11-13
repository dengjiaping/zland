package com.zhisland.android.blog.common.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.zhisland.lib.util.MLog;

import java.util.HashMap;

/**
 * 用来自动滑动group中的view
 * <p/>
 * Created by 向飞 on 2016/5/19.
 */
public class AnimatedSlideGroup extends ViewGroup {

    private static String TAG = "slideGroup";

    private HashMap<View, SlideRange> slideParams = new HashMap<>();

    public AnimatedSlideGroup(Context context) {
        super(context);
    }

    public AnimatedSlideGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedSlideGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int index = 0, count = this.getChildCount(); index < count; index++) {

            View view = this.getChildAt(index);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        MLog.d(TAG, "onlayout ");
        for (int index = 0, count = this.getChildCount(); index < count; index++) {

            View view = this.getChildAt(index);
            if (!slideParams.containsKey(view)) {
                continue;
            }

            SlideRange slide = slideParams.get(view);
            if (slide.slided < 1) {
//                MLog.d(TAG, "view not slide ");
                continue;
            }

//            MLog.d(TAG, "layout child view ");
            view.layout(0, slide.cur, r, slide.cur + view.getMeasuredHeight());//TODO 使用view的真正尺寸

        }

    }


    /**
     * 开始移动某个view
     *
     * @param view
     */
    public void startSlide(final View view, final SlideRange slide) {

//        MLog.d(TAG, "into start view " + slide.toString());

        //保存滑动参数
        slideParams.put(view, slide);

        ValueAnimator animator = ValueAnimator.ofInt(slide.start, slide.end);

        Interpolator interpolator = slide.interpolator;
        if (interpolator == null) {
            interpolator = new AccelerateDecelerateInterpolator();
        }
        animator.setInterpolator(interpolator);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animateStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animateFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animateFinish();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            //动画开始
            private void animateStart() {
//                MLog.d(TAG, "start slide " + slide.toString());
                slide.cur = slide.start;
                slide.slided = 1;

                for (int index = 0, count = getChildCount(); index < count; index++) {
                    View child = getChildAt(index);
                    if (view.equals(child))
                        return;
                }
                addView(view);
            }

            //动画结束或者取消
            private void animateFinish() {
//                MLog.d(TAG, "finish slide " + slide.toString());
                slide.slided = 0;
                if (!slide.stayInGroup) {
                    MLog.d(TAG, "remove the view when animate finish");
                    removeView(view);
                }
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                slide.cur = value;
                requestLayout();
            }
        });
        animator.setDuration(slide.duration);
        animator.start();

    }


    /**
     * 用来定义view滑动的参数阶段
     * <p>目前只支持垂直滑动</p>
     */
    public static class SlideRange {

        private int start;//起始位置
        private int end;//结束位置
        private long duration;//持续时间
        private boolean stayInGroup = true;//是否留在ViewGroup中
        private Interpolator interpolator;//动画速度解析

        private int cur;//现在的位置
        private int slided;//是否在滑动

        public SlideRange() {
        }

        /**
         * @param start    起始位置
         * @param end      结束位置
         * @param duration 持续时间
         */
        public SlideRange(int start, int end, long duration) {
            this.start = start;
            this.end = end;
            this.duration = duration;
        }

        public void setStayInGroup(boolean stay) {
            this.stayInGroup = stay;
        }

        public Interpolator getInterpolator() {
            return interpolator;
        }

        public void setInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
        }

        @Override
        public String toString() {
            return String.format("start :%d, end: %d, duration: %d", start, end, duration);
        }
    }
}
