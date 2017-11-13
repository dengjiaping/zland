package com.zhisland.android.blog.profilemvp.model;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 我的关注model
 * Created by Mr.Tui on 2016/9/6.
 */
public interface IMyAttentionModel extends IMvpModel {

    /**
     * 获取我关注的人列表
     */
    public Observable<ZHPageData<InviteUser>> getAttentionList(final String nextId);

}
