package com.zhisland.im.data;

import com.zhisland.lib.view.pulltorefresh.sectionlist.Groupable;

import java.util.ArrayList;

public class ContactGroup implements Groupable<IMContact> {

	public String letter;

	public ArrayList<IMContact> contacts = new ArrayList<IMContact>();

	@Override
	public String getTitle() {
		return letter;
	}

	@Override
	public ArrayList getChildren() {
		return contacts;
	}

	@Override
	public void addChildren(ArrayList children) {
		contacts.addAll(children);
	}

	@Override
	public void addChild(IMContact child) {
		contacts.add(child);
	}

}
