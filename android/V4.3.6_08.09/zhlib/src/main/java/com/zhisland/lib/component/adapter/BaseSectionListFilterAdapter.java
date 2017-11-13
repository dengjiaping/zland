package com.zhisland.lib.component.adapter;

import java.util.ArrayList;

import android.widget.AbsListView.RecyclerListener;

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.pulltorefresh.sectionlist.Groupable;

public abstract class BaseSectionListFilterAdapter<G extends Groupable<C>, C>
		extends BaseSectionListAdapter<G, C> implements RecyclerListener {

	protected String keyword = null;
	private ArrayList<Integer> groupIndexes;
	private ArrayList<ArrayList<Integer>> childIndexes;

	public BaseSectionListFilterAdapter() {
		super();
		groupIndexes = new ArrayList<Integer>();
		childIndexes = new ArrayList<ArrayList<Integer>>();
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private void filter() {

		long start = System.currentTimeMillis();
		groupIndexes.clear();
		childIndexes.clear();
		if (groups == null)
			return;

		int groupIndex = 0;
		for (G group : groups) {
			ArrayList<C> children = group.getChildren();
			if (children != null) {
				int childIndex = 0;
				ArrayList<Integer> cIndex = null;
				for (C child : children) {
					if (isChildMatched(child, keyword)) {
						if (cIndex == null) {
							cIndex = new ArrayList<Integer>();
						}
						cIndex.add(childIndex);
					}
					childIndex++;
				}
				if (cIndex != null) {
					groupIndexes.add(groupIndex);
					childIndexes.add(cIndex);
				}
			}
			groupIndex++;
		}

		MLog.d("filter", System.currentTimeMillis() - start + "ms");
	}

	protected boolean isChildMatched(C child, String keyword) {
		return true;
	}

	@Override
	public void notifyDataSetChanged() {
		this.filter();
		super.notifyDataSetChanged();
	}

	public int getGroupCount() {
		return groupIndexes.size();
	}

	public G getGroup(int groupPosition) {

		if (groups != null && groupIndexes.size() > groupPosition) {
			return groups.get(groupIndexes.get(groupPosition));
		}

		return null;
	}

	public C getChild(int groupPosition, int childPosition) {

		Groupable<C> group = this.getGroup(groupPosition);
		if (group != null) {
			ArrayList<C> children = group.getChildren();
			ArrayList<Integer> cids = childIndexes.get(groupPosition);
			if (children != null && cids.size() > childPosition) {
				return children.get(cids.get(childPosition));
			}
		}
		return null;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		Groupable<C> group = this.getGroup(groupPosition);
		if (group != null) {
			return childIndexes.get(groupPosition).size();
		}
		return 0;
	}

}
