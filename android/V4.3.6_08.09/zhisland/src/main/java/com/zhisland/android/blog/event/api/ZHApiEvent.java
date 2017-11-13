package com.zhisland.android.blog.event.api;

import android.content.Context;

import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.event.dto.EventListStruct;
import com.zhisland.android.blog.event.dto.EventSpread;
import com.zhisland.android.blog.event.dto.PayData;
import com.zhisland.android.blog.event.dto.PayRequest;
import com.zhisland.android.blog.event.dto.SceneNotify;
import com.zhisland.android.blog.event.dto.VoteTo;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.ArrayList;
import java.util.List;

public class ZHApiEvent extends ApiBase {


    private static class Holder {
        private static ZHApiEvent INSTANCE = new ZHApiEvent();
    }

    public static ZHApiEvent Instance() {
        return Holder.INSTANCE;
    }

    private ZHApiEvent() {

    }

    /**
     * 获取活动详情
     */
    public void getEventDetail(Context context, long eventId, TaskCallback<Event> callback) {
        addTask(context, new TaskGetEventDetail(context, eventId, callback));
    }

    /**
     * 获取活动list
     */
    public void getEventList(Context context, long categoryId, String curCategoryName, String orderId,
                             String nextId, TaskCallback<EventListStruct> callback) {
        addTask(context, new TaskGetEventList(context, categoryId,
                curCategoryName, orderId, nextId, callback));
    }

    /**
     * 获取往期活动list
     */
    public void getPastEventList(Context context, long categoryId, String curCategoryName,
                                 String nextId, TaskCallback<EventListStruct> callback) {
        addTask(context, new TaskGetPastEventList(context, categoryId,
                curCategoryName, nextId, callback));
    }

    /**
     * 获取推广活动
     */
    public void getEventSpread(Context context, TaskCallback<ArrayList<EventSpread>> callback) {
        addTask(context, new TaskGetEventSpread(context, callback));
    }

    /**
     * 获取现场模式中某个活动的通知list
     */
    public void getSceneNotifyList(Context context, long eventId, String nextId,
                                   TaskCallback<ZHPageData<SceneNotify>> callback) {
        addTask(context, new TaskGetSceneNotifyList(context, eventId, nextId,
                callback));
    }

    /**
     * 报名
     */
    public void signUp(Context context, long eventId, String userMobile, String choice, Integer invoiceOption, String invoiceContacts,
                       TaskCallback<PayData> callback) {
        addTask(context, new TaskSignUp(context, eventId, userMobile, choice, invoiceOption, invoiceContacts,
                callback));
    }

    /**
     * 取消报名
     */
    public void signCancel(Context context, long eventId, TaskCallback<PayData> callback) {
        addTask(context, new TaskSignCancel(context, eventId, callback));
    }

    /**
     * 审核通过
     */
    public void Approved(Context context, long eventId, long userId,
                         TaskCallback<Object> callback) {
        addTask(context, new TaskSignApproved(context, eventId, userId,
                callback));
    }

    /**
     * 获取报名人员列表
     */
    public void getSignedMembers(Context context, long eventId, String nextId,
                                 TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetMembersSigned(context, eventId, nextId,
                callback));
    }

    /**
     * 获取标签列表
     */
    public void getTagList(Context context, int tagType, TaskCallback<ArrayList<String>> callback) {
        addTask(context, new TaskGetTagList(context, tagType, callback));
    }

    /**
     * 获取我发布的活动列表
     */
    public void getInitiatedEvents(Context context, String nextId,
                                   TaskCallback<ZHPageData<Event>> callback) {
        addTask(context, new TaskGetInitiatedEvents(context, nextId, callback));
    }

    /**
     * 取消活动
     */
    public void cancelEvent(Context context, long eventId, String cancelReason,
                            TaskCallback<Object> callback) {
        addTask(context, new TaskEventCancel(context, eventId, cancelReason,
                callback));
    }

    /**
     * 获取我报名的活动列表
     */
    public void getSignUpEvents(Context context, String nextId,
                                TaskCallback<ZHPageData<Event>> callback) {
        addTask(context, new TaskGetSignUpEvents(context, nextId, callback));
    }

    /**
     * 获取报名审核中的人员列表
     */
    public void getAuditingMembers(Context context, long eventId, String nextId,
                                   TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetMembersAuditing(context, eventId, nextId,
                callback));
    }

    /**
     * 获取报名审核通过的人员列表
     */
    public void getAuditedMembers(Context context, long eventId, String nextId,
                                  TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskGetMembersAudited(context, eventId, nextId,
                callback));
    }

    /**
     * 发布活动
     */
    public void createEvent(Context context, Event event, TaskCallback<Event> callback) {
        addTask(context, new TaskEventCreate(context, event, callback));
    }

    /**
     * 修改活动
     */
    public void updateEvent(Context context, Event event, TaskCallback<Object> callback) {
        addTask(context, new TaskEventUpdate(context, event, callback));
    }

    /**
     * 现场模式签到
     */
    public void sceneSignIn(Context context, long eventId, TaskCallback<Object> callback) {
        addTask(context, new TaskSceneSign(context, eventId, callback));
    }

    /**
     * 现场活动想认识我的人
     */
    public void getFriendRequest(Context context, long eventId,
                                 TaskCallback<ArrayList<User>> callback) {
        addTask(context, new TaskGetFriendRequest(context, eventId, callback));
    }

    /**
     * 现场活动获取用户总数
     *
     * @param eventId
     * @param callback
     */
    public void getUserCount(Context context, long eventId, TaskCallback<Integer> callback) {
        addTask(context,
                new TaskGetUserCountInScene(context, eventId, callback));
    }

    /**
     * 现场活动想认识我的人
     */
    public void getSignInUser(Context context, long eventId, String nextId,
                              TaskCallback<ZHPageData<User>> callback) {
        addTask(context, new TaskSceneSignInUser(context, eventId, nextId,
                callback));
    }

    /**
     * 获取当前现场活动
     */
    public void getEventScene(Context context, TaskCallback<Event> callback) {
        addTask(context, new TaskGetEventScene(context, callback));
    }

    /**
     * 保存活动推广内容
     */
    public void saveExtension(Context context, long eventId, String content,
                              TaskCallback<Object> callback) {
        addTask(context, new TaskSpreadEvent(context, eventId, content,
                callback));
    }

    /**
     * 获取活动日程
     */
    public void getEventVote(Context context, long eventId,
                             TaskCallback<ArrayList<VoteTo>> callback) {
        addTask(context, new TaskGetEventVoteTo(context, eventId, callback));
    }

    /**
     * 喜欢活动
     */
    public void postLikeStatus(Context context, long eventId,
                               TaskCallback<Object> callback) {
        addTask(context, new TaskLikeStatus(context, eventId, callback));
    }

    /**
     * 获取微信支付请求信息
     */
    public void getWXPayReq(Context context, long id, TaskCallback<PayRequest> callback) {
        addTask(context, new TaskGetWXPayReq(context, id, callback));
    }

    /**
     * 获取支付结果
     */
    public void getPayRes(Context context, long id, TaskCallback<PayData> callback) {
        addTask(context, new TaskGetPayRes(context, id, callback));
    }
}
