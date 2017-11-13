package com.zhisland.lib.mvp.view;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.view.pulltorefresh.PullEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 下拉刷新lisytview的通用接口
 * Created by arthur on 2016/8/9.
 */
public interface IMvpListView<D> extends IMvpView {

    /**
     * 下拉加载数据
     */
    void loadNormal();

    /**
     * 上拉加载数据
     *
     * @param nextId
     */
    void loadMore(String nextId);

    /**
     * 成功加载数据
     *
     * @param data
     */
    void onLoadSucessfully(List<D> data);

    /**
     * 根据当前的刷新状态，如果是获取更多，直接将数据append，否则将清掉所有数据后，再将数据加入adapter
     */
    void onLoadSucessfully(ZHPageData<D> dataList);

    /**
     * 加载失败
     * @param failture
     */
    void onLoadFailed(Throwable failture);

    /**
     * 获取列表中的数据
     * @return
     */
    List<D> getDatas();

}
