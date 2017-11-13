package com.zhisland.lib.view.tab;

import android.view.View;

public interface TabBarOnCreateListener {

	View createTabView(TabBarView view, ZHTabInfo tab, int atIndex);

	void selectTabView(View view, ZHTabInfo tabInfo);

	void unSelectTabView(View view);

}
