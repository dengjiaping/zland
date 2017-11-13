package com.zhisland.android.blog.message.model;

import com.zhisland.android.blog.message.bean.Message;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import rx.Observable;

/**
 * Created by arthur on 2016/9/13.
 */
public interface ISystemMessageModel extends IMvpModel {

    /**
     * 加载更多的数据
     *
     * @param nextId
     * @param i
     * @return
     */
    Observable<ZHPageData<Message>> loadData(String nextId, int i);
}
