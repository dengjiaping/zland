package com.zhisland.android.blog.common.push;

/**
 * 1-100 系统;101-200 活动;201-300 IM
 */
public class NotifyTypeConstants {

    /**
     * 是否有我作为活动发起人的通知
     */
    public static final String PREF_MY_EVENT = "my_event";
    /**
     * 是否有我报名活动的通知
     */
    public static final String PREF_SIGN_EVENT = "sign_event";

    // 作为报名者接受的通知
    /**
     * 活动报名审核通过
     */
    public static final int EventAuditSuccess = 101;
    /**
     * 活动报名审核未通过
     */
    public static final int EventAuditFail = 102;
    /**
     * 活动详情被修改
     */
    public static final int EventModify = 103;
    /**
     * 活动被官方强制下线，发送给报名人
     */
    public static final int EventCancelByOfficialToSigner = 104;
    /**
     * 活动被发起人取消
     */
    public static final int EventCancelByOrganizer = 105;

    // 作为发布者会受到的通知
    /**
     * 活动有人报名
     */
    public static final int EventApplicant = 120;
    /**
     * 活动被官方强制下线，发送给发起人
     */
    public static final int EventCancelByOfficialToOrganizer = 121;

    /**
     * 报名的活动列表被查看，非NotifyType。用于EventBus，并非通知类型，提前占用199，以后定义通知类型不可用199
     */
    public static final int EventBusEventSignList = 199;

    /**
     * 发起的活动列表被查看，非NotifyType。用于EventBus，并非通知类型，提前占用198，以后定义通知类型不可用198
     */
    public static final int EventBusEventInitiatedList = 198;

    /**
     * IM消息
     */
    public static final int IMMessage = 201;
    /**
     * IM加好友
     */
    public static final int IMRelation = 202;

    /**
     * 用户求邀请，发送给被求人
     */
    public static final int INVITATION_REQUEST = 5;

    /**
     * 收到对自己的评论
     */
    public static final int COMMENT_RECEIVED = 10;

    /**
     * 新手任务在别的端改变
     */
    public static final int FRESH_TASK_FINISH = 11;

    /**
     * 与邀请的用户成为好友
     */
    public static final int INVITE_SUCCESS_TO_FRIEND = 98;

    /**
     * 通用推送
     */
    public static final int COMMON_PUSH = 15;

    /**
     * 是否为活动的通知， 100 -200 之间为活动的通知
     */
    public static boolean isEventNotify(int notifyId) {
        return notifyId > 100 && notifyId < 200;
    }
}