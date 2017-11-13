package com.zhisland.android.blog.profile.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.tab.TabBarOnCreateListener;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.TabBarViewListener;
import com.zhisland.lib.view.tab.ZHTabInfo;

/**
 * 个人页 tabbar,配置、包装两个TabBarView
 */
public abstract class TabWrapper implements TabBarViewListener,
		TabBarOnCreateListener {

	public static final int PROFILE_COVER = 1;//封面
	public static final int PROFILE_DYNAMIC = 2;//动态
	public static final int PROFILE_IMPRESSION = 3;//印象

	private List<TabBarView> tabBarViews = new ArrayList<TabBarView>();
	private List<TabButton> tabButtonList = new ArrayList<TabButton>();
	private Context context;
	private int curSelectedTabId;

	public TabWrapper(Context context) {
		this.context = context;
	}

	/**
	 * 增加一个tabView
	 * 
	 * @param tabbar
	 */
	public void addTabView(TabBarView tabbar) {
		tabBarViews.add(tabbar);
		initView(tabbar);
		setTabs(tabbar);
	}

	private void initView(TabBarView tabBarView) {
		tabBarView.setBottomIndicator(true);
		tabBarView.setListener(this);
		tabBarView.setCreateListener(this);
	}

	/**
	 * 初始化tabs //TODO 可以public出去给其他人使用
	 */
	private void setTabs(TabBarView tabBarView) {
		ArrayList<ZHTabInfo> tabs = new ArrayList<ZHTabInfo>();
		ZHTabInfo tab = new ZHTabInfo("封面", PROFILE_COVER);
		tabs.add(tab);
		tab = new ZHTabInfo("动态", PROFILE_DYNAMIC);
		tabs.add(tab);
		tab = new ZHTabInfo("印象", PROFILE_IMPRESSION);
		tabs.add(tab);
		tabBarView.setTabs(tabs);
	}
	
	public void refreshBtnName(boolean isSelf, boolean isMan) {
		//暂时不用了 同一叫做动态
	}

	@Override
	public boolean shouldSelectTab(TabBarView view, ZHTabInfo tab, int atIndex) {
		return true;
	}

	@Override
	public void didSelectTabBar(TabBarView view, ZHTabInfo tab, int atIndex) {

		for (TabBarView tabbar : tabBarViews) {
			if (view == tabbar || tabbar.getSelectedTab().tabId == tab.tabId)
				continue;

			tabbar.selectByTabId(tab.tabId);
		}
		if (curSelectedTabId != tab.tabId) {
			this.curSelectedTabId = tab.tabId;
			selectTabBar(view, tab, atIndex);
		}
	}

	public abstract void selectTabBar(TabBarView view, ZHTabInfo tab,
			int atIndex);

	@Override
	public View createTabView(TabBarView view, ZHTabInfo tab, int atIndex) {
		TabButton tb = new TabButton(context, tab.tabId);
		tb.text.setText(tab.name);
		tabButtonList.add(tb);
		return tb;
	}

	@Override
	public void selectTabView(View view, ZHTabInfo tabInfo) {
		TabButton tb = (TabButton) view;
		tb.text.setTextColor(context.getResources().getColor(R.color.color_dc));
	}

	@Override
	public void unSelectTabView(View view) {
		TabButton tb = (TabButton) view;
		tb.text.setTextColor(context.getResources().getColor(
				R.color.color_f2));
	}

	public static class TabButton extends RelativeLayout {

		public final TextView text;
		
		public int tabId;

		public TabButton(Context context, int tabId) {
			super(context);

			this.tabId = tabId;
			text = new TextView(getContext());
			RelativeLayout.LayoutParams textpl = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			DensityUtil.setTextSize(text, R.dimen.txt_14);
			textpl.addRule(RelativeLayout.CENTER_IN_PARENT);
			this.addView(text, textpl);
		}

	}
}
