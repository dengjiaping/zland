package com.zhisland.lib.view.pulltorefresh.sectionlist;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.lib.async.http.Failture;
import com.zhisland.lib.component.adapter.BaseSectionListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.view.pulltorefresh.PullEvent;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;

public class PullToRefreshSectionListProxy<G extends Groupable<C>, C> extends
		PullToRefreshProxy<AnimatedExpandableListView> {

	private static final String TAG = "absproxy";
	private static final int scrollBy = 150;

	protected BaseSectionListAdapter<G, C> adapter;

	private String maxId;
	private boolean isLastPage = true;
	private ArrayList<G> cacheData = null;
	private final Runnable showMoreDataRunnable = new Runnable() {

		@Override
		public void run() {
			scrollToShowMoreData();
		}
	};

	public PullToRefreshSectionListProxy() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {

		if (pullCache != null) {
			Object cacheData = pullCache.getCache();
			if (cacheData instanceof ArrayList<?>) {
				ArrayList<G> data = (ArrayList<G>) cacheData;
				adapter.add(data);
			}
		}
		adapter.setSectionList(this.pullView.getRefreshableView());
		this.pullView.getRefreshableView().setAdapter(adapter);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saveCacheData();
	}

	private void saveCacheData() {
		if (cacheData != null && cacheData.size() > 0 && pullCache != null) {
			pullCache.cacheData(cacheData);
		}
	}

	@Override
	public void onPullUpToRefresh(
			PullToRefreshBase<AnimatedExpandableListView> refreshView) {
		if (this.isRefreshing() == false) {
			this.currentEvent = PullEvent.more;
			pullListener.loadMore(maxId);
		}
	}

	/**
	 * 加载数据失败
	 * 
	 * @param failture
	 */
	public void onLoadFailed(Failture failture) {
		this.onRefreshFinished();
	}

	/**
	 * 清楚所有数据后，将列表中的数据加入adapter
	 * 
	 * @param data
	 */
	public void onLoadSucessfully(ArrayList<G> data) {

		this.onRefreshFinished();

		if (data != null) {
			this.adapter.clear();
			this.adapter.add(data);
			this.adapter.notifyDataSetChanged();

			if (data.size() > 0) {
				cacheData = data;
			}
		}

	}

	/**
	 * 根据分页信息将数据加入listview中
	 * 
	 * @param dataList
	 */
	public void onLoadSucessfully(ZHPageData<G> dataList) {
		if (dataList != null) {
			this.onLoadSucessfully(dataList, dataList.data);
		} else {
			this.onLoadSucessfully(dataList, null);
		}
	}

	/**
	 * 根据具体的分页信息将数据加入adapter中
	 * 
	 * @param dataList
	 * @param data
	 */
	private void onLoadSucessfully(ZHPageData<G> dataList, ArrayList<G> data) {

		if (dataList == null) {
			isLastPage = true;
		} else {

			if (data == null || data.size() == 0) {
			}

			String curMaxId = dataList.nextId;
			PullEvent currentEvent = this.getCurrentEvent();

			switch (currentEvent) {
			case normal:
				this.maxId = curMaxId;
				this.adapter.clear();
				this.adapter.add(data);
				isLastPage = dataList.pageIsLast;
				cacheData = data;
				break;
			case more:
				this.maxId = curMaxId;
				this.adapter.add(data);
				isLastPage = dataList.pageIsLast;
				handler.postDelayed(showMoreDataRunnable,
						PullToRefreshBase.SMOOTH_SCROLL_DURATION_MS + 50);
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
	 * 用动画动态滑动显示刚刚加载的数据
	 */
	private void scrollToShowMoreData() {
		try {
			final AnimatedExpandableListView actualListView = this.pullView
					.getRefreshableView();
			final int index = actualListView.getFirstVisiblePosition();
			if (index >= 0) {
				View v = actualListView.getChildAt(0);
				if (v != null) {

					int bottom = v.getBottom();
					int top = v.getTop();
					if (bottom > scrollBy) {
						ValueAnimator va = ObjectAnimator.ofInt(top, top
								- scrollBy);
						va.setDuration(150);
						va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

							@Override
							public void onAnimationUpdate(
									ValueAnimator animation) {
								int currentDiff = (Integer) animation
										.getAnimatedValue();
								actualListView.setSelectionFromTop(index,
										currentDiff);
							}
						});
						va.start();
					} else {
						ValueAnimator va = ObjectAnimator.ofInt(bottom, bottom
								- scrollBy);
						va.setDuration(150);
						va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

							@Override
							public void onAnimationUpdate(
									ValueAnimator animation) {
								int currentDiff = (Integer) animation
										.getAnimatedValue();
								actualListView.setSelectionFromTop(index + 1,
										currentDiff);
							}
						});
						va.start();
					}
				} else {
					ValueAnimator va = ObjectAnimator.ofInt(0, 30);
					va.setDuration(150);
					va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							int currentDiff = (Integer) animation
									.getAnimatedValue();
							actualListView.scrollBy(0, currentDiff);
						}
					});
					va.start();
				}
			}
		} catch (Exception ex) {
			// when exception happened, just ignore it, since here's code just
			// want to scroll list view
		}
	}

	/**
	 * 获取当前的adapter
	 * 
	 * @return
	 */
	public BaseSectionListAdapter<G, C> getAdapter() {
		return adapter;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	/**
	 * 设置adapter
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseSectionListAdapter<G, C> adapter) {
		this.adapter = adapter;
	}

	@Override
	public void setPullView(
			PullToRefreshBase<? extends AnimatedExpandableListView> pullView) {
		super.setPullView(pullView);
		setHeaderAndFooter(pullView.getContext(), pullView.getRefreshableView());
	}

}
