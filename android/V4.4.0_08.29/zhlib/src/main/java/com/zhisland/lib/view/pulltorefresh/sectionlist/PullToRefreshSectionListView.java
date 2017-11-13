package com.zhisland.lib.view.pulltorefresh.sectionlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;

public class PullToRefreshSectionListView extends
		PullToRefreshAdapterViewBase<AnimatedExpandableListView> {

	public PullToRefreshSectionListView(Context context) {
		super(context);
	}

	public PullToRefreshSectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshSectionListView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshSectionListView(Context context, Mode mode,
			AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	SectionWrap sectionWrap;

	@Override
	protected AnimatedExpandableListView createRefreshableView(Context context,
			AttributeSet attrs) {
		sectionWrap = new SectionWrap(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		sectionWrap.getInterList().setId(android.R.id.list);
		return sectionWrap.getInterList();
	}

	@Override
	protected FrameLayout createRefreshWrapper(Context context,
			AnimatedExpandableListView refreshableView) {
		return sectionWrap;
	}

}
