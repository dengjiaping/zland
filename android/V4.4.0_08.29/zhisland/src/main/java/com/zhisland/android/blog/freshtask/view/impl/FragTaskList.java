package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.impl.TaskListModel;
import com.zhisland.android.blog.freshtask.presenter.TaskListPresenter;
import com.zhisland.android.blog.freshtask.view.ITaskListView;
import com.zhisland.android.blog.freshtask.view.impl.holder.AddResourceHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.TaskListHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.TaskListItemHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.frag.FragPullList;
import com.zhisland.lib.mvp.presenter.PresenterManager;
import com.zhisland.lib.util.DensityUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 任务列表
 * Created by 向飞 on 2016/5/31.
 */
public class FragTaskList extends FragPullList<TaskCard> implements ITaskListView {

    private static final String INK_TASK = "ink_task";

    private TaskListHolder viewHolder;
    private TaskListAdapter adapter;
    private TaskListPresenter presenter;

    //===================static methods =============
    public static void invoke(Context context, TaskCardList taskCardList) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragTaskList.class;
        param.enableBack = false;
        param.swipeBackEnable = false;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INK_TASK, (Serializable) taskCardList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    //=============static end============

    //============lifecycle start=======================

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new TaskListAdapter();
        getPullProxy().setAdapter(adapter);

        presenter = new TaskListPresenter();
        TaskListModel model = new TaskListModel();
        presenter.setModel(model);
        Object obj = getActivity().getIntent().getSerializableExtra(INK_TASK);
        if (obj instanceof TaskCardList) {
            TaskCardList items = (TaskCardList) obj;
            presenter.setAllTasks(items);
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setSchedulerSubscribe(Schedulers.io());
        presenter.setSchedulerObserver(AndroidSchedulers.mainThread());
        presenter.setSchedulerMain(AndroidSchedulers.mainThread());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.DISABLED);
        getPullProxy().getPullView().getRefreshableView().setDividerHeight(DensityUtil.dip2px(10));
        getPullProxy().getPullView().getRefreshableView().setDivider(new ColorDrawable(Color.TRANSPARENT));
        getPullProxy().getPullView().getRefreshableView().setPadding(DensityUtil.dip2px(10), 0, DensityUtil.dip2px(10), 0);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        //注入
        presenter.bindView(this);
    }

    @Override
    @CallSuper
    public void onStop() {
        presenter.unbindView();
        super.onStop();
    }


    //==============lifecycle end======================

    //===========override methods start===========
    @Override
    protected View createDefaultFragView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.frag_task_list, null);
        viewHolder = new TaskListHolder();
        ButterKnife.inject(viewHolder, root);
        ButterKnife.inject(this, root);//事件监听注入
        return root;
    }
    //==========override methods end============

    //===============ui event start===============

    @OnClick(R.id.ivTaskListClose)
    public void onCloseClicked() {
        presenter.closeClicked();
    }

    //单条点击
    public void itemClicked(TaskCard item) {
        presenter.onCardItemClicked(item);
    }
    //===============ui event end================

    //================ui interfacec start============
    @Override
    public void setTask(List<TaskCard> task) {
        adapter.setData(task);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void finish() {
        getActivity().finish();
    }


    @Override
    public void notifyDataChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateListTitle(String title) {
        viewHolder.tvTitle.setText(title);
    }

    @Override
    public void updateListDesc(String desc) {
        viewHolder.tvDesc.setText(desc);
    }

    @Override
    public void gotoTaskCard(TaskCardList allTasks, int index) {
        FragTaskContainer.invoke(getActivity(), allTasks, index);
        getActivity().finish();
    }

    //============ui interface end==================

    private class TaskListAdapter extends BaseListAdapter<TaskCard> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_fresh_task_list, null);
                TaskListItemHolder holder = new TaskListItemHolder();
                ButterKnife.inject(holder, convertView);
                convertView.setTag(holder);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskCard task = (TaskCard) v.getTag(R.id.arg1);
                        itemClicked(task);
                    }
                });
            }

            TaskListItemHolder holder = (TaskListItemHolder) convertView.getTag();
            TaskCard taskCard = getItem(position);
            holder.fill(taskCard);
            convertView.setTag(R.id.arg1, taskCard);
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

    @Override
    public String getPageName() {
        return "RookieTaskList";
    }
}
