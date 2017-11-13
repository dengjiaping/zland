package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.RInfoPageData;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.List;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IRecommendInfo extends IMvpView {

    /**
     * 成功加载推荐资讯
     */
    void onloadSuccess(ZHPageData<ZHInfo> data);

    /**
     * 加载推荐资讯失败
     *
     * @param error
     */
    void onLoadFailed(Throwable error);

    /**
     * 下来刷新数据
     */
    void pullDownToRefresh(boolean showRefreshingHeader);

    /**
     * 刷新列表
     */
    void refreshListView();

    /**
     * 获取当前listview的数据
     */
    List<ZHInfo> getCurListData();

    /**
     * 显示提示消息
     */
    void showPrompt(String promptStr);

    /**
     * 隐藏提示消息
     */
    void hidePrompt();

    /**
     * 缓存当前数据
     */
    void cacheListData();
}
