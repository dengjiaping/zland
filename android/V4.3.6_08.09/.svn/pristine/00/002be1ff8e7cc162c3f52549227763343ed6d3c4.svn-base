package com.zhisland.android.blog.common.retrofit;

import android.app.Activity;

import com.zhisland.android.blog.aa.controller.ActRegisterAndLogin;
import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.aa.controller.SplashActivity;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口通用错误处理
 * Created by 向飞 on 2016/5/31.
 */
public class ApiErrorHandle {
    private ArrayList<Integer> noPromptCodes;
    private List<Integer> aaCode;

    private ApiErrorHandle() {

        aaCode = new ArrayList<>(4);
        aaCode.add(CodeUtil.CODE_NOT_EXIST);
        aaCode.add(CodeUtil.CODE_ILLEGAL);
        aaCode.add(CodeUtil.CODE_TOKEN_INVITATION);
        aaCode.add(CodeUtil.CODE_AUTHENTICATION_INFORMATION_NOT_COMPLETE);

        //不需要显示提示的错误码
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

    /**
     * 处理通用的API接口错误
     *
     * @param code
     * @param message
     * @return
     */
    public boolean handleApiError(int code, String message) {

        if (isAaError(code)) {
            Activity latestActivity = ZhislandApplication.getCurrentActivity();

            if (latestActivity == null) {
                HomeUtil.ClearDataAndCache();
            } else {
                if ((latestActivity instanceof SplashActivity)
                        || (latestActivity instanceof ActGuide)
                        || (latestActivity instanceof ActRegisterAndLogin))
                    return true;

                HomeUtil.logout();
            }

            return true;
        } else if (shouldShowPrompt(code)
                && !StringUtil.isNullOrEmpty(message)) {
            ZhislandApplication.ShowToastFromBackground(message);
        } else if (code == CodeUtil.CODE_NO_PERMISSION) {
            // 无权限弹用户权益界面
            final Activity currentActivity = ZhislandApplication.getCurrentActivity();
            if (currentActivity != null) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.showPermissionsDialog(currentActivity, "");
                    }
                });
            }
        }
        return false;
    }

    private boolean isAaError(int code) {
        return aaCode.contains(code);
    }

    private boolean shouldShowPrompt(int code) {
        return !noPromptCodes.contains(code);
    }

    public static ApiErrorHandle INSTANCE() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static ApiErrorHandle INSTANCE = new ApiErrorHandle();
    }

}
