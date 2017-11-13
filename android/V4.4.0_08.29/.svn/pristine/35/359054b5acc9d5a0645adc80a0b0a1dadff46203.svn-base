package com.zhisland.android.blog.common.view.pullzoom;

import com.zhisland.android.blog.common.view.ScrollTitleBar;

/**
 * 与{@link PullToZoomScrollViewEx} 搭配使用
 * 
 * <code>
 * ScrollViewExTitleListener titleAlphaListener = new ScrollViewExTitleListener();
		titleAlphaListener.setTitledOffset(headerHeight - titleHeight);
		titleAlphaListener.setTitleView(rlTitle);
		rlTitle.setLeftRes(R.drawable.sel_btn_feed_back,
				R.drawable.sel_btn_back);
		rlTitle.setRightRes(R.drawable.sel_btn_feed_more,
				R.drawable.sel_btn_more);
		scrollView.addOnScrollChangedListener(titleAlphaListener);
 * </code>
 * 
 * @author 向飞
 *
 */
public class ScrollViewExTitleListener implements OnScrollChangedListener {

	static final String TAG = "pts";
	private int titleOffset;// 标题变化的offset
	private ScrollTitleBar titleView;

	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (titleView != null) {
			float alpha = (float) t / (float) titleOffset;
			titleView.updateAlpha(alpha);
			// MLog.d(TAG, "" + alpha + " - " + oldt + " - " + t);
		}

	}

	public void setTitledOffset(int pinnedOffset) {
		this.titleOffset = pinnedOffset;
	}

	public void setTitleView(ScrollTitleBar titleView) {
		this.titleView = titleView;
	}

}
