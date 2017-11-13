package com.zhisland.lib.view.pulltorefresh.absview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.pulltorefresh.PullEvent;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;

public class PullToRefreshAbsListViewProxy<D, V extends AbsListView> extends
		PullToRefreshProxy<V> {

	protected BaseListAdapter<D> adapter;

	private String maxId;
	public boolean isLastPage = true;
	private List<D> newCacheData = null;

	public PullToRefreshAbsListViewProxy() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		if (pullCache != null) {
			Object cacheData = pullCache.getCache();
			if (cacheData instanceof ArrayList<?>) {
				ArrayList<D> data = (ArrayList<D>) cacheData;
				adapter.add(data);
			}
		}
		adapter.setAbsView(pullView.getRefreshableView());
		pullView.getRefreshableView().setAdapter(adapter);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saveCacheData();
	}

	public void saveCacheData() {
		if (newCacheData != null && newCacheData.size() > 0
				&& pullCache != null) {
			pullCache.cacheData((Serializable) newCacheData);
		}
	}

	public void onLoadFailed(Throwable failture) {
		if(maxId == null){
			isLastPage = true;
		}
		
		if (isLastPage) {
			this.pullView.setMode(Mode.PULL_FROM_START);
		} else {
			this.pullView.setMode(Mode.BOTH);
		}
		
		this.adapter.notifyDataSetChanged();
		this.onRefreshFinished();
	}

	/**
	 * 由于没有分页信息，会先清楚所有的数据，再将list中的数据增加到adapter中
	 */
	public void onLoadSucessfully(List<D> data) {

		switch (currentEvent) {
		case normal:
			this.adapter.clearItems();
			newCacheData = data;
			break;
		default:
			break;
		}
		if (data != null) {
			this.adapter.add(data);
		}
		this.onRefreshFinished();

	}

	/**
	 * 根据当前的刷新状态，如果是获取更多，直接将数据append，否则将清掉所有数据后，再将数据加入adapter
	 */
	public void onLoadSucessfully(ZHPageData<D> dataList) {
		if (dataList != null) {
			this.onLoadSucessfully(dataList, dataList.data);
		} else {
			this.onLoadSucessfully(dataList, null);
		}
	}

	/**
	 * 具体的将数据插入到adapter
	 * 
	 * @param dataList
	 * @param data
	 */
	protected void onLoadSucessfully(ZHPageData<D> dataList, ArrayList<D> data) {

		if (dataList == null) {
			isLastPage = false;
		} else {

			if (data == null || data.size() == 0) {
			}

			String curMaxId = dataList.nextId;
			PullEvent currentEvent = this.getCurrentEvent();

			switch (currentEvent) {
			case normal:
			case none:
				this.maxId = curMaxId;

				this.adapter.clearItems(); // clear cached datas
				this.adapter.add(data);
				isLastPage = dataList.page_is_last;

				newCacheData = data;

				break;
			case more:
				this.maxId = curMaxId;
				this.adapter.add(data);
				isLastPage = dataList.page_is_last;
				break;
			default:
				break;
			}
		}
		this.onRefreshFinished();

		if (isLastPage) {
			this.pullView.setMode(Mode.PULL_FROM_START);
		} else {
			this.pullView.setMode(Mode.BOTH);
		}

	}

	/**
	 * 获取当前abslistview的adapter
	 * 
	 * @return
	 */
	public BaseListAdapter<D> getAdapter() {
		return adapter;
	}

	/**
	 * 重载上拉刷新的逻辑
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<V> refreshView) {
		if (this.isRefreshing() == false) {
			this.currentEvent = PullEvent.more;
			pullListener.loadMore(maxId);
		}
	}

	/**
	 * 设置adater
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseListAdapter<D> adapter) {
		this.adapter = adapter;
	}

	@Override
	public void setPullView(PullToRefreshBase<? extends V> view) {
		super.setPullView(view);
		PullToRefreshAdapterViewBase<? extends V> absView = (PullToRefreshAdapterViewBase<? extends V>) view;
		absView.setOnScrollListener(new OnScrollListener() {

			int scrollState;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				this.scrollState = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 如果滚动的位置已经超过倒数第二条，并且不是最后一页，以及没有正在刷新，则自动加载后面更多
				
				if (isLastPage || scrollState == 0)
					return;
				if (isRefreshing())
					return;
				MLog.d("zhscroll", firstVisibleItem + " " + visibleItemCount
						+ " " + totalItemCount);
				if (totalItemCount - (firstVisibleItem + visibleItemCount) < 2) {
					MLog.d("zhscroll", "自动下拉刷新");
					currentEvent = PullEvent.more;
					pullListener.loadMore(maxId);
				}

			}
		});
	}

	@Override
	public PullToRefreshAdapterViewBase<? extends V> getPullView() {
		return (PullToRefreshAdapterViewBase<? extends V>) super.getPullView();
	}
}
