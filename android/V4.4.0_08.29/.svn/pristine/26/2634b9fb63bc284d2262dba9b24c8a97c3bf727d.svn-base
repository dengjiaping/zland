package com.zhisland.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhisland.lib.util.MLog;

public class PinnedListView extends ListView implements OnScrollListener {

	public PinnedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContextMenuInfo getContextMenuInfo() {
		return super.getContextMenuInfo();
	}

	public View mHeaderView;
	public int mHeaderViewHeight;

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		if (mHeaderView != null) {
			this.setFadingEdgeLength(0);
			this.setOnScrollListener((OnScrollListener) this);
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (mHeaderView != null) {
			configHeader();
		}
	}

	private void configHeader() {
		MLog.d("pin", "config");
		int itemPos = this.pointToPosition(10, this.getScrollY()
				+ mHeaderViewHeight - 1);
		if (itemPos == AdapterView.INVALID_POSITION) {
			itemPos = this.pointToPosition(10, this.getScrollY()
					+ mHeaderViewHeight + 5);
		}
		MLog.d("pin", String.format("pos:%d, ", itemPos));
		if (itemPos != AdapterView.INVALID_POSITION && itemPos >= 1) {
			mHeaderView.setVisibility(View.VISIBLE);
		} else {
			mHeaderView.setVisibility(View.INVISIBLE);
		}

	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mHeaderView != null) {
			configHeader();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mHeaderView != null) {
			configHeader();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
