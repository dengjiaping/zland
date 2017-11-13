package com.zhisland.im.event;

/**
 * 关系事件通知
 * 
 * @author arthur
 *
 */
public class EventRelation {
	
	/**
	 * 接收到好友申请
	 * */
	public static final int RECEIVE_INVITE = 1;
	/**
	 * 接收到通过好友
	 * */
	public static final int RECEIVE_ACCEPTED = 2;
	/**
	 * 接收到对方删除好友
	 * */
	public static final int RECEIVE_DELETE = 3;
	
	/**
	 * 接受别人的好友请求
	 * */
	public static final int REQUEST_ACCEPTED = 4;
	
	/**
	 * 忽略别人的好友申请
	 * */
	public static final int REQUEST_IGNORE = 5;

	public String jid;
	public String name;
	public int action;
	public String msg;

	/**
	 * ，
	 * 
	 * @param jid
	 *            对方的jid
	 * @param action
	 *            {@code FriendRelationJson#ASK_ACCEPTED}
	 * @param msg
	 *            请求操作附带的消息
	 */
	public EventRelation(String jid, String name, int action, String msg) {
		this.jid = jid;
		this.name = name;
		this.action = action;
		this.msg = msg;
	}

}
