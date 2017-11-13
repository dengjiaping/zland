package com.zhisland.im.event;

/**
 * XMPP 长链接通知
 * 
 * @author arthur
 *
 */
public class EventConnection {

	public static final int ACTION_COLSED = 1; // 链接关闭
	public static final int ACTION_CONNECTING = 2;// 正在连接
	public static final int ACTION_CONNECTED = 3;// 链接成功
	public static final int ACTION_CONFLICT = 4;// 账号在其他地方登录

	/**
	 * 
	 * @param action
	 *            {@code XmppEvent#ACTION_COLSED}
	 */
	public EventConnection(int action) {
		this.action = action;
	}

	public int action;

}
