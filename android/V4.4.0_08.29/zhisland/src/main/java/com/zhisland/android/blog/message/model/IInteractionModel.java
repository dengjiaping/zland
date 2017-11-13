package com.zhisland.android.blog.message.model;

import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * 可能感兴趣的人model
 * Created by arthur on 2016/8/31.
 */
public interface IInteractionModel extends IMvpModel {

    //获取新鲜事列表
    Observable<ZHPageData<Message>> getInteractionMessageList(String nextId,int count);


}
