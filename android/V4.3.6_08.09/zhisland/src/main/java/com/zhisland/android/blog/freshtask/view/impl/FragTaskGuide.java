package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.impl.TaskGuideModel;
import com.zhisland.android.blog.freshtask.presenter.TaskGuidePresenter;
import com.zhisland.android.blog.freshtask.view.ITaskGuideView;
import com.zhisland.android.blog.freshtask.view.impl.holder.TaskGuideHolder;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新手任务引导页
 * Created by 向飞 on 2016/5/30.
 */
public class FragTaskGuide extends FragBaseMvp<TaskGuidePresenter> implements ITaskGuideView {

    private static final String INK_TASK = "ink_task";

    /**
     * 唤醒新手任务引导页面
     *
     * @param context
     * @param task
     */
    public static void invoke(Context context, TaskCardList task) {
        CommonFragActivity.CommonFragParams params = new CommonFragActivity.CommonFragParams();
        params.noTitle = true;
        params.clsFrag = FragTaskGuide.class;
        Intent intent = CommonFragActivity.createIntent(context, params);
        intent.putExtra(INK_TASK, task);
        context.startActivity(intent);
    }

    private TaskGuideHolder holder;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_TASK);
        if (obj == null || !(obj instanceof TaskCardList)) {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_task_guide, container, false);
        holder = new TaskGuideHolder();
        ButterKnife.inject(holder, root);
        ButterKnife.inject(this, root);
        return root;
    }


    @Override
    protected String getPageName() {
        return "InterceptRookieLanding";
    }

    //==========UI event==========
    @OnClick(R.id.tvTaskGuideStart)
    public void onStartClick() {
        presenter().onStartClicked();
    }

    @OnClick(R.id.tvTaskGuideSkip)
    public void onSkipClick() {
        presenter().onSkipClicked();
    }

    //===============task guide listener=======
    @Override
    public void gotoTask(TaskCardList cards) {
        getActivity().finish();
        FragTaskContainer.invoke(getActivity(), cards, 0);
    }

    @Override
    public void finishSelf() {
        getActivity().finish();
    }

    @Override
    public void setTitle(String text) {
        holder.tvTitle.setText(text);
    }

    @Override
    public void setRankIcon(int resId) {
        holder.ivIcon.setImageResource(resId);
    }

    @Override
    protected TaskGuidePresenter createPresenter() {
        TaskGuidePresenter presenter = new TaskGuidePresenter();
        TaskGuideModel model = new TaskGuideModel();
        presenter.setModel(model);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_TASK);
        presenter.setTask((TaskCardList) obj);
        return presenter;
    }
}
