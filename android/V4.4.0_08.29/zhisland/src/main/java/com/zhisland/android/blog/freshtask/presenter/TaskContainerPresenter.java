package com.zhisland.android.blog.freshtask.presenter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.model.impl.TaskContainerModel;
import com.zhisland.android.blog.freshtask.view.ITaskContainerView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class TaskContainerPresenter extends BasePresenter<TaskContainerModel, ITaskContainerView> {

    private TaskCardList allTasks;
    private int current = 0;
    /**
     * 已看过的卡片 type值
     */
    private List<Integer> visited = new ArrayList<>(5);

    public TaskContainerPresenter() {
        BusFreshTask.Bus().register(this);
    }

    @Override
    public void unbindView() {
        BusFreshTask.Bus().unregister(this);
        super.unbindView();
    }


    /**
     * bus 事件
     */
    public void onEventMainThread(EventFreshTask eft) {
        if (allTasks == null)
            return;

        int taskType = eft.taskType;
        switch (taskType) {
            case EventFreshTask.TYPE_SWITCH_TO_NEXT_CARD:
                nextClicked();
                break;
            case EventFreshTask.TYPE_FRESH_TASK_LIST:
                TaskCardList cardList = (TaskCardList) eft.obj;
                if (cardList != null) {
                    setAllTasks(cardList);
                    switchTo(0);
                }
                break;
            case TaskType.CONTACT:
            case TaskType.FIGURE:
            case TaskType.INTRODUCTION:
            case TaskType.RESOURCE:
            case TaskType.PRICE:
                for (TaskCard tc : allTasks.cards) {
                    if (tc.type == taskType) {
                        tc.state.setState(eft.status);
                        break;
                    }
                }
                if (setupDone()) {
                    view().refreshCurrentItem();
                }
                break;
        }
    }

    /**
     * 设置任务数据
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
     * 设置当前位置
     */
    public void setCurrentPosition(int currentPosition) {
        this.current = currentPosition;
    }

    @Override
    protected void updateView() {
        super.updateView();
        if (this.allTasks != null) {
            view().setTask(this.allTasks.cards);
            switchTo(current);

            view().updateListTitle(allTasks.title);
            view().updateListDesc(allTasks.desc);
        }
    }

    /**
     * 更新view到第几步
     */
    public void switchTo(int index) {
        if (index >= allTasks.cards.size()) {
            // 不应该出现这种情况
            return;
        }
        this.current = index;
        TaskCard task = allTasks.cards.get(index);
        if (!visited.contains(task.type)) {
            visited.add(task.type);
        }
        String nextTxt = (index == (allTasks.cards.size() - 1)) ? "完成" : "下一关";
        view().updateNext(nextTxt);
        view().updateStep("规定动作 " + (index + 1));
        view().updateSummary(task.title);
        view().updateHolder(index);
    }

    /**
     * 下一步点击
     */
    public void nextClicked() {
        if (current == allTasks.cards.size() - 1) {
            quitAndSaveData();
        } else {
            switchTo(current + 1);
        }
    }

    public void closeClicked() {
        quitAndSaveData();
    }

    private void quitAndSaveData() {
        if (visited.size() > 0) {
            String data = "";
            for (int i = 0; i < visited.size(); i++) {
                data += visited.get(i);
                if (i != visited.size() - 1) {
                    data += ",";
                }
            }
            model().uploadVisit(data)
                    .observeOn(getSchedulerObserver())
                    .subscribeOn(getSchedulerSubscribe())
                    .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Void aLong) {
                        }
                    });
        }
        view().finish();
    }

    /**
     * 任务项点击
     */
    public void onCardItemClicked(TaskCard taskCard) {
        switch (taskCard.type) {
            case TaskType.CONTACT: {
                if (PrefUtil.Instance().getShowTaskContact()) {
                    view().gotoContact();
                } else {
                    view().showContactTask();
                }
                break;
            }
            case TaskType.FIGURE: {
                if (PrefUtil.Instance().getGuideFigure(PrefUtil.Instance().getUserId())) {
                    view().gotoFigure();
                } else {
                    view().showGuideFigure();
                }
                break;
            }
            case TaskType.INTRODUCTION: {
                view().gotoIntroduction();
                break;
            }
            case TaskType.RESOURCE: {
                view().gotoResource();
                break;
            }
            case TaskType.PRICE: {
                view().gotoPrice();
                break;
            }
            case TaskType.UPGRADE_HAIKE: {
                view().gotToUpdateHaiKe();
                break;
            }

        }
    }

    /**
     * 上传形象照
     */
    public void loadFigure(String localUrl) {
        view().showProgressDlg("正在上传您的图片...");
        AvatarUploader.instance().uploadAvatar(localUrl, new AvatarUploader.OnUploaderCallback() {
            @Override
            public void callBack(String backUrl) {
                if (StringUtil.isNullOrEmpty(backUrl)) {
                    view().hideProgressDlg();
                    view().showToast("上传图片失败");

                    view().refreshCurrentItem();
                } else {
                    uploadFigure(backUrl);
                }
            }
        });
    }

    /**
     * 取消上传形象照
     */
    public void cancelUpload() {
        AvatarUploader.instance().loadFinish();
        view().refreshCurrentItem();
    }

    /**
     * 修改User 形象照（老接口）
     */
    private void uploadFigure(final String figure) {
        model().updateUserFigure(view().getViewContext(), figure,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        view().hideProgressDlg();
                        final User tmpUser = new User();
                        tmpUser.uid = PrefUtil.Instance().getUserId();
                        tmpUser.figure = figure;
                        model().cacheUser(tmpUser);
                        for (TaskCard tc : allTasks.cards) {
                            if (tc.type == TaskType.FIGURE) {
                                tc.figureUrl = figure;
                                break;
                            }
                        }
                        BusFreshTask.Bus().post(new EventFreshTask(TaskType.FIGURE, TaskStatus.FINISHED));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        view().hideProgressDlg();
                        view().showToast("修改形象照失败");
                        view().refreshCurrentItem();
                    }
                });
    }

    /**
     * 点击形象照引导，知道了按钮
     */
    public void clickGuideFigure() {
        PrefUtil.Instance().setGuideFigure(PrefUtil.Instance().getUserId());
        view().gotoFigure();
    }

    private void refreshTaskIconRes(TaskCard task) {
        switch (task.type) {
            case TaskType.CONTACT:
                task.iconCardRes = R.drawable.task_img_logo_contact_large;
                break;
            case TaskType.FIGURE:
                // 添加形象照 type
                User selfUser = model().getSelf();
                if (selfUser.sex != null && selfUser.sex == User.VALUE_SEX_WOMAN) {
                    task.iconCardRes = R.drawable.task_img_female_figure;
                } else {
                    task.iconCardRes = R.drawable.task_img_male_figure;
                }
                task.figureUrl = selfUser.figure;
                break;
            case TaskType.INTRODUCTION:
                task.iconCardRes = R.drawable.task_img_logo_introduction_large;
                break;
            case TaskType.RESOURCE:
                task.iconCardRes = R.drawable.task_img_logo_resource_large;
                break;
            case TaskType.PRICE:
                task.iconCardRes = R.drawable.task_img_logo_price_large;
                break;
            case TaskType.UPGRADE_HAIKE:
                task.iconCardRes = R.drawable.task_img_logo_haike_large;
                break;
        }
    }

}
