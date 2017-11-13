package com.zhisland.lib.view.pulltorefresh.absview;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.pulltorefresh.PullEvent;

public class PullToRefreshListViewProxy<D> extends
		PullToRefreshAbsListViewProxy<D, ListView> {
	
	private static final int scrollBy = 150;
	private final Runnable showMoreDataRunnable = new Runnable() {

		@Override
		public void run() {
			scrollToShowMoreData();
		}
	};

	public PullToRefreshListViewProxy() {
		super();
	}

	@Override
	public void onLoadSucessfully(List<D> data) {
		PullEvent event = this.currentEvent;
		super.onLoadSucessfully(data);

		if (data == null || data.size() < 1) {
			return;
		}

		switch (event) {
		case more:
			handler.postDelayed(showMoreDataRunnable,
					PullToRefreshBase.SMOOTH_SCROLL_DURATION_MS + 100);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onLoadSucessfully(ZHPageData<D> dataList, ArrayList<D> data) {
		PullEvent event = this.currentEvent;
		super.onLoadSucessfully(dataList, data);

		if (data == null || data.size() < 1) {
			return;
		}

		switch (event) {
		case more:
			handler.postDelayed(showMoreDataRunnable,
					PullToRefreshBase.SMOOTH_SCROLL_DURATION_MS + 100);
			break;
		default:
			break;
		}
	}

	/**
	 * 加载数据完成后，采用动画平滑的滑动list，使得先露出刚刚加载的数据
	 */
	private void scrollToShowMoreData() {

		final ListView actualListView = this.pullView.getRefreshableView();
		int curPos = actualListView.getLastVisiblePosition();
		int total = adapter.getCount();
		if (total > curPos) {
			final int index = actualListView.getFirstVisiblePosition();
			if (index == 0) {
				return;
			}
			if (index > 0) {
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
		}
	}
	
	@Override
	public void setPullView(PullToRefreshBase<? extends ListView> pullView) {
		super.setPullView(pullView);
		setHeaderAndFooter(pullView.getContext(), pullView.getRefreshableView());
	}
}
