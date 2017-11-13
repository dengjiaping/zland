package com.zhisland.android.blog.im.view.row;

import android.view.View;
import android.view.View.OnCreateContextMenuListener;

import com.beem.project.beem.service.Contact;
import com.zhisland.android.blog.im.view.adapter.ImSessAdapter.OnAdapterListener;
import com.zhisland.im.data.IMMessage;

/**
 * all chat row should implements this interface, now provide two base class
 * {@link BaseRowView} and {@link BaseRowListenerImpl}
 */
public interface OnRowListener {

	void fill(IMMessage msg);

	IMMessage getMessage();

	void refresh(int status, int progress);

	void recycle();

	void setUsers(Contact userSelf, Contact userOther);
 
	void setOnCreateMenuListener(OnCreateContextMenuListener createMenuListener);

	void setOnAdapterListener(OnAdapterListener createMenuListener);

	View getView();

	void performOnClick();

}
