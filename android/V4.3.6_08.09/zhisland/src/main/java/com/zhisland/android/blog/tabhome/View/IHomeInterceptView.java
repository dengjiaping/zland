package com.zhisland.android.blog.tabhome.View;

import com.zhisland.android.blog.aa.dto.SplashData;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 首页拦截view
 * Created by 向飞 on 2016/5/28.
 */
public interface IHomeInterceptView extends IMvpView {

    /**
     * 展示新手任务引导
     *
     * @param taskCardList
     */
    void showTaskGuide(TaskCardList taskCardList);

    /**
     * 展示新手任务
     *
     * @param taskCardList
     */
    void showTaskCards(TaskCardList taskCardList, int position);

    /**
     * 展示新手任务列表
     *
     * @param taskCardList
     */
    void showTaskList(TaskCardList taskCardList);

    /**
     * 显示推荐的神评论
     *
     * @param data
     */
    void showRecommendComment(List<UserComment> data);

    /**
     * 显示titlebar的标
     */
    void showTaskTitleIcon(boolean showFreshTask, boolean showRedDot);

    /**
     * 展示splashActivity
     *
     * @param splashData
     */
    void showSplash(SplashData splashData);

    /**
     * 从接口获取splashData
     */
    void getSplashData();
}
