package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.view.PageControl;

import butterknife.InjectView;

/**
 * 任务container的View Holder
 * Created by 向飞 on 2016/5/26.
 */
public class TaskContainerHolder {

    @InjectView(R.id.tvStep)
    public TextView tvStep;

    @InjectView(R.id.tvSummary)
    public TextView tvSummary;

    @InjectView(R.id.viewPager)
    public ViewPager vp;

    @InjectView(R.id.pageControl)
    public PageControl pc;

    @InjectView(R.id.tvNextStep)
    public TextView tvNextStep;

    @InjectView(R.id.viewClose)
    public View viewClose;


}
