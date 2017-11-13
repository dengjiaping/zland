package com.zhisland.android.blog.common.util;

import android.app.Activity;

import java.net.ConnectException;
import java.util.ArrayList;

import org.apache.http.client.HttpResponseException;

import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 服务器异常状态码处理
 */
public class ZHExceptionUtil {

    private static ZHExceptionUtil util;

    private ArrayList<Integer> noPromptCodes;

    private ZHExceptionUtil() {
    }

    public static ZHExceptionUtil getInstance() {
        if (util == null) {
            util = new ZHExceptionUtil();
        }
        return util;
    }

    /**
     * 所有接口都要处理的状态码
     */
    public void dispatchException(Throwable error, boolean backgroundTask) {
        if (error == null || ZhislandApplication.getCurrentActivity() == null) {
            return;
        }
        if (error instanceof HttpResponseException) {
            HttpResponseException exception = (HttpResponseException) error;
            int code = exception.getStatusCode();
            String message = exception.getMessage();

            if (!getNoPromptCodes().contains(code)
                    && !StringUtil.isNullOrEmpty(message)) {
                ToastUtil.showShort(message);
            } else if (code == CodeUtil.CODE_NO_PERMISSION) {
                // 无权限弹用户权益界面
                Activity currentActivity = ZhislandApplication.getCurrentActivity();
                if(currentActivity != null) {
                    DialogUtil.showPermissionsDialog(currentActivity, "");
                }
            }
        } else {
            if (!backgroundTask) {
                if (error instanceof ConnectException) {
                    ToastUtil.showLong("无网络连接，请稍后重试");
                } else {
                    ToastUtil.showLong("连接超时，请稍后重试");
                }
            }
        }
    }

    /**
     * 不需要提示的状态码
     */
    private ArrayList<Integer> getNoPromptCodes() {
        if (noPromptCodes == null) {
            noPromptCodes = new ArrayList<>();
            noPromptCodes.add(CodeUtil.CODE_NO_REGISTER);
            noPromptCodes.add(CodeUtil.CODE_LOGIN_LIMIT);
            noPromptCodes.add(CodeUtil.CODE_BASIC_INFO_YES);
            noPromptCodes.add(CodeUtil.CODE_NO_NEW_VERSION);
            noPromptCodes.add(CodeUtil.CODE_NO_VERSION_INFO);
            noPromptCodes.add(CodeUtil.CODE_POSITION_NO_CONFORM);
            noPromptCodes.add(CodeUtil.CODE_NO_INVITE);
            noPromptCodes.add(CodeUtil.CODE_UNENABLE_COMMENT);
            noPromptCodes.add(CodeUtil.CODE_NOT_ACCESS_WHITE);
            noPromptCodes.add(CodeUtil.CODE_INVITE_INVALID);
            noPromptCodes.add(CodeUtil.CODE_NO_PERMISSION);
        }
        return noPromptCodes;
    }
}
