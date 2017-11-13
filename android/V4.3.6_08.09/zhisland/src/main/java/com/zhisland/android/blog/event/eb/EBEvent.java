package com.zhisland.android.blog.event.eb;

import com.zhisland.android.blog.event.dto.Event;

/**
 * 活动EventBus
 */
public class EBEvent {

	/** 通知 ：活动创建成功 */
	public static final int TYPE_EVENT_CREATE = 2;
	/** 通知 ：报名了一个活动 */
	public static final int TYPE_EVENT_SIGN_UP = 3;
	/** 通知 : 审核通过一位报名我的活动的用户 */
	public static final int TYPE_EVENT_APPROVED = 4;
	/** 支付状态发生变化 */
	public static final int TYPE_EVENT_PAY_STATUS_CHANGED = 11;
	/** 通知 : 我报名的活动的活动状态、审核状态发生变化，需要更新我报名的活动列表 */
	public static final int TYPE_EVENT_STATUS_CHANGED = 12;
    /** 跳转报名确认页 */
    public static final int TYPE_SIGN_CONFIRM = 13;

	private int type;
	private Event event;
	private Object obj;

	public EBEvent(int type, Event event) {
		this.type = type;
		this.event = event;
	}

	public int getType() {
		return type;
	}

	public Event getEvent() {
		return event;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}
}
