package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.view.pulltorefresh.CardViewPager;

import butterknife.InjectView;

/**
 * 神评精选 holder
 */
public class CommentRecommendHolder {

    @InjectView(R.id.viewClose)
    public View viewClose;

    @InjectView(R.id.viewPager)
    public ViewPager vp;

    @InjectView(R.id.tvNextStep)
    public TextView tvNextStep;

}
