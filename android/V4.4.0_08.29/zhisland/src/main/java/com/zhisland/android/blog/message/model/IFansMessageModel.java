package com.zhisland.android.blog.message.model;

import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * Created by arthur on 2016/9/13.
 */
public interface IFansMessageModel extends IMvpModel {

    Observable<ZHPageData<InviteUser>> loadData(String nextId, int i);

    Observable<Void> follow(long uid, String source);
}
