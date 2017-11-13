package com.zhisland.android.blog.aa.api;

import android.content.Context;

import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.aa.dto.SplashData;
import com.zhisland.android.blog.common.base.ApiBase;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.upapp.ZHUpgrade;
import com.zhisland.android.blog.setting.api.TaskCheckVersion;
import com.zhisland.android.blog.setting.api.TaskFeedBack;
import com.zhisland.android.blog.setting.api.TaskLogout;
import com.zhisland.lib.async.http.task.TaskCallback;

import java.util.ArrayList;

public class ZHApiAA extends ApiBase {

    // ===========singleton=============
    private ZHApiAA() {

    }

    private static class Holder {
        private static ZHApiAA INSTANCE = new ZHApiAA();
    }

    public static ZHApiAA Instance() {
        return Holder.INSTANCE;
    }

    /**
     * 验证码登录
     */
    public void loginByVerifyCode(Context context, String phone, String codes,
                                  TaskCallback<LoginResponse> callback) {
        addTask(context, new TaskLoginByVerifyCode(context, phone, codes, callback));
    }

    /**
     * 验证码登录
     */
    public void loginByPwd(Context context, String phone, String pwd,
                           TaskCallback<LoginResponse> callback) {
        addTask(context, new TaskLoginByPwd(context, phone, pwd, callback));
    }

    /**
     * 登出
     */
    public void logout(Context context, TaskCallback<Object> callback) {
        addTask(context, new TaskLogout(context, callback));
    }

    /**
     * 获取验证码
     */
    public void getVerifyCode(Context context, String mobileNum, String countryCode, TaskCallback<Object> callback) {
        addTask(context, new TaskGetVerifyCode(context, mobileNum, countryCode, callback));
    }

    /**
     * 注册
     */
    public void register(Context context, String mobileNum, String code, TaskCallback<LoginResponse> callback) {
        addTask(context, new TaskRegister(context, mobileNum, code, callback));
    }

    /**
     * 修改密码
     */
    public void modifyPwd(Context context, String newPassword,
                          TaskCallback<Object> responseCallback) {
        addTask(context, new TaskModifyPwd(context, newPassword,
                responseCallback));
    }

    /**
     * 版本更新
     */
    public void update(Context context, boolean isFromSplash, TaskCallback<ZHUpgrade> callback) {
        addTask(context, new TaskCheckVersion(context, isFromSplash, callback));
    }

    /**
     * 意见反馈
     */
    public void feedBack(Context context, String feedBack, TaskCallback<Object> callback) {
        addTask(context, new TaskFeedBack(context, feedBack, callback));
    }

    /**
     * 完善基本信息
     */
    public void updateBasicInfo(Context context, User user, TaskCallback<Integer> callback) {
        addTask(context, new TaskUpdateBasicInfo(context, user, callback));
    }

    /**
     * 获取开机闪屏数据
     */
    public void getSplashData(Context context, TaskCallback<ArrayList<SplashData>> callback) {
        addTask(context, new TaskGetSplashData(context, callback));
    }
}
