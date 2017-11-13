package com.zhisland.im.event;

import com.zhisland.im.data.IMContact;

/**
 * 联系人相关的通知
 * 
 * @author arthur
 *
 */
public class EventFriend {
	
	/**
	 * 增加好友
	 * */
	public static final int ACTION_ADD = 1;
	
	/**
	 * 删除好友
	 * */
	public static final int ACTION_DELETE = 2;

	public EventFriend(int action,IMContact contact) {
		this.action = action;
		this.contact = contact;
	}

	public int action;
	
	public IMContact contact;
}
