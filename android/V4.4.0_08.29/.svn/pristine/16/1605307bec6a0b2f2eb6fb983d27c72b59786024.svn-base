package com.zhisland.lib.component.frag;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.zhisland.lib.R;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;
import com.zhisland.lib.view.pulltorefresh.absview.PullToRefreshAbsListViewProxy;

public class FragPullGrid<D> extends FragBasePull<GridView> {

	public static final int COUNT = 20;

	// ========life cycle event==========

	@Override
	public void loadNormal() {
	}

	@Override
	public void loadMore(String nextId) {

	}

	/**
	 * 滚动到listview的最上面并下拉刷新
	 * 
	 * @param refresh
	 */
	public void scrollToTop(boolean refresh) {
		GridView lv = (GridView) this.getPullProxy().getPullView()
				.getRefreshableView();
		lv.setSelection(0);
		if (refresh) {
			getPullProxy().pullDownToRefresh(true);
		}
	}

	@Override
	protected View createDefaultFragView() {
		return LayoutInflater.from(getActivity()).inflate(
				R.layout.pull_to_refresh_grid, null);
	}

	@Override
	protected PullToRefreshProxy<GridView> createProxy() {
		return new PullToRefreshAbsListViewProxy<D, GridView>();
	}

	@Override
	public String getPageName() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PullToRefreshAbsListViewProxy<D, GridView> getPullProxy() {
		return (PullToRefreshAbsListViewProxy<D, GridView>) super
				.getPullProxy();
	}
}
