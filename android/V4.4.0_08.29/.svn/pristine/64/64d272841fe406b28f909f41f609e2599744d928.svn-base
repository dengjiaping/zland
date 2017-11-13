package com.zhisland.lib.component.act;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.zhisland.lib.R;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.PageControl;
import com.zhisland.lib.view.SwipeView;
import com.zhisland.lib.view.SwipeView.OnPageChangedListener;
import com.zhisland.lib.view.tab.TabBarOnCreateListener;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.TabBarViewListener;
import com.zhisland.lib.view.tab.ZHTabInfo;

public abstract class BaseTabFragPageActivity extends BaseFragmentActivity
		implements TabBarViewListener, OnPageChangedListener { 

	private static final String TAG = "basetabpage";

	protected TabBarView tabBar;
	protected SwipeView lvContainer;

	private Fragment[] pages = null;
	private HashMap<ZHTabInfo, View> pageHolders;// 当启用lazyload的时候，用来占位
	private ArrayList<ZHTabInfo> tabs;

	/**
	 * current tab index
	 */
	private int curIndex = -1;
	private int tabCount;

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		this.setContentView(this.layoutResId());
		tabBar = (TabBarView) findViewById(R.id.tab_bar);
		tabBar.setBackgroundColor(Color.WHITE);
		tabBar.setListener(this);
		tabBar.setCreateListener(getCreateTabListener());
		lvContainer = (SwipeView) findViewById(R.id.tab_container);
		lvContainer.setOnPageChangedListener(this);
		Drawable drawable = lvContainer.getBackground();
		if (drawable == null) {
			lvContainer.setBackgroundColor(Color.WHITE);
		}

		tabs = this.tabToAdded();
		tabCount = tabs.size();
		this.tabBar.setTabs(tabs);
		initPageViews();

	}

	/**
	 * the layout of tab pages, tabbar's id is R.id.tab_bar, and tab container's
	 * id is R.id.tab_container
	 * 
	 * @return
	 */
	protected int layoutResId() {
		switch (titleType()) {
		case TitleType.TITLE_LAYOUT:
			return R.layout.base_tabpage_title;
		default:
			return R.layout.base_tabpage;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (curIndex >= 0 && pages[curIndex].isAdded()) {
			pages[curIndex].onActivityResult(requestCode, resultCode, data);
		}
	}

	private void initPageViews() {
		pages = new Fragment[tabCount];
		pageHolders = new HashMap<ZHTabInfo, View>(tabCount);

		boolean hasLoadFirstPage = false;
		for (int i = 0, size = tabs.size(); i < size; i++) {
			ZHTabInfo info = tabs.get(i);
			int containerId = getResources().getIdentifier("tab" + i, "id",
					this.getPackageName());
			LinearLayout singlePageContainer = new LinearLayout(this);
			singlePageContainer.setBackgroundColor(Color.TRANSPARENT);
			singlePageContainer.setId(containerId);
			pageHolders.put(info, singlePageContainer);
			lvContainer.addView(singlePageContainer);

			if (!hasLoadFirstPage) {
				hasLoadFirstPage = true;
				tryLoadPage(i);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem menu) {
		boolean res = this.pages[curIndex].onContextItemSelected(menu);
		if (!res)
			res = super.onContextItemSelected(menu);
		return res;
	}

	/**
	 * =====================tab bar listener=====================
	 */
	@Override
	public boolean shouldSelectTab(TabBarView view, ZHTabInfo tab, int atIndex) {
		return true;
	}

	@Override
	public void didSelectTabBar(TabBarView view, ZHTabInfo tab, int atIndex) {
		selectPage(atIndex, false);
	}

	@Override
	public boolean shouldSelect(int oldPage, int newPage) {
		return true;
	}

	@Override
	public void onPageChanged(int oldPage, int newPage) {
		selectPage(newPage, true);
	}

	protected void didSelectTab(ZHTabInfo tab, int atIndex) {

	}

	/**
	 * 
	 * @param index
	 *            page index or tab index
	 * @param shouldScrollTabBar
	 */
	protected void selectPage(final int index, final boolean shouldScrollTabBar) {

		MLog.d(TAG, "select page " + index);
		handler.post(new Runnable() {

			@Override
			public void run() {

				if (shouldScrollTabBar) {
					tabBar.setSelectedIndex(index, true, false);
				} else {
					lvContainer.smoothScrollToPage(index);
				}
			}
		});

		long delayed = shouldScrollTabBar ? 250 : 0;

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				curIndex = index;
				tryLoadPage(index);
				didSelectTab(tabs.get(index), index);
			}
		}, delayed);
	}

	/**
	 * if fragment has not been loaded, load it, else do nothing
	 */
	private void tryLoadPage(int index) {

		if (pages[index] != null)
			return;

		ZHTabInfo tabInfo = tabs.get(index);
		pages[index] = this.createTabPage(tabInfo);
		View pageContainer = pageHolders.remove(tabInfo);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.add(pageContainer.getId(), pages[index]);
		ft.commit();
	}

	protected void setScrollable(boolean scrollable) {
		lvContainer.isScrollable = scrollable;
	}

	protected void setPageControl(PageControl pc) {
		if (lvContainer != null) {
			lvContainer.setPageControl(pc);
		}
	}

	protected TabBarOnCreateListener getCreateTabListener() {
		return null;
	}

	protected Fragment getCurPage() {
		return curIndex >= 0 ? pages[curIndex] : null;
	}

	/**
	 * get current page index
	 */
	protected int getCurIndex() {
		return curIndex;
	}

	protected abstract Fragment createTabPage(ZHTabInfo tabInfo);

	protected abstract ArrayList<ZHTabInfo> tabToAdded();

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		MLog.d("bkey", event.toString());
		return super.dispatchKeyEvent(event);
	}

	public void selectByTabId(int id) {
		tabBar.selectByTabId(id);
	}

}
