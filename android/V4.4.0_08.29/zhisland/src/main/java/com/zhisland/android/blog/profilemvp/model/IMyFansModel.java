package com.zhisland.android.blog.profilemvp.model;

import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 我的粉丝model
 * Created by Mr.Tui on 2016/9/6.
 */
public interface IMyFansModel extends IMvpModel {

    /**
     * 获取我的粉丝列表
     */
    public Observable<ZHPageData<InviteUser>> getFansList(final String nextId);

    //关注某个用户
    Observable<Void> follow(User user, String source);
}
