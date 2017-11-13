package com.zhisland.android.blog.aa.api;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.TaskCallback;

import org.apache.http.HttpResponse;

/**
 * 同步Vcard
 */
public class TaskVcardList extends TaskBase<Object, Object> {

    private List<PhoneContactUtil.ContactResult<String>> data;

    private PhoneContactUtil.ContactResult currentItem;

    public TaskVcardList(Object context, List<PhoneContactUtil.ContactResult<String>> data,
                         TaskCallback<Object> responseCallback) {
        super(context, responseCallback);
        isPureRest = true;
        this.data = data;
    }

    @Override
    public void execute() {
        if (data != null && data.size() > 0) {
            currentItem = data.remove(0);
            syncVCard(currentItem);
        }
    }

    private void syncVCard(PhoneContactUtil.ContactResult<String> data) {
        RequestParams params = null;
        params = this.put(params, "data", data.result);
        this.post(params, null);
    }

    @Override
    protected Object handleStringProxy(HttpResponse response) throws Exception {
        if (currentItem != null && currentItem.timestamp != null) {
            PhoneContactUtil.setLastTimestamp(currentItem.timestamp);
        }
        if (data != null && data.size() > 0) {
            currentItem = data.remove(0);
            syncVCard(currentItem);
        }
        return super.handleStringProxy(response);
    }

    @Override
    protected String getBaseUrl() {
        return Config.getHttpsHostUrl();
    }

    @Override
    protected String getPartureUrl() {
        return "user/contact/sync";
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<Object>() {
        }.getType();
    }

}
