package com.zhisland.lib.view.tab;

public interface TabBarViewListener {

	public boolean shouldSelectTab(TabBarView view, ZHTabInfo tab, int atIndex);

	public void didSelectTabBar(TabBarView view, ZHTabInfo tab, int atIndex);
}
