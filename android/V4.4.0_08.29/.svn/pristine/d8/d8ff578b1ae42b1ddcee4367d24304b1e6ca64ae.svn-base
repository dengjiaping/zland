package com.zhisland.android.blog.aa.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.LoginResponse;
import com.zhisland.android.blog.aa.eb.EBLogin;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.InternationalPhoneView;
import com.zhisland.lib.util.IntentUtil;

import org.apache.http.client.HttpResponseException;

import de.greenrobot.event.EventBus;

/**
 * 登录注册 返回的状态码 处理
 */
public class ResultCodeUtil {

    private static ResultCodeUtil resultCodeUtil;
    private static final Object sysObj = new Object();

    private ResultCodeUtil() {
    }

    public static ResultCodeUtil getInstance() {
        if (resultCodeUtil == null) {
            synchronized (sysObj) {
                if (resultCodeUtil == null) {
                    resultCodeUtil = new ResultCodeUtil();
                }
            }
        }
        return resultCodeUtil;
    }

    /**
     * 注册分发 (1) 用户等级海客及以上&已激活：跳转主页 （2）信息不完整或者是预注册 跳 完善基本信息
     */
    public void dispatchSuccess(Activity activity, LoginResponse content) {
        if (content != null && activity != null) {
            User.saveSelfInformation(content);

            int status = content.basicInfoStatus;
            if (status != CodeUtil.CODE_BASIC_INFO_YES) {
                // 跳转到完善基本基本信息页
                FragCreateBasicInfo.invoke(activity, false);
                activity.finish();
            } else if (!content.user.isUploadContacts()) {
                // 没上传过通讯录，跳转授权通讯录界面
                FragVisitContact.invoke(activity, false);
                activity.finish();
            } else {
                // 直接登录
                PrefUtil.Instance().setIsCanLogin(true);
                HomeUtil.NavToHome(activity);
                activity.finish();
            }
        }
    }

    /**
     * 分发处理登录注册返回的错误状态码
     */
    public void dispatchErrorCode(Context context, Throwable error, InternationalPhoneView phoneView) {
        if (error instanceof HttpResponseException) {
            HttpResponseException e = (HttpResponseException) error;
            int statusCode = e.getStatusCode();
            showDialog(context, statusCode, phoneView);
        }
    }

    // 根据不同type show不同的dialog
    private void showDialog(final Context context, int type, final InternationalPhoneView phoneView) {
        final CommonDialog dialog = new CommonDialog(context);
        final String zhislandPhone = context.getString(R.string.app_service_phone);
        switch (type) {
            case CodeUtil.CODE_NO_REGISTER:
                //未注册
                dialog.show();
                dialog.setTitle("此号码还未注册正和岛；如果您更换了登岛注册的手机号，请联系服务岛丁，或拨打客服热线" + zhislandPhone);
                dialog.tvDlgConfirm.setText("拨打客服");
                dialog.tvDlgCancel.setText("知道了");
                dialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        IntentUtil.dialTo(context, zhislandPhone);
                        dialog.dismiss();
                    }
                });
                break;
            case CodeUtil.CODE_NO_ACCESS:
                dialog.show();
                dialog.setTitle("目前只支持岛邻会员登录，登岛请访问www.zhisland.com或咨询" + zhislandPhone);
                dialog.tvDlgConfirm.setText("拨打客服");
                dialog.tvDlgCancel.setText("知道了");
                dialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        IntentUtil.dialTo(context, zhislandPhone);
                        dialog.dismiss();
                    }
                });
                break;
            case CodeUtil.CODE_LOGIN_LIMIT:
                // 密码输入错误次数达到上限
                dialog.show();
                dialog.setTitle("密码连续5次输入有误");
                dialog.setContent("点击确认使用验证码登录");
                dialog.tvDlgConfirm
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                EBLogin event = new EBLogin(EBLogin.LOGIN_BY_VERFIFY_CODE);
                                event.setObj(phoneView.getMobileNum() + "," + phoneView.getCurrentDict().code);
                                EventBus.getDefault().post(event);
                            }
                        });
                break;
        }
    }
}
