package com.zhisland.lib.view.tab;

import java.io.Serializable;

public class ZHTabInfo implements Serializable {

	private static final long serialVersionUID = -6351746230195128213L;

	public ZHTabInfo(String name, int tabId) {
		this(name, tabId, null);
	}

	public ZHTabInfo(String name, int tabId, Object tag) {
		this.name = name;
		this.tabId = tabId;
		this.tag = tag;
	}

	public ZHTabInfo(String name, int tabId, int arg1, Object tag) {
		this.name = name;
		this.tabId = tabId;
		this.tag = tag;
		this.arg1 = arg1;
	}

	/**
	 * when large than 0, will create page and show it, or not create
	 */
	// public int showPage = 1;

	public String name;

	public int tabId;

	public int arg1;

	public Object tag;

	@Override
	public String toString() {
		return name;
	}

}
