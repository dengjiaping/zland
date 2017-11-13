package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;

/**
 * 主动邀请匹配通讯录
 */
public class TaskMatchContactsNormal extends TaskBase<ZHPageData<InviteUser>, Object> {

    private String vCardData;
    private String nextId;

    public TaskMatchContactsNormal(Object context, String vCardData, String nextId,
                                   TaskCallback<ZHPageData<InviteUser>> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.vCardData = vCardData;
        this.nextId = nextId;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "data", vCardData);
        params = this.put(params, "nextId", nextId);
        this.post(params, null);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "/contacts/match/invite";
    }

    @Override
    protected String getApiVersion() {
        return "1.1";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<ZHPageData<InviteUser>>() {
        }.getType();
    }

    @Override
    protected ZHPageData<InviteUser> handleStringProxy(HttpResponse response) throws Exception {
        return super.handleStringProxy(response);
    }

}
