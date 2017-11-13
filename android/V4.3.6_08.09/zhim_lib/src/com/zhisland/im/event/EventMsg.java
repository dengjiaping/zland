package com.zhisland.im.event;

import com.beem.project.beem.service.Contact;

/**
 * 当收到一条新的消息时，XMPP类库会通过eventbus抛出这个事件
 * 
 * @author arthur
 *
 */
public class EventMsg {

	private Contact contact;
	private int unreadCount;
	private String msgBody;

	public EventMsg(Contact contact, int unreadCount, String msgBody) {
		this.contact = contact;
		this.unreadCount = unreadCount;
		this.msgBody = msgBody;
	}

	public Contact getContact() {
		return contact;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public String getMsgBody() {
		return msgBody;
	}

}
