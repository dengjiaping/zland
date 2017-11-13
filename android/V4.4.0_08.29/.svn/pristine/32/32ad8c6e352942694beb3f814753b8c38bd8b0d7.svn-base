package com.zhisland.android.blog.home.view;

import java.util.ArrayList;
import java.util.Random;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.shrink.ShrinkRelativeLayout;

/**
 * 实现一种类似新郎微波发布的动画效果
 */
public class ActionAnimBuilder implements OnClickListener {

    private static final int BOT_MARGIN = -DensityUtil.dip2px(90);
    private static final int DURATION = 300;
    private static final int MAX_DELAY = 50;
    private static final int VER_INTERVAL = DensityUtil.dip2px(20);
    private static int ITEM_WIDTH = DensityUtil.dip2px(88);
    private static int ITEM_HEIGHT = DensityUtil.dip2px(118);
    private RelativeLayout container;
    private ArrayList<ActionItem> items;
    private SparseArray<ActionView> actionViews;
    private int horizontalInterval;
    private Random random;
    private ValueAnimator vaContainerShow, vaContainerHide;
    private OnActionItemClickListener itemListener;

    public ActionAnimBuilder(final RelativeLayout container) {
        this.container = container;
        actionViews = new SparseArray<ActionView>();
        horizontalInterval = (DensityUtil.getWidth() - ITEM_WIDTH * 3) / 4;
        random = new Random();
        vaContainerHide = ValueAnimator.ofFloat(1.0f, 0f);
        vaContainerHide.setDuration(DURATION + MAX_DELAY);
        vaContainerHide.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                container.setAlpha(value);
                if (value <= 0)
                    container.setVisibility(View.GONE);
            }
        });

        vaContainerShow = ValueAnimator.ofFloat(0f, 1.0f);
        vaContainerShow.setDuration(DURATION);
        vaContainerShow.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                container.setAlpha(value);
            }
        });
    }

    /**
     * 构造动画需要的属性
     *
     * @param context
     * @param items
     * @param itemListener
     * @param orientation  0：横向，1：竖向，默认是横向
     */
    public void build(Context context, ArrayList<ActionItem> items,
                      OnActionItemClickListener itemListener, int orientation) {
        if (orientation == 1) {
            buildVertical(context, items, itemListener);
        } else {
            buildHorizontal(context, items, itemListener);
        }
    }

    //横向排版
    private void buildHorizontal(Context context, ArrayList<ActionItem> items,
                                 OnActionItemClickListener itemListener) {
        this.items = items;
        this.itemListener = itemListener;

        int index = 0;
        for (ActionItem item : items) {

            int colIndex = index % 3;
            int rowIndex = index / 3;
            ActionView view = new ActionView(context, ITEM_WIDTH);
            view.setTag(item);
            view.iv.setImageResource(item.resId);
            view.tv.setText(item.title);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    ITEM_WIDTH, ITEM_HEIGHT);
            // param.bottomMargin = -itemSize * 2;
            param.leftMargin = (colIndex + 1) * horizontalInterval + colIndex
                    * ITEM_WIDTH;
            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            view.setLayoutParams(param);
            container.addView(view);

            item.marginBottom = DensityUtil.getHeight() * 3 / 7 - rowIndex
                    * (ITEM_HEIGHT + VER_INTERVAL);


            actionViews.put(item.id, view);

            view.setOnClickListener(this);

            item.colIndex = colIndex;
            item.rowIndex = rowIndex;
            item.vaShow = createHorizontalShowAnim(item);
            item.vaHide = createHorizontalHideAnim(item);

            index++;
        }
    }

    //竖向排版
    private void buildVertical(Context context, ArrayList<ActionItem> items,
                               OnActionItemClickListener itemListener) {
        this.items = items;
        this.itemListener = itemListener;

        int index = 0;
        int verticalInterval = DensityUtil.getHeight() * 2 / 25;
        int topMargin = (DensityUtil.getHeight() - verticalInterval * 2 - ITEM_HEIGHT * 3) / 2;
        for (ActionItem item : items) {

            ActionView view = createVerticalView(context, item);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    ITEM_WIDTH, ITEM_HEIGHT);
            param.leftMargin = (DensityUtil.getWidth() - ITEM_WIDTH) / 2;
            param.topMargin = topMargin + (verticalInterval + ITEM_HEIGHT) * index;
            view.setLayoutParams(param);

            view.setScaleY(0);
            view.setScaleX(0);

            container.addView(view);

            actionViews.put(item.id, view);

            view.setOnClickListener(this);

            item.vaShow = createVerticalShowAnim(item, index);
            item.vaHide = createVerticalHideAnim(item);

            index++;
        }
    }

    private ActionView createVerticalView(Context context, ActionItem item) {
        ActionView view = new ActionView(context, ITEM_WIDTH);
        view.setTag(item);
        view.iv.setImageResource(item.resId);
        view.tv.setText(item.title);
        return view;
    }

    @Override
    public void onClick(View arg0) {
        ActionItem item = (ActionItem) arg0.getTag();
        if (this.itemListener != null) {
            itemListener.onItemClick(arg0, item);
        }
    }

    public void show() {
        container.setAlpha(0.0f);
        container.setVisibility(View.VISIBLE);
        vaContainerShow.start();
        for (ActionItem ai : items) {
            ai.vaShow.start();
        }
    }

    public void hide() {
        for (ActionItem ai : items) {
            ai.vaHide.start();
        }
        vaContainerHide.start();
    }

    //创建竖向排版的显示动画
    private ValueAnimator createVerticalShowAnim(final ActionItem item, int index) {

        final ActionView view = actionViews.get(item.id);
        final ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
        va.setDuration(200);
        va.setStartDelay((200 - 100) * index);
//        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                view.setAlpha(value);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        return va;
    }

    //创建竖向排版的显示动画
    private ValueAnimator createVerticalHideAnim(final ActionItem item) {

        final ActionView view = actionViews.get(item.id);
        final ValueAnimator va = ValueAnimator.ofFloat(1f, 0f);
        va.setDuration(DURATION);
        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                view.setAlpha(value);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        return va;
    }


    //region 横板动画
    //创建横向排版的显示动画
    private ValueAnimator createHorizontalShowAnim(final ActionItem item) {

        final ActionView view = actionViews.get(item.id);
        final ValueAnimator va = ValueAnimator.ofInt(BOT_MARGIN,
                item.marginBottom);
        va.setDuration(DURATION);
        long delay = random.nextInt(MAX_DELAY);
        va.setStartDelay(delay);
        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                param.bottomMargin = value;
                view.requestLayout();
            }
        });
        return va;
    }

    //创建横向排版的隐藏动画
    private ValueAnimator createHorizontalHideAnim(final ActionItem item) {

        final ActionView view = actionViews.get(item.id);
        final ValueAnimator va = ValueAnimator.ofInt(item.marginBottom,
                BOT_MARGIN * (item.rowIndex + 1));
        va.setDuration(DURATION - 100);
        long delay = random.nextInt(MAX_DELAY) + (1 - item.rowIndex) * 70;
        va.setStartDelay(delay);
        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                param.bottomMargin = value;
                view.requestLayout();
            }
        });
        return va;
    }
    //endregion

    //region 基本数据定义
    public static class ActionItem {

        public int id;
        public int resId;
        public String title;

        public ActionItem(int id, int resId, String title) {
            this.id = id;
            this.resId = resId;
            this.title = title;
        }

        //横向排版需要的属性
        private int marginBottom;
        private ValueAnimator vaShow, vaHide;
        private int colIndex, rowIndex;

    }

    public interface OnActionItemClickListener {
        void onItemClick(View view, ActionItem item);
    }

    static class ActionView extends ShrinkRelativeLayout {

        public ImageView iv;
        public TextView tv;

        public ActionView(Context context, int imgSize) {
            super(context);

            iv = new ImageView(context);
            iv.setId(R.id.arg1);
            this.addView(iv, imgSize, imgSize);

            tv = new TextView(context);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            LayoutParams param = new LayoutParams(imgSize,
                    DensityUtil.dip2px(20));
            param.topMargin = DensityUtil.dip2px(10);

            param.addRule(BELOW, R.id.arg1);
            param.addRule(CENTER_HORIZONTAL);
            this.addView(tv, param);
        }

    }
    //endregion

}
