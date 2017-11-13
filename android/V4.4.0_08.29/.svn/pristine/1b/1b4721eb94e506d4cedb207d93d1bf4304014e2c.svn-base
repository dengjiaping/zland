package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.EventListStruct;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.StringUtil;

import java.lang.reflect.Type;

/**
 * 获取活动列表
 */
public class TaskGetEventList extends TaskBase<EventListStruct, Object> {

	private long eventType;
	private String eventTag;
    private String orderId;
	private String nextId;

	public TaskGetEventList(Object context, long eventType, String eventTag, String orderId,
                            String nextId, TaskCallback<EventListStruct> responseCallback) {
		super(context, responseCallback);
		this.isPureRest = true;
		this.eventType = eventType;
		this.eventTag = eventTag;
        this.orderId = orderId;
		this.nextId = nextId;
	}

	@Override
	public void execute() {
		RequestParams params = null;
        // 活动类型
		if (eventType == Event.CATEGORY_TYPE_OFFICIAL) {
			params = this.put(params, "type", eventType);
		}
        // 活动标签
		if (eventType == Event.CATEGORY_TYPE_TAG) {
			if (!StringUtil.isNullOrEmpty(eventTag)) {
				params = this.put(params, "tag", eventTag);
			}
        }
        // 排序规则
        params = this.put(params, "order", orderId);
        params = this.put(params, "nextId", nextId);
		this.post(params, null);
	}

	@Override
	protected String getPartureUrl() {
		return "/event/list/current";
	}

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
	protected Type getDeserializeType() {
		return new TypeToken<EventListStruct>() {
		}.getType();
	}

}
