package com.zhisland.android.blog.tabhome.model.remote;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.TaskBase;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.tabhome.bean.FriendRequestAndUserDetail;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.Pinyin4jUtil;
import com.zhisland.lib.util.StringUtil;

import org.apache.http.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取 好友请求和用户详情
 */
public class TaskGetFriendRequestAndUserDetail extends TaskBase<FriendRequestAndUserDetail, Object> {


    public TaskGetFriendRequestAndUserDetail(Context context,
                                             TaskCallback<FriendRequestAndUserDetail> responseCallback) {
        super(context, responseCallback);
    }

    @Override
    public void execute() {
        RequestParams params = null;
        this.get(params, null);
    }

    @Override
    protected String getPartureUrl() {
        return "/interception/switching";
    }

    @Override
    protected String getApiVersion() {
        return "1.0";
    }

    @Override
    protected Type getDeserializeType() {
        return new TypeToken<FriendRequestAndUserDetail>() {
        }.getType();
    }

    @Override
    protected boolean isBackgroundTask() {
        return true;
    }

    @Override
    protected FriendRequestAndUserDetail handleStringProxy(HttpResponse response)
            throws Exception {

        handleHeaderInfo(response);
        String responseBody = convertToString(response);
        Type listType = this.getDeserializeType();

        FriendRequestAndUserDetail res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);
        saveUser(res.user);

        return res;
    }

    /**
     * 保存用户
     */
    private void saveUser(User user) {
        // 保存当前用户信息
        if (user.uid == PrefUtil.Instance().getUserId()) {
            PrefUtil.Instance().setNeedRefrshSelf(false);
            User.saveSelfUserToPrefUtil(user);
            try {
                String body = GsonHelper.GetCommonGson().toJson(user);
                PrefUtil.Instance().setUserJsonString(body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 插入数据库
        DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
    }
}
