package com.zhisland.lib.mvp.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zhisland.lib.R;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;
import com.zhisland.lib.view.pulltorefresh.absview.PullToRefreshListViewProxy;

import java.util.List;

/**
 * Created by Mr.Tui on 2016/6/30.
 */
public abstract class FragPullListMvps<D> extends FragBasePullMvps<ListView> {

    public static final int COUNT = 20;

    // ========life cycle event==========

    @Override
    public void loadNormal() {
    }

    @Override
    public void loadMore(String nextId) {

    }

    public void onLoadSucessfully(List<D> data) {
        getPullProxy().onLoadSucessfully(data);
    }

    public void onLoadSucessfully(ZHPageData dataList) {
        getPullProxy().onLoadSucessfully(dataList);
    }

    public void onLoadFailed(Throwable failture) {
        getPullProxy().onLoadFailed(failture);
    }

    /**
     * 滚动到listview的最上面并下拉刷新
     *
     * @param refresh
     */
    public void scrollToTop(boolean refresh) {
        ListView lv = (ListView) this.getPullProxy().getPullView()
                .getRefreshableView();
        lv.setSelection(0);
        if (refresh) {
            getPullProxy().pullDownToRefresh(true);
        }
    }

    @Override
    protected View createDefaultFragView() {
        return LayoutInflater.from(getActivity()).inflate(
                R.layout.pull_to_refresh_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPullProxy().getPullView().getRefreshableView().setDividerHeight(0);
        internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected PullToRefreshProxy<ListView> createProxy() {
        return new PullToRefreshListViewProxy<D>();
    }

    @Override
    protected String getPageName() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PullToRefreshListViewProxy<D> getPullProxy() {
        return (PullToRefreshListViewProxy<D>) super.getPullProxy();
    }
}
