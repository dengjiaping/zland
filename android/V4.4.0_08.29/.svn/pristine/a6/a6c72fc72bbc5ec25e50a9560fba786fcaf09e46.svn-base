package com.zhisland.android.blog.freshtask.view;

import android.content.Context;

import com.zhisland.android.blog.freshtask.bean.TaskCard;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * Created by 向飞 on 2016/5/27.
 */
public interface ITaskContainerView extends IMvpView {

    /**
     * 更新任务数据
     *
     * @param task
     */
    void setTask(List<TaskCard> task);

    /**
     * 更新步骤文案
     *
     * @param text
     */
    void updateStep(String text);

    /**
     * 更新summary
     *
     * @param summary
     */
    void updateSummary(String summary);

    /**
     * 更新下一步
     *
     * @param next
     */
    void updateNext(String next);

    /**
     * 更新 Holder
     */
    void updateHolder(int position);

    /**
     * 完成所有任务
     */
    void finish();

    /**
     * 渠道好友圈任务
     */
    void gotoContact();

    /**
     * 渠道个人形象
     */
    void gotoFigure();

    /**
     * 去到个人简介
     */
    void gotoIntroduction();

    /**
     * 渠道资源发布
     *
     * @param item
     */
    void gotoResource();

    /**
     * 渠道神评论
     */
    void gotoPrice();

    /**
     * 跳转升级为海客
     */
    void gotToUpdateHaiKe();

    /**
     * 设置显示第几步
     *
     * @param current
     */
    void switchTo(int current);

    /**
     * 刷新当前任务
     */
    void refreshCurrentItem();

    /**
     * 显示形象照引导
     */
    void showGuideFigure();

    /**
     * 通知列表数据变化
     */
    void notifyDataChanged();

    /**
     * 显示通讯录弹框
     */
    void showContactTask();

    //==========for task list========

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
     * 获取 Context
     */
    Context getViewContext();
}
