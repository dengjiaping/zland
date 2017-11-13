package com.zhisland.android.blog.im.view.row;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;

import com.beem.project.beem.service.Contact;
import com.zhisland.android.blog.im.view.adapter.ImSessAdapter.OnAdapterListener;
import com.zhisland.im.data.IMMessage;

public abstract class BaseRowListenerImpl implements OnRowListener,
		OnClickListener {

	protected Context context;
	protected LayoutInflater inflater;
	protected IMMessage msg;
	protected Contact userSelf;
	protected Contact userOther;

	private OnCreateContextMenuListener menuListener;

	public BaseRowListenerImpl(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public void performOnClick() {
	}

	@Override
	public void fill(IMMessage msg) {
		this.msg = msg;
	}

	@Override
	public void recycle() {
		this.msg = null;
	}

	@Override
	public final IMMessage getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUsers(Contact userSelf, Contact userOther) {
		this.userOther = userOther;
		this.userSelf = userSelf;
	}

	@Override
	public void setOnCreateMenuListener(
			OnCreateContextMenuListener createMenuListener) {
		this.menuListener = createMenuListener;
	}

	@Override
	public void setOnAdapterListener(OnAdapterListener adapterListener) {
	}

	@Override
	public void onClick(View view) {
	}

	/**
	 * set onCreateMenulistener and message tag
	 */
	protected void fillMenu(View view) {
		BaseRowUtil.fillMenu(view, menuListener, msg, this);
	}

	/**
	 * clean onCreateMenulistener and message tag
	 * 
	 */
	protected void cleanMenu(View view) {
		BaseRowUtil.cleanMenu(view);
	}

}
