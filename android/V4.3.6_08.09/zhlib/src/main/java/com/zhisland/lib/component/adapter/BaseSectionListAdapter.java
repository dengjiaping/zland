package com.zhisland.lib.component.adapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.zhisland.lib.bitmap.ImageCache;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.pulltorefresh.sectionlist.AnimatedExpandableListView;
import com.zhisland.lib.view.pulltorefresh.sectionlist.Groupable;
import com.zhisland.lib.view.pulltorefresh.sectionlist.OnGroupListener;
import com.zhisland.lib.view.pulltorefresh.sectionlist.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public abstract class BaseSectionListAdapter<G extends Groupable<C>, C> extends
		AnimatedExpandableListAdapter implements RecyclerListener,
		OnScrollListener {

	protected ArrayList<G> groups;
	protected LayoutInflater inflater;
	private OnGroupListener groupListener;
	protected AnimatedExpandableListView listview;
	private OnAdapterChangeListener changedListener;

	protected int firstVisiblePos = -1;
	protected int lastVisiblePos = -1;
	protected int scrollState;

	public BaseSectionListAdapter() {
		inflater = LayoutInflater.from(ZHApplication.APP_CONTEXT);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (changedListener != null) {
			int count = groups == null ? 0 : groups.size();
			changedListener.onDataChanged(count);
		}
	}

	/**
	 * 设置adapter 对应的listview
	 * 
	 * @param lisview
	 */
	public void setSectionList(AnimatedExpandableListView sectionList) {
		this.listview = sectionList;
		this.listview.setRecyclerListener(this);
		this.listview.setOnScrollListener(this);
		listview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// We call collapseGroupWithAnimation(int) and
				// expandGroupWithAnimation(int) to animate group
				// expansion/collapse.
				if (listview.isGroupExpanded(groupPosition)) {
					listview.collapseGroupWithAnimation(groupPosition);
				} else {
					listview.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}

		});
	}

	public void setDataChangeListener(OnAdapterChangeListener changeListener) {
		this.changedListener = changeListener;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		if (groupListener != null) {
			groupListener.onGroupCollapsed(groupPosition);
		}
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		if (groupListener != null) {
			groupListener.onGroupExpanded(groupPosition);
		}
	}

	public G getGroup(int groupPosition) {

		if (groups != null && groups.size() > groupPosition) {
			return groups.get(groupPosition);
		}

		return null;
	}

	public int getGroupCount() {
		return groups != null ? groups.size() : 0;
	}

	public ArrayList<G> getGroups() {
		return groups;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public C getChild(int groupPosition, int childPosition) {
		Groupable<C> group = this.getGroup(groupPosition);
		if (group != null) {
			ArrayList<C> children = group.getChildren();
			if (children != null && children.size() > childPosition) {
				return children.get(childPosition);
			}
		}
		return null;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		Groupable<C> group = this.getGroup(groupPosition);
		if (group != null) {
			ArrayList<C> children = group.getChildren();
			if (children != null) {
				return children.size();
			}
		}
		return 0;
	}

	public boolean shouldLoadImg(String imgUrl) {
		return (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_FLING || ImageCache
				.getInstance().containBitmap(imgUrl)) ? true : false;
	}

	@Override
	public void onMovedToScrapHeap(View view) {
		if (view != null) {
			recycleView(view);
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * =================abstract method to override======================
	 */

	public abstract View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent);

	/**
	 * Indicates that the specified View was moved into the recycler's scrap
	 * heap. The view is not displayed on screen any more and any expensive
	 * resource associated with the view should be discarded.
	 */
	public abstract void recycleView(View view);

	/**
	 * ====================custom method===============
	 */

	public void add(ArrayList<G> groupsToAdded) {
		if (groupsToAdded == null || groupsToAdded.size() < 1) {
			notifyDataSetChanged();
			return;
		}

		if (groups == null) {
			// do NOT use groupsToAdded directly, since the caller may do other
			// operation on it
			groups = new ArrayList<G>();
		}

		if (groups.size() < 1) {
			groups.addAll(groupsToAdded);
			this.notifyDataSetChanged();
			return;
		}

		G lastSection = getGroup(groups.size() - 1);
		G firstSection = groupsToAdded.get(0);

		if (isSameSection(lastSection, firstSection)) {
			groupsToAdded.remove(firstSection);
			lastSection.addChildren(firstSection.getChildren());
		}
		groups.addAll(groupsToAdded);
		this.notifyDataSetChanged();
	}

	protected boolean isSameSection(G lastSection, G firstSection) {
		return false;
	}

	public void clear() {

		if (groups != null) {
			groups.clear();
		}
	}

	public void expandAll() {
		int groupCount = this.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			Object obj = listview.getExpandableListAdapter();
			if (obj != null) {
				this.listview.expandGroup(i);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_FLING: {
			break;
		}
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: {
			firstVisiblePos = view.getFirstVisiblePosition();
			lastVisiblePos = view.getLastVisiblePosition();
			MLog.d("listada",
					"touch scroll"
							+ String.format("first:%d, last:%d",
									firstVisiblePos, lastVisiblePos));
			break;
		}
		case OnScrollListener.SCROLL_STATE_IDLE: {
			int absFirst = Math.abs(view.getFirstVisiblePosition()
					- firstVisiblePos);
			int absLast = Math.abs(view.getLastVisiblePosition()
					- lastVisiblePos);
			if (absFirst > 0 || absLast > 0) {
				notifyDataSetChanged();
			}

			MLog.d("listada",
					"state idle "
							+ String.format("first:%d, last:%d", absFirst,
									absLast));
			break;
		}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}
	
	public void setGroup(ArrayList<G> groups){
		this.groups = groups;
	}
}
