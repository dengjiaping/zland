package com.zhisland.android.blog.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.wxapi.Share;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileUtil;

/**
 * 微信分享Util
 */
public class WechatUtil {

    public static String WE_CHAT_APP_ID;

    /**
     * 微信朋友
     */
    public static final int SHARE_TO_WE_CHAT = 0;
    /**
     * 微信朋友圈
     */
    public static final int SHARE_TO_WE_CHAT_MOM = 1;

    public static WechatUtil wechatUtil;
    private IWXAPI wxApi;

    private WechatUtil() {
    }

    public static WechatUtil getInstance() {
        if (wechatUtil == null) {
            wechatUtil = new WechatUtil();
        }
        return wechatUtil;
    }

    private void registerWxApi(Context context) {
        if (StringUtil.isNullOrEmpty(WE_CHAT_APP_ID)) {
            WE_CHAT_APP_ID = context.getString(R.string.wechat_release);
        }
        wxApi = WXAPIFactory.createWXAPI(context, WE_CHAT_APP_ID, true);
        wxApi.registerApp(WE_CHAT_APP_ID);
    }

    /**
     * 判断是否支持手机是否装微信APP和支持微信分享功能
     */
    public boolean isInstallWechat(Context context) {
        if (wxApi == null) {
            registerWxApi(context);
        }
        boolean isCanShare = wxApi.isWXAppInstalled()
                && wxApi.isWXAppSupportAPI();
        if (!isCanShare) {
            ToastUtil.showShort("系统检测到您的手机没有安装微信或者版本较低不支持分享！");
        }
        return isCanShare;
    }

    /**
     * 微信好友 / 朋友圈分享
     *
     * @param context
     * @param flag    SHARE_TO_WE_CHAT（微信好友）、 SHARE_TO_WE_CHAT_MOM（微信朋友圈）
     * @param share
     */
    public void wechatShare(Context context, int flag, Share share) {
        if (share == null) {
            return;
        }
        if (wxApi == null) {
            registerWxApi(context);
        }

        WXMediaMessage msg = new WXMediaMessage();
        if (StringUtil.isNullOrEmpty(share.webpageUrl)) {
            WXTextObject textObject = new WXTextObject();
            msg.mediaObject = textObject;
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = share.webpageUrl;
            msg.mediaObject = webpage;
        }
        msg.title = share.title;
        msg.description = share.description;
        Bitmap bitmap = null;
        if (!StringUtil.isNullOrEmpty(share.iconUrl)) {
            bitmap = ImageWorkFactory.getFetcher().getBitmap(share.iconUrl);
            if (bitmap != null) {
                String filePath = FileUtil.saveBitmapToSDCard(bitmap, share.iconUrl);
                msg.setThumbImage(BitmapUtil.getWechatThumbBitmap(filePath, 150, 150, true));
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon_wechat_share);
            msg.setThumbImage(bitmap);
        }

        Req req = new Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == SHARE_TO_WE_CHAT ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;

        wxApi.sendReq(req);
    }

    /**
     * 微信支付
     */
    public void wechatPay(Context context, PayReq content) {
        if (wxApi == null) {
            registerWxApi(context);
        }
        boolean wx = wxApi.sendReq(content);
        if (!wx) {
            ToastUtil.showShort("启动微信失败。");
        }
    }

    /**
     * 判断微信版本是否支持支付功能
     * */
    public boolean isPaySupported(Context context) {
        if (wxApi == null) {
            registerWxApi(context);
        }
        return wxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    /**
     * 跳转到微信
     *
     * @param context
     * @return
     */
    public boolean launchWX(Context context) {
        if (wxApi == null) {
            registerWxApi(context);
        }
        return wxApi.openWXApp();
    }
}
