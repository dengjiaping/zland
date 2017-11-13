package com.zhisland.android.blog.contacts.api;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;

/**
 * 通过手机号进行邀请
 */
public class TaskInviteByPhone extends TaskBase<String, Object> {

    String name;        //用户名称
    String countryCode;    //国家码
    String phone;       //电话

    public TaskInviteByPhone(Object context, String name, String countryCode, String phone,
                             TaskCallback<String> responseCallback) {
        super(context, responseCallback);
        this.isPureRest = true;
        this.name = name;
        this.countryCode = countryCode;
        this.phone = phone;
    }

    @Override
    public void execute() {
        RequestParams params = null;
        params = this.put(params, "name", name);
        params = this.put(params, "countryCode", countryCode);
        params = this.put(params, "phone", phone);
        this.post(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/invite/phone";
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
    protected String handleStringProxy(HttpResponse response) throws Exception {
        return convertToString(response);
    }
}
