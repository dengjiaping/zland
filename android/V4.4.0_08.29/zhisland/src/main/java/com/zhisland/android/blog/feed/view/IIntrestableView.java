package com.zhisland.android.blog.feed.view;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * 可能感兴趣的人view
 * Created by arthur on 2016/8/31.
 */
public interface IIntrestableView extends IMvpView {

    //更新可能感兴趣的人
    void updateIntrestable(List<RecommendUser> users);

    //跳转到用户的详情
    void gotoUserDetail(User user);

    //移除某个推荐人
    void removeRecommend(RecommendUser user);

    //更新某一条数据
    void updateItem(RecommendUser inviteUser);

    //隐藏可能认识的人的区域
    void hideIntrestView();

    //展示可能认识的人的区域
    void showIntrestView();

    //跳转到查看全部
    void gotoAllIntrest(List<RecommendUser> users);

    //继承IMvpListView与fragfeedlist冲突
    void onLoadFailed(Throwable e);

    //继承IMvpListView与fragfeedlist冲突
    void onLoadSucess(List<RecommendUser> recommendUsers);
}
