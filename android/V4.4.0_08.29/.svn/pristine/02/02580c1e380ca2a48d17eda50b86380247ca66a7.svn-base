package com.zhisland.android.blog.freshtask.view;

import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * Created by 向飞 on 2016/5/27.
 */
public interface ITaskListView extends IMvpView {

    /**
     * 更新任务数据
     *
     * @param task
     */
    void setTask(List<TaskCard> task);


    /**
     * 关闭当前界面
     */
    void finish();


    /**
     * 通知列表数据变化
     */
    void notifyDataChanged();


    /**
     * 任务列表的标题
     *
     * @param title
     */
    void updateListTitle(String title);

    /**
     * 任务列表的描述
     *
     * @param desc
     */
    void updateListDesc(String desc);

    /**
     * 去任务卡片页面
     *
     * @param allTasks
     * @param index
     */
    void gotoTaskCard(TaskCardList allTasks, int index);
}
