package com.zhisland.im.data;

import com.zhisland.lib.view.pulltorefresh.sectionlist.Groupable;

import java.util.ArrayList;

public class UserGroup implements Groupable<IMUser> {

    public String letter;

    public ArrayList<IMUser> contacts = new ArrayList<>();

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
    public void addChild(IMUser child) {
        contacts.add(child);
    }

}
