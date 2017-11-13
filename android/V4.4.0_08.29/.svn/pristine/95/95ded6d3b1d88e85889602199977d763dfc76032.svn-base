package com.zhisland.android.blog.im.controller;


import android.util.Log;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.im.dto.MessageCount;
import com.zhisland.android.blog.im.eb.EBMessage;
import com.zhisland.android.blog.tabhome.eb.EBTabHome;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.rxjava.RxBus;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * 新增粉丝、互动消息、系统通知
 */
public class MessageLooping {

    private static MessageLooping INSTANCE;
    // 轮询间隔
    private static final int TIME_INTERVAL = 3 * 60 * 1000;

    private Subscription subscribe;

    private MessageLooping() {

    }

    public static MessageLooping getInstance() {
        if (INSTANCE == null) {
            synchronized (MessageLooping.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageLooping();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 开始轮询获取
     */
    public void startLooping() {
        getMessageCountTask(true);
        subscribe = Observable.interval(TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long number) {
                        getMessageCountTask(true);
                    }
                });
    }

    /**
     * 停止轮询
     */
    public void stopLooping() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    /**
     * 获取消息数 task
     *
     * @param showTabRedDot 是否显示tabbar上的红点
     */
    public void getMessageCountTask(final boolean showTabRedDot) {
        ZHApis.getCommonApi().getMessageCount(null, new TaskCallback<MessageCount>() {
            @Override
            public void onSuccess(MessageCount content) {
                Log.e("hui", "success ...");
                // 消息tab红点逻辑
                if (content.isMsgCountChange() && showTabRedDot) {
                    EventBus.getDefault().post(
                            new EBTabHome(EBTabHome.TYPE_TAB_RED_DOT, TabHome.TAB_ID_IM));
                }
                PrefUtil.Instance().setNewlyAddedFansCount(content.getFansCount());
                PrefUtil.Instance().setInteractiveCount(content.getInteractiveCount());
                PrefUtil.Instance().setSystemMsgCount(content.getSystemCount());
                // 发送推送 修改消息数
                RxBus.getDefault().post(new EBMessage(EBMessage.TYPE_MESSAGE_REFRESH, null));
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e("hui", "fail ....");
            }
        });
    }

}
