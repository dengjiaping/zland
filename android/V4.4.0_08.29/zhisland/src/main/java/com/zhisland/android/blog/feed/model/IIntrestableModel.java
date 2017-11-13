package com.zhisland.android.blog.feed.model;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.RecommendUser;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.List;

import rx.Observable;

/**
 * 可能感兴趣的人model
 * Created by arthur on 2016/8/31.
 */
public interface IIntrestableModel extends IMvpModel {

    //忽略一个推荐人
    Observable<Void> IgnoreRecommend(User user);

    //关注某个用户
    Observable<Void> follow(User user, String source);

    Observable<Void> unfollow(User user);

    //获取可能感兴趣的人
    Observable<List<RecommendUser>> getIntrestPeople();
}
