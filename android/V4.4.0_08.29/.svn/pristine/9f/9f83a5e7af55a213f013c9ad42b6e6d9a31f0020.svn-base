package com.zhisland.lib.view.pulltorefresh.sectionlist;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.zhisland.lib.component.adapter.BaseSectionListAdapter;
import com.zhisland.lib.util.MLog;

public class SectionWrap extends FrameLayout {

	private ExpandableSectionList es;
	private View mHeaderView;
	private int mHeaderViewWidth;
	private int mHeaderViewHeight;
	// TODO optimize this, should measure it every time
	private SparseArray<Integer> groupHeight = new SparseArray<Integer>();

	public SectionWrap(Context context, AttributeSet attrs) {
		super(context, attrs);
		es = new ExpandableSectionList(context, attrs);
		es.setGroupIndicator(null);
		this.addView(es, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public AnimatedExpandableListView getInterList() {
		return es;
	}

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;

		if (mHeaderView != null) {
			setFadingEdgeLength(0);
			this.addView(mHeaderView, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
	}

	public View getPinnedView() {
		return mHeaderView;
	}

	public int getIndicatorId() {
		return es.indicatorGroupId;

	}

	private class ExpandableSectionList extends AnimatedExpandableListView {

		private static final String TAG = "section";

		private int left = 0;
		private int top = 0;
		private int right = 0;
		private int bottom = 0;
		private boolean headerShow = false;
		private int indicatorGroupId = -1;
		private int indicatorGroupHeight;

		private MotionEvent lastEvent = null;

		public ExpandableSectionList(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		private BaseSectionListAdapter<?, ?> mAdapter;

		@Override
		public void setAdapter(ExpandableListAdapter adapter) {
			super.setAdapter(adapter);
			mAdapter = (BaseSectionListAdapter<?, ?>) adapter;
			if (mAdapter != null) {
				mAdapter.expandAll();
				mAdapter.registerDataSetObserver(new DataSetObserver() {
					public void onChanged() {
						indicatorGroupId = -1;
					}
				});
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if (mHeaderView != null) {
				measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
				mHeaderViewWidth = mHeaderView.getMeasuredWidth();
				mHeaderViewHeight = mHeaderView.getMeasuredHeight();
			}
		}

		protected void configHeader() {
			if (mHeaderView == null)
				return;

			/**
			 * calculate point (0,0)
			 */
			int itemPos = this.pointToPosition(10, 1);

			if (itemPos != AdapterView.INVALID_POSITION) {
				long pos = this.getExpandableListPosition(itemPos);
				int childPos = ExpandableListView.getPackedPositionChild(pos);
				int groupPos = ExpandableListView.getPackedPositionGroup(pos);

				headerShow = (childPos == -1 && groupPos == -1);

				if (childPos == AdapterView.INVALID_POSITION) {
					int offset = headerShow ? 1 : 0;
					View groupView = this.getChildAt(itemPos
							- this.getFirstVisiblePosition() + offset);
					if (groupView != null) {
						indicatorGroupHeight = groupView.getHeight();
					}
				}
				// get an error data, so return now
				if (indicatorGroupHeight == 0) {
					return;
				}

				// update the data of indicator group view
				if (groupPos != indicatorGroupId) {
					if (!headerShow) {

						mAdapter.getGroupView(groupPos,
								this.isGroupExpanded(groupPos), mHeaderView,
								null);
						MLog.d(TAG,
								"refresh group view " + groupPos + " from "
										+ mHeaderViewHeight + " to "
										+ mHeaderView.getMeasuredHeight());

						if (groupHeight.get(groupPos) == null) {
							indicatorGroupHeight = mHeaderView
									.getMeasuredHeight();
							groupHeight.put(groupPos, indicatorGroupHeight);
						} else {
							indicatorGroupHeight = groupHeight.get(groupPos);
						}

					}
					indicatorGroupId = groupPos;
				}
			}

			if (indicatorGroupId == -1) {
				// header was show, so hide the pinned group header
				headerShow = true;
				mHeaderView.setVisibility(View.GONE);
				return;
			}

			mHeaderView.setVisibility(View.VISIBLE);
			
			/**
			 * calculate point (0,indicatorGroupHeight)
			 */
			int showHeight = indicatorGroupHeight;
			int nEndPos = this.pointToPosition(10, indicatorGroupHeight + 1);

			if (nEndPos != AdapterView.INVALID_POSITION) {
				long pos = this.getExpandableListPosition(nEndPos);
				int groupPos = ExpandableListView.getPackedPositionGroup(pos);
				int childPos = ExpandableListView.getPackedPositionChild(pos);

				int gViewPos = nEndPos - (childPos >= 0 ? (childPos + 1) : 0);
				MLog.d(TAG, String.format(
						"endPos:%d, pos:%d, gpos:%d, cpos:%d", nEndPos, pos,
						groupPos, childPos));
				if (groupPos != indicatorGroupId) {
					View viewNext = this.getChildAt(gViewPos
							- this.getFirstVisiblePosition());
					showHeight = viewNext.getTop();
				}
			}

			left = 0;
			top = -(indicatorGroupHeight - showHeight);
			right = mHeaderViewWidth;
			bottom = indicatorGroupHeight + top;
			MLog.d(TAG,
					String.format(
							"top:%d, bottom:%d, showHeight:%d, indicatorGroupHeight:%d",
							top, bottom, showHeight, indicatorGroupHeight));
			mHeaderView.layout(left, top, right, bottom);
			FrameLayout.LayoutParams param = (android.widget.FrameLayout.LayoutParams) mHeaderView
					.getLayoutParams();
			param.topMargin = top;

		}

		@Override
		public boolean expandGroup(int groupPos, boolean animate) {
			return super.expandGroup(groupPos, animate);
		}

		@Override
		public boolean collapseGroup(int groupPos) {
			return super.collapseGroup(groupPos);
		}

		public void singleClick() {
			if (lastEvent != null) {
				lastEvent.setAction(MotionEvent.ACTION_DOWN);
				super.dispatchTouchEvent(lastEvent);

				lastEvent.setAction(MotionEvent.ACTION_CANCEL);
				super.dispatchTouchEvent(lastEvent);
			}
		}

		@Override
		protected void onScrollChanged(int l, int t, int oldl, int oldt) {
			super.onScrollChanged(l, t, oldl, oldt);
			if (mHeaderView != null) {
				configHeader();
			}
		}

	}

}
