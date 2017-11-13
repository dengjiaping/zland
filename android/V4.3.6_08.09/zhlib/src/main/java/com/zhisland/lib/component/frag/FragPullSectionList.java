package com.zhisland.lib.component.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.LinearLayout;

import com.zhisland.lib.R;
import com.zhisland.lib.component.adapter.BaseSectionListAdapter;
import com.zhisland.lib.view.pulltorefresh.PullRefeshListener;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;
import com.zhisland.lib.view.pulltorefresh.sectionlist.AnimatedExpandableListView;
import com.zhisland.lib.view.pulltorefresh.sectionlist.Groupable;
import com.zhisland.lib.view.pulltorefresh.sectionlist.PullToRefreshSectionListProxy;
import com.zhisland.lib.view.pulltorefresh.sectionlist.SectionWrap;

/**
 * section list的基类
 * 
 * @author arthur
 *
 * @param <G>
 * @param <C>
 */
public class FragPullSectionList<G extends Groupable<C>, C> extends
		FragBasePull<AnimatedExpandableListView> implements
		OnChildClickListener, PullRefeshListener {

	protected BaseSectionListAdapter<G, C> sectionAdapter;
	protected Class<?> clsKey;

	/**
	 * must same as your groupview's layout, absolutely same, so the best way is
	 * put your group layout in a xml file, and then return xml's reousrce id.
	 */
	protected int sectionResource() {
		return R.id.invalidResId;
	}

	/**
	 * if you do not want to put section in xml file, you can return section
	 * view diresctly
	 * 
	 * @return
	 */
	protected View sectionView() {
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		View sectionView = this.sectionView();
		int secViewId = this.sectionResource();
		if (sectionView == null && secViewId != R.id.invalidResId) {
			sectionView =getActivity().getLayoutInflater().inflate(secViewId,
					getPullProxy().getPullView().getRefreshableView(), false);
		} else if (sectionView != null) {
			LayoutParams param = new AbsListView.LayoutParams(
					AbsListView.LayoutParams.MATCH_PARENT,
					AbsListView.LayoutParams.WRAP_CONTENT);
			sectionView.setLayoutParams(param);
		}
		if (sectionView != null) {
			sectionView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});
		}
		((SectionWrap) pullView.getRefreshableViewWrapper())
				.setPinnedHeaderView(sectionView);
		getPullProxy().getPullView().getRefreshableView()
				.setGroupIndicator(null);

		getPullProxy().getPullView().getRefreshableView()
				.setOnChildClickListener(this);
		getPullProxy().getPullView().getRefreshableView()
				.setOnCreateContextMenuListener(this);

	}

	/**
	 * 禁用section的折叠功能
	 */
	protected void disableCollapse() {
		getPullProxy().getPullView().getRefreshableView()
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						getPullProxy().getPullView().getRefreshableView()
								.expandGroup(groupPosition);

					}
				});
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		C item = this.sectionAdapter.getChild(groupPosition, childPosition);
		if (item != null) {
			this.onItemClick(item);
		}
		return true;
	}

	/**
	 * 当子项目被点击时出发这个事件
	 * 
	 * @param item
	 */
	protected void onItemClick(C item) {

	}

	@Override
	public void loadNormal() {

	}

	@Override
	public void loadMore(String nextId) {

	}

	@Override
	protected View createDefaultFragView() {
		return LayoutInflater.from(getActivity()).inflate(
				R.layout.pull_to_refresh_section_list, null);
	}

	@Override
	protected PullToRefreshProxy<AnimatedExpandableListView> createProxy() {
		return new PullToRefreshSectionListProxy<G, C>();

	}

	@Override
	protected String getPageName() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PullToRefreshSectionListProxy<G, C> getPullProxy() {
		return (PullToRefreshSectionListProxy<G, C>) super.getPullProxy();
	}

}