package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.TaskListModel;
import com.zhisland.android.blog.freshtask.view.ITaskListView;
import com.zhisland.lib.mvp.presenter.BasePresenter;

/**
 * Created by 向飞 on 2016/5/26.
 */
public class TaskListPresenter extends BasePresenter<TaskListModel, ITaskListView> {

    private TaskCardList allTasks;

    public TaskListPresenter() {
        BusFreshTask.Bus().register(this);
    }

    @Override
    public void unbindView() {
        BusFreshTask.Bus().unregister(this);
        super.unbindView();
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (this.allTasks != null) {
            view().setTask(this.allTasks.cards);

            view().updateListTitle(allTasks.title);
            view().updateListDesc(allTasks.desc);
        }
    }

    /**
     * bus 事件
     *
     * @param eft
     */
    public void onEventMainThread(EventFreshTask eft) {
        if (allTasks == null)
            return;

        int taskType = eft.taskType;
        switch (taskType) {
            case EventFreshTask.TYPE_FRESH_TASK_LIST:
                TaskCardList cardList = (TaskCardList) eft.obj;
                if (cardList != null) {
                    setAllTasks(cardList);
                }
                break;
        }
    }


    /**
     * 设置任务数据
     *
     * @param allTasks
     */
    public void setAllTasks(TaskCardList allTasks) {
        this.allTasks = allTasks;
        if (allTasks != null && allTasks.cards != null) {
            for (TaskCard card : allTasks.cards) {
                refreshTaskIconRes(card);
            }
        }
        if (setupDone()) {
            view().setTask(this.allTasks.cards);
        }
    }

    /**
     * 关闭按钮点击
     */
    public void closeClicked() {
        view().finish();
    }


    /**
     * 任务项点击
     */
    public void onCardItemClicked(TaskCard taskCard) {
        int index = allTasks.cards.indexOf(taskCard);
        view().gotoTaskCard(allTasks, index);
    }

    private void refreshTaskIconRes(TaskCard task) {
        boolean finished = task.state.getState() == TaskStatus.FINISHED;
        switch (task.type) {
            case TaskType.CONTACT:
                task.iconListRes = finished ? R.drawable.task_img_logo_contact_finish : R.drawable.task_img_logo_contact;
                break;
            case TaskType.FIGURE:
                task.iconListRes = finished ? R.drawable.task_img_logo_cover_finish : R.drawable.task_img_logo_cover;
                break;
            case TaskType.INTRODUCTION:
                task.iconListRes = finished ? R.drawable.task_img_logo_introduction_finish : R.drawable.task_img_logo_introduction;
                break;
            case TaskType.RESOURCE:
                task.iconListRes = finished ? R.drawable.task_img_logo_resource_finish : R.drawable.task_img_logo_resource;
                break;
            case TaskType.PRICE:
                task.iconListRes = finished ? R.drawable.task_img_logo_price_finish : R.drawable.task_img_logo_price;
                break;
             case TaskType.UPGRADE_HAIKE:
                task.iconListRes = finished ? R.drawable.task_img_list_logo_haike_finish : R.drawable.task_img_list_logo_haike;
                break;
        }
    }
}
