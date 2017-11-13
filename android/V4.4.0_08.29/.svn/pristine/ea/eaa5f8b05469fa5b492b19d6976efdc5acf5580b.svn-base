package com.zhisland.android.blog.tabhome.model;

import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.tabhome.bean.FreshTaskAndComment;
import com.zhisland.lib.mvp.model.IMvpModel;


import rx.Observable;

/**
 * Created by 向飞 on 2016/5/28.
 */
public interface IHomeInterceptModel extends IMvpModel {

    /**
     * 检查好友任务状态 及 神评论
     */
    Observable<FreshTaskAndComment> checkFreshTaskAndRecommendComment();

    /**
     * 检查新手任务状态
     *
     * @return
     */
    Observable<TaskCardList> checkFreshTask();

    /**
     * 获取任务拦截的上次显示时间
     *
     * @return
     */
    long getLastTaskShowTime();

    /**
     * 保存任务显示时间
     *
     * @param time
     */
    void saveTaskShowTime(long time);

    /**
     * 获取神评论拦截的上次显示时间
     *
     * @return
     */
    long getLastPrizeShowTime();

    /**
     * 保存神评论显示时间
     *
     * @param time
     */
    void savePrizeShowTime(long time);
}
