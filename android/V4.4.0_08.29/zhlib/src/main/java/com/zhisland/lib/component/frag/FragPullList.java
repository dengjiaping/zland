package com.zhisland.lib.component.frag;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zhisland.lib.R;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;
import com.zhisland.lib.view.pulltorefresh.absview.PullToRefreshListViewProxy;

/**
 * 对listview的下来刷新封装。
 * <P>
 * 
 * @author arthur
 *
 * @param <D>
 */
public class FragPullList<D> extends FragBasePull<ListView> {

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
	protected PullToRefreshProxy<ListView> createProxy() {
		return new PullToRefreshListViewProxy<D>();
	}

	@Override
	public String getPageName() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PullToRefreshListViewProxy<D> getPullProxy() {
		return (PullToRefreshListViewProxy<D>) super.getPullProxy();
	}
}
