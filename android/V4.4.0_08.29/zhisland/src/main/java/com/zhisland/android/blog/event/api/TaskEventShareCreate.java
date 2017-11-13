package com.zhisland.android.blog.event.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;

/*
 * 创建活动 
 */
public class TaskEventShareCreate extends TaskBase<Feed, Object> {

    private Event event;

    public TaskEventShareCreate(Object context, Event event,
                                TaskCallback<Feed> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.event = event;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "title", event.eventTitle);
        params = this.put(params, "content", event.content);
        params = this.put(params, "category", event.category);
        params = this.put(params, "startTime", event.startTime);
        params = this.put(params, "endTime", event.endTime);
        params = this.put(params, "provinceId", event.provinceId);
        params = this.put(params, "cityId", event.cityId);
        params = this.put(params, "provinceName", event.provinceName);
        params = this.put(params, "cityName", event.cityName);
        params = this.put(params, "location", event.location);
        if (event.price == null) {
            event.price = 0f;
        }
        params = this.put(params, "price", "" + event.price);
        if (event.userLimitLevel != null) {
            params = this.put(params, "userLimitLevel", event.userLimitLevel);
        }
        if (event.totalNum != null) {
            params = this.put(params, "totalNum", event.totalNum);
        }
        if (event.contactMobile != null) {
            params = this.put(params, "contactMobile", event.contactMobile);
        }
        if (event.userLimitLevel != null) {
            params = this.put(params, "displayLevel", event.displayLevel);
        }
        this.post(params, null);
    }

    @Override
    protected String getApiVersion() {
        return "1.1";
    }

    @Override
    protected String getPartureUrl() {
        return "/event";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Feed>() {
        }.getType();
    }

    @Override
    protected Feed handleStringProxy(HttpResponse response) throws Exception {
        Feed result = super.handleStringProxy(response);
        return result;
    }

}
