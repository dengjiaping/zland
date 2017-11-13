package com.zhisland.android.blog.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.aa.dto.CustomShare;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.ShareDialog;
import com.zhisland.android.blog.common.view.ShareDialog.OnShareActionClick;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.event.dto.Event;
import com.zhisland.android.blog.feed.bean.Attach;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.feed.view.impl.FragShareFeed;
import com.zhisland.android.blog.freshtask.bean.TaskCommentDetail;
import com.zhisland.android.blog.freshtask.bean.TaskStatus;
import com.zhisland.android.blog.freshtask.eb.BusFreshTask;
import com.zhisland.android.blog.freshtask.eb.EventFreshTask;
import com.zhisland.android.blog.freshtask.view.impl.FragFriendAdd;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.profile.controller.FragNoPermissions;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.android.blog.wxapi.Share;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.dialog.AProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogUtil {

    // 活动报名成功分享
    public static final int FROM_EVENT_SIGN_OK = 0;
    // 活动创建成功分享
    public static final int FROM_EVENT_CREATE = 1;
    // 活动详情分享
    public static final int FROM_EVENT_DETAIL = 2;
    //邀请须知
    private static final String KEY_INVITE_NOTICE = "key_invite_notice"
            + PrefUtil.Instance().getUserId();

    private static DialogUtil dialogUtil;
    private static Object sycObj = new Object();

    private Dialog shareDialog;
    // 上一次发起相同toast的时间
    private static HashMap<String, Long> mLastToastTimeMap;
    // 维护记录上一次发起相同时间的toast的map
    private static int LAST_TOAST_COLLECTION_SIZE = 10;
    //是否有邀请的 cach
    public static String CACH_INVITE_SHARE = "cach_invite_share";

    private DialogUtil() {
    }

    public static DialogUtil getInstatnce() {
        synchronized (sycObj) {
            if (dialogUtil == null) {
                dialogUtil = new DialogUtil();
            }
        }
        return dialogUtil;
    }

    public static AProgressDialog createProgressDialog(Context context) {
        AProgressDialog dlg = new AProgressDialog(context);
        return dlg;
    }

    /**
     * 创建由底部弹出的ActionSheet
     *
     * @param title       标题
     * @param cancelTitle 取消按钮
     * @param otherBtns   所有其它的按钮
     * @param actionClick 点击事件
     */
    public static Dialog createActionSheet(Context ctx, String title,
                                           String cancelTitle, List<ActionItem> otherBtns,
                                           OnActionClick actionClick) {
        ActionDialog actionDlg = new ActionDialog(ctx, R.style.ActionDialog,
                title, cancelTitle, otherBtns, actionClick);
        WindowManager.LayoutParams wmlp = actionDlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        return actionDlg;
    }

    private static Dialog createShareDialog(Context ctx, String title,
                                            String cancelTitle, List<ActionItem> actionItems, int numColumns,
                                            OnShareActionClick actionClick) {
        ShareDialog actionDlg = new ShareDialog(ctx, R.style.ActionDialog,
                actionItems, numColumns, actionClick);
        actionDlg.setTitle(title);
        actionDlg.setCancel(cancelTitle);
        WindowManager.LayoutParams wmlp = actionDlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        return actionDlg;
    }

    /**
     * 个人分享Dialog
     */
    public void showProfileShareDialog(final Context context,
                                       final UserDetail userDetail, final String pageName) {
        if (shareDialog != null && shareDialog.isShowing()) {
            return;
        }
        if (userDetail == null) {
            return;
        }
        User user = userDetail.user;
        if (user == null) {
            return;
        }
        final boolean isInstallWechat = WechatUtil.getInstance()
                .isInstallWechat(context);
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        actions.add(new ActionItem(1, "微信好友",
                isInstallWechat ? R.drawable.img_wechat
                        : R.drawable.img_wechat_disable));
        actions.add(new ActionItem(2, "朋友圈",
                isInstallWechat ? R.drawable.img_wechat_circle
                        : R.drawable.img_wechat_circle_disabled));

        CustomShare customShare = userDetail.share;
        final Share shareWC = new Share();
        shareWC.webpageUrl = customShare.url;
        shareWC.iconUrl = user.userAvatar;
        shareWC.title = customShare.title;
        shareWC.description = customShare.desc;

        shareDialog = createShareDialog(context, "分享到", "取消", actions,
                actions.size(), new OnShareActionClick() {

                    @Override
                    public void onClick(int id, ActionItem item) {
                        shareDialog.dismiss();
                        switch (id) {
                            case 1:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT, shareWC);
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.USER + TrackerAlias.WX_FRIEND);
                                }
                                break;
                            case 2:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT_MOM,
                                            shareWC);
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.USER + TrackerAlias.WX_TIME_LINE);
                                }
                                break;
                        }
                    }
                });
        shareDialog.show();
    }

    /**
     * 活动分享Dialog
     */
    public void showEventDialog(final Context context, final Event event,
                                final int from, final String pageName) {
        if (shareDialog != null && shareDialog.isShowing()) {
            return;
        }
        ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_SHARE);
        final boolean isInstallWechat = WechatUtil.getInstance()
                .isInstallWechat(context);
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        actions.add(new ActionItem(5, "正和岛商圈",
                R.drawable.img_share_zhisland));
        actions.add(new ActionItem(10, "微信好友",
                isInstallWechat ? R.drawable.img_wechat
                        : R.drawable.img_wechat_disable));
        actions.add(new ActionItem(20, "朋友圈",
                isInstallWechat ? R.drawable.img_wechat_circle
                        : R.drawable.img_wechat_circle_disabled));
        actions.add(new ActionItem(30, "复制链接", R.drawable.img_link));

        shareDialog = createShareDialog(context, "分享到", "取消", actions, 3,
                new OnShareActionClick() {

                    @Override
                    public void onClick(int id, ActionItem item) {
                        shareDialog.dismiss();
                        switch (id) {
                            case 5: {
                                Feed feed = new Feed();
                                feed.type = FeedType.EVENT;
                                Attach attach = new Attach();
                                feed.attach = attach;
                                attach.uri = AUriMgr.instance().getUriString(event.eventId, AUriMgr.PATH_EVENTS);
                                attach.title = event.eventTitle;
                                attach.info = event.period + " " + event.cityName;
                                FragShareFeed.Invoke(context, feed, event.eventId);
                            }
                            break;
                            case 10:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT,
                                            createEventShareWc(event, from));
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.EVENT + TrackerAlias.WX_FRIEND);
                                }
                                break;
                            case 20:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT_MOM,
                                            createEventShareWc(event, from));
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.EVENT + TrackerAlias.WX_TIME_LINE);
                                }
                                break;
                            case 30:
                                ToastUtil.showShort("已复制到剪贴板");
                                StringUtil.copyToClipboard(context, event.shareUrl);
                                break;
                        }
                    }
                });
        shareDialog.show();
    }

    /**
     * 活动发布成功分享dialog
     */
    public void showCreateEventSuccessDialog(final Context context,
                                             final Event event) {
        if (event == null) {
            return;
        }

        final boolean isInstallWechat = WechatUtil.getInstance()
                .isInstallWechat(context);

        final Dialog dialog = new Dialog(context, R.style.DialogGuest);
        dialog.setContentView(R.layout.dlg_create_event_success);
        dialog.setCancelable(true);

        TextView tvShareWeChat = (TextView) dialog
                .findViewById(R.id.tvShareWeChat);
        TextView tvShareWeChatCircle = (TextView) dialog
                .findViewById(R.id.tvShareWeChatCircle);

        tvShareWeChat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isInstallWechat) {
                    WechatUtil.getInstance().wechatShare(context,
                            WechatUtil.SHARE_TO_WE_CHAT,
                            createEventShareWc(event, FROM_EVENT_CREATE));
                }
            }
        });

        tvShareWeChatCircle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isInstallWechat) {
                    WechatUtil.getInstance().wechatShare(context,
                            WechatUtil.SHARE_TO_WE_CHAT_MOM,
                            createEventShareWc(event, FROM_EVENT_CREATE));
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    /**
     * 活动详情页，第一次点“喜欢此活动”button时弹出的对话框
     */
    public static Dialog createEventEnjoyDialog(Context context) {
        View enJoyEventView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_enjoy_event_layout, null);
        final Dialog enjoyDialog = new Dialog(context, R.style.UpdateDialog);
        TextView tv_ok = (TextView) enJoyEventView.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != enjoyDialog && enjoyDialog.isShowing()) {
                    enjoyDialog.dismiss();
                }
            }
        });
        enjoyDialog.setContentView(enJoyEventView);
        enjoyDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = enjoyDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL;
        wmlp.height = DensityUtil.dip2px(340);
        wmlp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
        enjoyDialog.show();
        return enjoyDialog;
    }

    /**
     * 第一次升级为海客Dialog
     */
    public static Dialog dialogBecomeGuest(Context context) {
        ZhislandApplication.trackerPageStart(TrackerAlias.PAGE_UPGRADE_HAIKE_DESCRIPTION);
        View guestView = LayoutInflater.from(context).inflate(R.layout.dlg_become_guest, null);
        final Dialog dialog = new Dialog(context, R.style.UpdateDialog);
        TextView tvOk = (TextView) guestView.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog && dialog.isShowing()) {
                    ZhislandApplication.trackerClickEvent(TrackerAlias.PAGE_UPGRADE_HAIKE_DESCRIPTION, TrackerAlias.CLICK_UPGRADE_HAIKE_DESCRIPTION_CONFIRM);
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(guestView);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL;
        wmlp.height = DensityUtil.dip2px(320);
        wmlp.width = DensityUtil.getWidth() - DensityUtil.dip2px(64); // 宽度
        dialog.show();
        return dialog;
    }


    /**
     * 活动分享 Share对象
     */
    private Share createEventShareWc(Event event, int from) {
        Share shareWC = new Share();

        User user = DBMgr.getMgr().getUserDao().getSelfUser();
        shareWC.webpageUrl = event.shareUrl;
        shareWC.iconUrl = event.getEventImgUrl();
        shareWC.title = event.eventTitle;

        switch (from) {
            case FROM_EVENT_SIGN_OK:
                shareWC.description = "我是你的好友" + user.name + "（" + user.userCompany + "-"
                        + user.userPosition + "）" + "，快来与我一起参加这个活动吧。";
                break;
            case FROM_EVENT_CREATE:
                shareWC.description = "我是你的好友" + user.name + "（" + user.userCompany + "-"
                        + user.userPosition + "）" + "，邀请你来参加我发布的这个活动吧。";
                break;
            case FROM_EVENT_DETAIL:
                shareWC.description = "我是你的好友" + user.name + "（" + user.userCompany + "-"
                        + user.userPosition + "）" + "，快来与我一起参加这个活动吧。";
                break;
        }

        return shareWC;
    }

    /**
     * 资讯分享Dialog
     */
    public void showInfoDialog(final Context context, final ZHInfo info, final String pageName) {
        if (shareDialog != null && shareDialog.isShowing()) {
            return;
        }
        final boolean isInstallWechat = WechatUtil.getInstance()
                .isInstallWechat(context);
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        actions.add(new ActionItem(5, "正和岛商圈",
                R.drawable.img_share_zhisland));
        actions.add(new ActionItem(10, "微信好友",
                isInstallWechat ? R.drawable.img_wechat
                        : R.drawable.img_wechat_disable));
        actions.add(new ActionItem(20, "朋友圈",
                isInstallWechat ? R.drawable.img_wechat_circle
                        : R.drawable.img_wechat_circle_disabled));
        actions.add(new ActionItem(30, "复制链接", R.drawable.img_link));

        final Share shareWC = new Share();
        shareWC.webpageUrl = info.infoShareUrl;
        if (!StringUtil.isNullOrEmpty(info.coverSmall)) {
            shareWC.iconUrl = info.coverSmall;
        } else if (!StringUtil.isNullOrEmpty(info.coverLarge)) {
            shareWC.iconUrl = info.coverLarge;
        }
        shareWC.title = info.title;
        shareWC.description = info.summary;

        shareDialog = createShareDialog(context, "分享到", "取消", actions, 3,
                new OnShareActionClick() {

                    @Override
                    public void onClick(int id, ActionItem item) {
                        shareDialog.dismiss();
                        switch (id) {
                            case 5: {
                                Feed feed = new Feed();
                                feed.type = FeedType.INFO;
                                Attach attach = new Attach();
                                feed.attach = attach;
                                attach.uri = AUriMgr.instance().getUriString(info.newsId, AUriMgr.PATH_NEWS);
                                attach.title = info.title;
                                attach.info = info.resourceFrom;
                                FragShareFeed.Invoke(context, feed, info.newsId);
                            }
                            break;
                            case 10:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT, shareWC);
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.INFO + TrackerAlias.WX_FRIEND);
                                }
                                break;
                            case 20:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT_MOM,
                                            shareWC);
                                    ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.INFO + TrackerAlias.WX_TIME_LINE);
                                }
                                break;
                            case 30:
                                ToastUtil.showShort("已复制到剪贴板");
                                StringUtil.copyToClipboard(context, info.infoShareUrl);
                                break;
                        }
                    }
                });
        shareDialog.show();
    }

    private void cachImage(Context context, String imgUrl) {
        //将图片缓存到image cach中
        ImageView imageView = new ImageView(context);
        ImageWorkFactory.getFetcher().loadImage(imgUrl, imageView);
    }

    /**
     * 输入Toast的key值和内容，可以对这个key对应的toast避免重复弹出
     */
    public static void singleToast(Context context, String key, String content) {
        long currentToastTime = System.currentTimeMillis();
        if (mLastToastTimeMap == null) {
            mLastToastTimeMap = new HashMap<String, Long>();
        }
        if (!mLastToastTimeMap.containsKey(key)) {
            if (mLastToastTimeMap.size() >= LAST_TOAST_COLLECTION_SIZE)
                for (String k : mLastToastTimeMap.keySet()) {
                    if (currentToastTime - mLastToastTimeMap.get(k) > 3000) {
                        mLastToastTimeMap.remove(k);
                        break;
                    }
                }
            mLastToastTimeMap.put(key, -1L);
        }
        if (currentToastTime - mLastToastTimeMap.get(key) < 3000) {
            return;
        }
        ToastUtil.showShort(content);
    }

    /**
     * 来源于微信 活动报名人展示dialog
     */
    public static void showWXEventSignedDialog(Context context, User user) {
        if (user == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, R.style.DialogGuest);
        dialog.setContentView(R.layout.dlg_wx_event_signed);
        dialog.setCancelable(true);

        LinearLayout llRooView = (LinearLayout) dialog
                .findViewById(R.id.llRooView);
        ImageView ivUserAvatar = (ImageView) dialog
                .findViewById(R.id.ivUserAvatar);
        TextView tvUserName = (TextView) dialog.findViewById(R.id.tvUserName);
        TextView tvUserPostion = (TextView) dialog
                .findViewById(R.id.tvUserPostion);
        ImageWorkFactory.getCircleFetcher().loadImage(user.userAvatar,
                ivUserAvatar, R.drawable.img_profile_default_weichat);
        tvUserName.setText(user.name);
        tvUserPostion.setText(user.userCompany + " " + user.userPosition);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    /**
     * 显示一个基本对话框，左为cancel， 右为ok</br> leftListener为空 cancel不显示</br>
     * rightListener为空 ok不显示</br>
     */
    public static void showDialog(Context context, String title,
                                  String content, final OnCommonDialogBtnListener leftListener,
                                  final OnCommonDialogBtnListener rightListener) {
        showDialog(context, title, content, null, null, leftListener,
                rightListener);
    }

    /**
     * 显示一个基本对话框，左为cancel， 右为ok</br> leftListener为空 cancel不显示</br>
     * rightListener为空 ok不显示</br>
     */
    public static void showDialog(Context context, String title,
                                  String content, String cancel, String ok,
                                  final OnCommonDialogBtnListener leftListener,
                                  final OnCommonDialogBtnListener rightListener) {
        final CommonDialog dialog = new CommonDialog(context);
        if (!dialog.isShowing()) {
            dialog.show();
            if (!StringUtil.isNullOrEmpty(title)) {
                dialog.setTitle(title);
            }
            if (!StringUtil.isNullOrEmpty(content)) {
                dialog.setContent(content);
            }
            if (leftListener == null) {
                dialog.setLeftBtnGone();
            }
            if (rightListener == null) {
                dialog.setRightBtnGone();
            }
            if (ok != null)
                dialog.tvDlgConfirm.setText(ok);
            if (cancel != null) {
                dialog.tvDlgCancel.setText(cancel);
            } else {
                dialog.tvDlgCancel.setText("取消");
            }
            dialog.tvDlgCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    leftListener.onClick(dialog);
                }
            });
            dialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    rightListener.onClick(dialog);
                }
            });
        }

    }

    /**
     * 邀请好友为我写神评
     */
    public void showInviteFriendComment(final Context context, TaskCommentDetail data) {
        if (shareDialog != null && shareDialog.isShowing()) {
            return;
        }
        final boolean isInstallWechat = WechatUtil.getInstance()
                .isInstallWechat(context);
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        actions.add(new ActionItem(1, "微信好友",
                isInstallWechat ? R.drawable.img_wechat
                        : R.drawable.img_wechat_disable));
        actions.add(new ActionItem(2, "朋友圈",
                isInstallWechat ? R.drawable.img_wechat_circle
                        : R.drawable.img_wechat_circle_disabled));

        final Share shareWC = new Share();
        shareWC.title = data.shareTitle;
        shareWC.description = data.shareDesc;
        shareWC.webpageUrl = data.shareLinkUrl;
        shareWC.iconUrl = PrefUtil.Instance().getUserAvatar();
        cachImage(context, shareWC.iconUrl);

        shareDialog = createShareDialog(context, "发送至", "取消", actions, 2,
                new OnShareActionClick() {

                    @Override
                    public void onClick(int id, ActionItem item) {
                        shareDialog.dismiss();
                        switch (id) {
                            case 1:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT, shareWC);
                                }
                                break;
                            case 2:
                                if (isInstallWechat) {
                                    WechatUtil.getInstance().wechatShare(context,
                                            WechatUtil.SHARE_TO_WE_CHAT_MOM, shareWC);
                                }
                                break;
                        }
                    }
                });
        shareDialog.show();
    }

    public interface OnCommonDialogBtnListener {
        public void onClick(CommonDialog dialog);
    }

    /**
     * 显示用户权益dialog
     */
    public static void showPermissionsDialog(final Context context, final String pageName) {
        FragNoPermissions.invoke(context, pageName);
    }

    /**
     * 继续注册dialog
     */
    public void showContinueRegisterDialog(final Activity activity, String title, String content, String leftStr, String rightStr) {
        CommonDialog commonDialog = new CommonDialog(activity);
        commonDialog.show();
        commonDialog.setTitle(title);
        commonDialog.setContent(content);
        commonDialog.setLeftBtnContent(leftStr);
        commonDialog.setRightBtnContent(rightStr);
        commonDialog.tvDlgCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActGuide.invoke(activity);
                activity.finish();
            }
        });
    }

    /**
     * 新手任务，通讯录弹窗dialog
     */
    public static boolean showFreshTaskContactDlg(final Context context) {
        PrefUtil.Instance().setShowTaskContact();
        final Dialog dialog = new Dialog(context, R.style.DialogGuest);
        dialog.setContentView(R.layout.dlg_fresh_task_contact);
        dialog.setCancelable(false);
        final TextView tvVisitContact = (TextView) dialog
                .findViewById(R.id.tvVisitContact);
        TextView tvNotAddFriend = (TextView) dialog.findViewById(R.id.tvNotAddFriend);
        tvVisitContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    FragFriendAdd.invoke(context);
                }
            }
        });
        tvNotAddFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    BusFreshTask.Bus().post(new EventFreshTask(EventFreshTask.TYPE_SWITCH_TO_NEXT_CARD, TaskStatus.NORMAL));
                }
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        dialog.show();
        return false;
    }

    /**
     * 新手任务，标题引导dialog
     */
    public static void showFreshTaskTitleMask(final Context context) {
        User selfUser = DBMgr.getMgr().getUserDao().getSelfUser();
        if (selfUser != null && !PrefUtil.Instance().getIsShowFreshTaskMask()) {
            PrefUtil.Instance().setIsShowFreshTaskMask();
            final Dialog dialog = new Dialog(context, R.style.DIALOG_FRESH_TASK_MASK);
            dialog.setContentView(R.layout.dlg_fresh_task_title);
            dialog.setCancelable(true);
            ImageView ivFreshTaskTitle = (ImageView) dialog.findViewById(R.id.ivFreshTaskTitle);
            ivFreshTaskTitle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.TOP | Gravity.LEFT);

            dialog.show();
        }
    }
}
