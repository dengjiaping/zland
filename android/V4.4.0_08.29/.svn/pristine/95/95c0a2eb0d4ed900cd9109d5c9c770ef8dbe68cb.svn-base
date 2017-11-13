package com.zhisland.android.blog.tabhome.presenter;

import com.zhisland.android.blog.aa.dto.SplashData;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.util.TimeUtil;
import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.bean.TaskType;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventTitleClick;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.tabhome.View.IHomeInterceptView;
import com.zhisland.android.blog.tabhome.bean.FreshTaskAndComment;
import com.zhisland.android.blog.tabhome.model.IHomeInterceptModel;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.List;

import rx.Subscriber;

/**
 * 首页拦截主导器
 * Created by 向飞 on 2016/5/28.
 */
public class HomeInterceptPresenter extends BasePresenter<IHomeInterceptModel, IHomeInterceptView> {

    private TaskCardList tasks;

    public HomeInterceptPresenter() {
        BusFreshTask.Bus().register(this);
    }

    @Override
    public void unbindView() {
        BusFreshTask.Bus().unregister(this);
        super.unbindView();
    }

    /**
     * 监听主页titleclicked事件
     */
    @Deprecated
    public void onEventMainThread(EventTitleClick titleClick) {
        view().showTaskList(tasks);
    }

    /**
     * bus 事件
     */
    public void onEventMainThread(EventFreshTask eft) {
        int taskType = eft.taskType;
        switch (taskType) {
            case TaskType.CONTACT:
            case TaskType.FIGURE:
            case TaskType.INTRODUCTION:
            case TaskType.RESOURCE:
            case TaskType.PRICE:
            case TaskType.UPGRADE_HAIKE:
                //修改数据
                for (TaskCard tc : tasks.cards) {
                    if (tc.type == taskType) {
                        tc.state.setState(eft.status);
                        break;
                    }
                }

                /**
                 * 检测是否是所有任务都完成了，如果都完成了则隐藏红点
                 */
                boolean isComplete = true;
                for (TaskCard tc : tasks.cards) {
                    if (tc.state.getState() != TaskStatus.FINISHED) {
                        isComplete = false;
                    }
                }
                if (isComplete) {
                    view().showTaskTitleIcon(true, false);
                }
                break;
            case EventFreshTask.TYPE_SHOW_CARD:
                int position = eft.status;
                view().showTaskCards(tasks, position);
                break;
            case EventFreshTask.TYPE_GET_FRESH_TASK:
                refreshTasks();
                break;
        }
    }

    /**
     * push eventbus
     */
    public void onEventMainThread(EBNotify ebNotify) {
        if (ebNotify.notifyType == NotifyTypeConstants.FRESH_TASK_FINISH) {
            refreshTasks();
        }
    }

    /**
     * 刷新新手任务数据
     */
    private void refreshTasks() {
        model().checkFreshTask()
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<TaskCardList>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<TaskCardList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TaskCardList taskCardList) {
                        if (taskCardList != null) {
                            updateData(taskCardList);
                            //刷新列表或者卡片
                            BusFreshTask.Bus().post(new EventFreshTask(EventFreshTask.TYPE_FRESH_TASK_LIST, taskCardList));
                        }
                    }
                });
    }

    /**
     * 开始检查拦截项
     *
     * @param checkSplash 是否需要检查splash
     */
    public void checkIntercept(boolean checkSplash) {
        if (checkSplash) {
            //检查是否需要弹出splash，如果弹出splash就不再检查其他拦截。
            boolean splashStarted = checkSplash();
            if (splashStarted) {
                return;
            }
        }

        long lastTaskShowTime = model().getLastTaskShowTime();
        boolean shouldShowTask = !TimeUtil.isSameDay(lastTaskShowTime, System.currentTimeMillis());//一天显示一次任务拦截

        long lastPrizeShowTime = model().getLastPrizeShowTime();
        boolean shouldShowPrize = !TimeUtil.isSameDay(lastPrizeShowTime, System.currentTimeMillis());//一天显示一次神评论

        checkTask(shouldShowTask, shouldShowPrize);
    }

    /**
     * 检查是否要启动splash.(如果有未使用过的SplashData)
     *
     * @return 启动返回true，不启动返回false
     */
    private boolean checkSplash() {
        SplashData splashData = SplashData.getValidityUnUsed();
        //后台前换到app时，判断是否有在有效期内，且没有使用过的SplashData
        if (splashData != null) {
            //如果有启动SplashData。
            view().showSplash(splashData);
            return true;
        } else {
            //如果没有getSplashData
            view().getSplashData();
            return false;
        }
    }

    // 检查新手任务 及 神评论
    private void checkTask(final boolean shouldShowTask, final boolean shouldShowPrize) {
        model().checkFreshTaskAndRecommendComment()
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<FreshTaskAndComment>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<FreshTaskAndComment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FreshTaskAndComment freshTaskAndComment) {
                        if (freshTaskAndComment != null) {
                            TaskCardList taskCardList = freshTaskAndComment.cardList;

                            List<UserComment> userComment = freshTaskAndComment.userComment;
                            if (taskCardList != null) {
                                updateData(taskCardList);
                                if (taskCardList.display == TaskCardList.SHOW_DISPLAY_GUIDE) {
                                    // 显示新手任务 引导页
                                    model().saveTaskShowTime(System.currentTimeMillis());
                                    view().showTaskGuide(taskCardList);
                                } else if (taskCardList.popStatus != null && taskCardList.popStatus == taskCardList.NOT_SHOW_POP_STATE) {
                                    // 已完成新手任务，check 神评论
                                    showRecommendComment(shouldShowPrize, userComment);
                                } else {
                                    // 显示新手任务 卡片
                                    if (shouldShowTask) {
                                        model().saveTaskShowTime(System.currentTimeMillis());
                                        view().showTaskCards(taskCardList, 0);
                                    } else {
                                        showRecommendComment(shouldShowPrize, userComment);
                                    }
                                }
                            } else {
                                showRecommendComment(shouldShowPrize, userComment);
                            }
                        }
                    }
                });
    }

    /**
     * 显示神评论推荐
     */
    private void showRecommendComment(boolean shouldShowPrize, List<UserComment> comments) {
        if (shouldShowPrize && comments != null && comments.size() > 0) {
            //保存时间
            model().savePrizeShowTime(System.currentTimeMillis());
            view().showRecommendComment(comments);
        }
    }

    private void updateData(TaskCardList taskCardList) {
        HomeInterceptPresenter.this.tasks = taskCardList;
        Integer unfinishedCount = taskCardList.unfinishedCount;
        view().showTaskTitleIcon(true, unfinishedCount > 0);
    }

}
