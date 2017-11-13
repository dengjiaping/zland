package com.zhisland.android.blog.aa.api;

import android.content.Context;

import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.contacts.api.TaskGetInvitationRequests;
import com.zhisland.android.blog.contacts.api.TaskGetRecommendUser;
import com.zhisland.android.blog.contacts.api.TaskGetUsersNearBy;
import com.zhisland.android.blog.contacts.api.TaskIgnoreRequestByUid;
import com.zhisland.android.blog.contacts.api.TaskInvite;
import com.zhisland.android.blog.contacts.api.TaskInviteByPhone;
import com.zhisland.android.blog.contacts.api.TaskMatchContactsNormal;
import com.zhisland.android.blog.contacts.api.TaskProcessRecommend;
import com.zhisland.android.blog.contacts.api.TaskSearchUsers;
import com.zhisland.android.blog.contacts.api.TaskUpdateLoc;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.contacts.dto.Loc;
import com.zhisland.android.blog.freshtask.model.remote.TaskAddResource;
import com.zhisland.android.blog.freshtask.model.remote.TaskCallFriend;
import com.zhisland.android.blog.freshtask.model.remote.TaskUpdateUserFigure;
import com.zhisland.android.blog.profile.api.TaskGetUserDetail;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.setting.api.TaskInvisiblePosition;
import com.zhisland.android.blog.tabhome.model.remote.TaskGetAllDict;
import com.zhisland.android.blog.tabhome.model.remote.TaskGetFriendRequestAndUserDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;
import java.util.List;

public class ZHApiUser extends ApiBase {


    private static class Holder {
        private static ZHApiUser INSTANCE = new ZHApiUser();
    }

    public static ZHApiUser Instance() {
        return Holder.INSTANCE;
    }

    private ZHApiUser() {

    }

    /**
     * 更新user信息
     */
    public void updateUser(Context context, User user, int from,
                           TaskCallback<Object> responseCallback) {
        addTask(context, new TaskUpdateUser(context, user, from,
                responseCallback));
    }

    /**
     * 新手任务，更新user形象照
     */
    public void updateUserFigure(Context context, String figure,
                                 TaskCallback<Object> responseCallback) {
        addTask(context, new TaskUpdateUserFigure(context, figure,
                responseCallback));
    }

    /**
     * 新手任务，添加资源
     */
    public void addReource(Context context, String resource,
                                       TaskCallback<Object> responseCallback) {
        addTask(context, new TaskAddResource(context, resource,
                responseCallback));
    }

    /**
     * 获取用户信息
     */
    public void getUserDetail(Context context, long userId, TaskCallback<UserDetail> responseCallback) {
        addTask(context, new TaskGetUserDetail(context, userId,
                responseCallback));
    }

    /**
     * 上传位置
     */
    public void updateLoc(Context context, Loc loc, TaskCallback<Object> callback) {
        addTask(context, new TaskUpdateLoc(context, loc, callback));
    }

    /**
     * 获取推荐人列表
     */
    public void getRecommendUser(Context context, TaskCallback<ArrayList<User>> callback) {
        addTask(context, new TaskGetRecommendUser(context, callback));
    }

    /**
     * 处理推荐人，添加好友或者忽略
     */
    public void processRecommend(Context context, long targetUid, int mark,
                                 TaskCallback<Object> callback) {
        addTask(context, new TaskProcessRecommend(context, targetUid, mark,
                callback));
    }

    /**
     * 获取附近的人
     */
    public void getUsersNearBy(Context context, String loc, String maxId,
                               TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetUsersNearBy(context, loc, maxId, callback));
    }

    /**
     * 搜索用户
     */
    public void search(Context context, String keyword, String maxId,
                       TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskSearchUsers(keyword, context, maxId, callback));
    }

    /**
     * 设置是否对附近的人可见
     */
    public void setInvisiblePosition(Context context, Integer invisible,
                                     TaskCallback<Object> callback) {
        addTask(context,
                new TaskInvisiblePosition(context, invisible, callback));
    }

    /**
     * 忽略好友申请
     */
    public void ignoreRequest(Context context, TaskCallback<Object> callback, long applyUid) {
        addTask(context,
                new TaskIgnoreRequestByUid(context, callback, applyUid));
    }

    /**
     * 用户收到的邀请请求列表
     */
    public void getInvitationRequests(Context context, String nextId,
                                      TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetInvitationRequests(context, nextId,
                callback));
    }

    /**
     * 同步Vcard
     */
    public void syncVcard(Context context, List<PhoneContactUtil.ContactResult<String>> data, TaskCallback<Object> callback) {
        addTask(context, new TaskVcardList(context, data, callback));
    }

    /**
     * 发邀请
     */
    public void invite(Context context, String name, String mobile,
                       TaskCallback<String> callback) {
        addTask(context, new TaskInvite(context, name, mobile,
                callback));
    }

    /**
     * 主动邀请，通讯录匹配
     */
    public void matchContactsNormal(Context context, String vcard, String nextId, TaskCallback<ZHPageData<InviteUser>> callback) {
        addTask(context, new TaskMatchContactsNormal(context, vcard, nextId, callback));
    }

    /**
     * 主动邀请，通讯录匹配 more
     */
    public void inviteByPhone(Context context, String name, String countryCode, String phone, TaskCallback<String> callback) {
        addTask(context, new TaskInviteByPhone(context, name, countryCode, phone, callback));
    }

    /**
     * 召唤好友
     */
    public void callFriend(Context context, String data,  TaskCallback<Object> callback) {
        addTask(context, new TaskCallFriend(context,data, callback));
    }

    /**
     * 获取 好友请求和用户详情
     */
    public void getFriendRequestAndUserDetail(Context context) {
        addTask(context, new TaskGetFriendRequestAndUserDetail(context, null));
    }

    /**
     * 获取 所有字典
     */
    public void getAllDict() {
        addTask(null, new TaskGetAllDict(null, null));
    }
}
