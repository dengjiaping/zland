package com.zhisland.android.blog.common.view.pullzoom;

import android.view.View;

/**
 * 与{@link PullToZoomScrollViewEx} 搭配使用
 * 
 * @author 向飞
 *
 */
public class ScrollViewExPinnedListener implements OnScrollChangedListener {

	static final String TAG = "pts";
	private int pinnedOffset;
	private View pinnedView;

	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (pinnedView != null) {
			if (t > oldt && t >= pinnedOffset) {
				// MLog.d(TAG, "hide float view");
				pinnedView.setVisibility(View.VISIBLE);

			} else if (t < oldt && t < pinnedOffset) {
				// MLog.d(TAG, "hide pinned view");
				pinnedView.setVisibility(View.INVISIBLE);
			}
		}

	}

	public void setPinnedOffset(int pinnedOffset) {
		this.pinnedOffset = pinnedOffset;
	}

	public void setPinnedView(View pinnedView) {
		this.pinnedView = pinnedView;
	}

}
