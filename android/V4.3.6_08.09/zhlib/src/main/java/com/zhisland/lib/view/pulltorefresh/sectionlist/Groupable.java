package com.zhisland.lib.view.pulltorefresh.sectionlist;

import java.util.ArrayList;

public interface Groupable<C> {

	String getTitle();

	ArrayList<C> getChildren();

	void addChild(C child);

	void addChildren(ArrayList<C> children);

}
