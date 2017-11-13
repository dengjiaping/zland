package com.zhisland.android.blog.common.push;

import java.util.Map;

import com.zhisland.android.blog.contacts.push.InvitationRequestHandler;
import com.zhisland.android.blog.contacts.push.InviteSuccessHandler;
import com.zhisland.android.blog.event.push.EventApplicantHandler;
import com.zhisland.android.blog.event.push.EventAuditFailHandler;
import com.zhisland.android.blog.event.push.EventAuditSuccessHandler;
import com.zhisland.android.blog.event.push.EventCancelByOfficialToOrganizerHandler;
import com.zhisland.android.blog.event.push.EventCancelByOfficialToSignerHandler;
import com.zhisland.android.blog.event.push.EventCancelByOrganizerHandler;
import com.zhisland.android.blog.event.push.EventModifyHandler;
import com.zhisland.android.blog.freshtask.push.FreshTaskHandler;
import com.zhisland.android.blog.im.push.IMMessageHandler;
import com.zhisland.android.blog.im.push.IMRelationHandler;
import com.zhisland.android.blog.profile.push.CommentReceivedHandler;
import com.zhisland.lib.util.MLog;

import android.util.SparseArray;

/**
 * push 管理类
 */
public class PushMgr {

    private static final String TAG = "PushMgr";
    private static PushMgr pushMgr;

    private SparseArray<BasePushHandler> handlers;

    public static PushMgr getInstance() {
        if (pushMgr == null) {
            synchronized (PushMgr.class) {
                if (pushMgr == null) {
                    pushMgr = new PushMgr();
                }
            }
        }
        return pushMgr;
    }

    private PushMgr() {
        registerHandlers();
    }

    /**
     * 注册push handler。 如有新的push类型，只需在这个方法中添加新Handler即可
     */
    private void registerHandlers() {
        if (handlers == null) {
            handlers = new SparseArray<>();
            try {
                handlers.put(NotifyTypeConstants.EventAuditSuccess,
                        EventAuditSuccessHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventAuditFail,
                        EventAuditFailHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventModify,
                        EventModifyHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventCancelByOfficialToSigner,
                        EventCancelByOfficialToSignerHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventCancelByOrganizer,
                        EventCancelByOrganizerHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventApplicant,
                        EventApplicantHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.EventCancelByOfficialToOrganizer,
                        EventCancelByOfficialToOrganizerHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.IMMessage,
                        IMMessageHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.IMRelation,
                        IMRelationHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.INVITATION_REQUEST,
                        InvitationRequestHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.COMMENT_RECEIVED,
                        CommentReceivedHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.INVITE_SUCCESS_TO_FRIEND,
                        InviteSuccessHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.FRESH_TASK_FINISH,
                        FreshTaskHandler.class.newInstance());
                handlers.put(NotifyTypeConstants.COMMON_PUSH,
                        CommonPushHandler.class.newInstance());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据push type进行push分发
     */
    public void dispatchPush(Map<String, String> maps) {
        if (!maps.containsKey(BasePushHandler.TYPE_KEY))
            return;

        int notifyType = -1;
        try {
            notifyType = Integer.parseInt(maps.get(BasePushHandler.TYPE_KEY));
        } catch (Exception ex) {
            MLog.d(TAG, "个推出错" + ex.getMessage());
        }
        if (notifyType < 0)
            return;

        BasePushHandler baseHandler = handlers.get(notifyType);
        if (baseHandler != null) {
            baseHandler.onReceive(maps);
        }
    }

}
