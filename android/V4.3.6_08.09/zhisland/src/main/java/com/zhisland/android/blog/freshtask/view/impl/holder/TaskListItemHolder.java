package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;

import butterknife.InjectView;

/**
 * TaskListItem 视图holder
 * Created by 向飞 on 2016/5/31.
 */
public class TaskListItemHolder {

    @InjectView(R.id.ivTaskListItemIcon)
    public ImageView ivIcon;//图标

    @InjectView(R.id.tvTaskListItemTitle)
    public TextView tvTitle;//标题

    @InjectView(R.id.tvTaskListItemDesc)
    public TextView tvDesc;//描述

    @InjectView(R.id.ivTaskListItemComplete)
    public ImageView ivStatus;//状态

    @InjectView(R.id.ivTaskListItemUnvisited)
    public ImageView ivUnvisted;//未读标识

    /**
     * 填充试图
     *
     * @param taskCard
     */
    public void fill(TaskCard taskCard) {
        tvTitle.setText(taskCard.title);
        tvDesc.setText(taskCard.desc);
        if (taskCard.state.getState() == TaskStatus.FINISHED) {
            ivStatus.setVisibility(View.VISIBLE);
            ivUnvisted.setVisibility(View.GONE);
        } else {
            ivStatus.setVisibility(View.GONE);
            ivUnvisted.setVisibility(View.VISIBLE);
        }
        ivIcon.setImageResource(taskCard.iconListRes);
    }
}
