package com.zhisland.android.blog.im.controller;

import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.event.EventConnection;

/**
 * 用来跟踪链接状态，并更新标题
 * 
 * @author arthur
 *
 */
public class XmppTitleListener {

	private TitleBarProxy titleBar;
	private String ttt;
	private boolean enabled = true;

	public XmppTitleListener(TitleBarProxy titleBar, String title) {
		this.titleBar = titleBar;
		this.ttt = title;
		refreshTitle();
	}

	public void onEventMainThread(EventConnection event) {
		if (!enabled) {
			return;
		}
		String title = null;
		switch (event.action) {
		case EventConnection.ACTION_COLSED:
			title = String.format("%s(%s..)", ttt, "未连接");
			break;
		case EventConnection.ACTION_CONNECTING:
			title = String.format("%s(%s..)", ttt, "连接中..");
			break;
		default:
			title = this.ttt;
			break;
		}
		this.titleBar.setTitle(title);
	}

	/**
	 * 启用xmpp监听更新title
	 */
	public void enable() {
		enabled = true;
	}

	/**
	 * 禁用xmpp监听更新title
	 */
	public void disable() {
		enabled = false;
	}

	/**
	 * 根据xmpp状态更新title
	 */
	public void refreshTitle() {
		if (BeemApplication.getInstance().isConnected()) {
			this.titleBar.setTitle(ttt);
		} else {
			this.titleBar.setTitle(String.format("%s(%s)", ttt, "未连接"));
		}
	}
}
