package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.feed.bean.RecommendData;
import com.zhisland.android.blog.feed.bean.RecommendUser;

import java.util.List;

/**
 * 首页商圈相关的view
 * <p>
 * Created by arthur on 2016/8/31.
 */
public interface IFeedListView extends IMiniFeedListView {

    //设置推广位的数据
    void setRecommendData(RecommendData recommendData);

    //设置感兴趣的人数据
    void setIntrestableUsers(List<RecommendUser> intrestableUsers);

    //展示发布按钮
    void showPublishView();

    //展示刷新后的toast
    void showFeedToast(String toastText);

    void hideFeedToast();

    //显示无权限对话框
    void showPermissionsDialog();
}
