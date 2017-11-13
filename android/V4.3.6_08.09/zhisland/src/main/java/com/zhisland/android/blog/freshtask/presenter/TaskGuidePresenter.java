package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.impl.TaskGuideModel;
import com.zhisland.android.blog.freshtask.view.ITaskGuideView;
import com.zhisland.lib.mvp.presenter.BasePresenter;

/**
 * 新手任务引导
 * Created by 向飞 on 2016/5/30.
 */
public class TaskGuidePresenter extends BasePresenter<TaskGuideModel, ITaskGuideView> {

    private TaskCardList taskCardList;//会员任务

    /**
     * 设置会员等级
     */
    public void setTask(TaskCardList task) {
        this.taskCardList = task;
        if (setupDone()) {
            updateView();
        }
    }

    public void onStartClicked() {
        view().gotoTask(taskCardList);
    }

    public void onSkipClicked() {
        view().finishSelf();
    }

    @Override
    protected void updateView() {
        super.updateView();
        view().setTitle(taskCardList.guideTitle);
        view().setRankIcon(rankToIcon());
    }

    /**
     * 身份标识
     *
     * @return
     */
    private int rankToIcon() {
        User user = model().getSelf();
        Integer userType = user.userType;
        Integer isLifelong = user.isLifelong;
        Integer zhislandType = user.zhislandType;
        if (userType != null && userType == User.VALUE_TYPE_VIP) {
            if (isLifelong != null && isLifelong == User.VALUE_TYPE_VIP_LIFE) {
                return R.drawable.icon_task_goldlion;
            } else {
                if (zhislandType != null
                        && zhislandType == User.VALUE_TYPE_VIP_GREEN) {
                    return R.drawable.icon_task_greenlion;
                } else if (zhislandType != null
                        && zhislandType == User.VALUE_TYPE_VIP_BLUE) {
                    return R.drawable.icon_task_eagle;
                }
            }
        } else if (userType != null && userType == User.VALUE_TYPE_DING) {
            return R.drawable.icon_task_bee;
        } else if (userType != null && userType == User.VALUE_TYPE_HAIKE) {
            return R.drawable.icon_task_dolphin;
        }
        return -1;
    }


}
