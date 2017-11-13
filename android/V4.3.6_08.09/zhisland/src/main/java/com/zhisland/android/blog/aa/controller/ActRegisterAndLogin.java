package com.zhisland.android.blog.aa.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.eb.EBLogin;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.lib.component.act.TitleType;

import de.greenrobot.event.EventBus;


/**
 * 注册 登录 包括使用验证码登录/使用密码登录
 */
public class ActRegisterAndLogin extends FragBaseActivity {

    // 注册 type
    public static final int TYPE_REGISTER = 0;
    // 登录 type
    public static final int TYPE_LOGIN = 1;

    private static final String TYPE_SHOW = "type_show";

    private FragRegister fragRegister;
    private FragLoginByVerifyCode fragLoginByVerifyCode;
    private FragLoginByPwd fragLoginByPwd;
    // 是否显示过注册页，如果显示过，当返回时需要退回注册页
    private boolean showFragRegister;

    public static void invoke(Activity activity, int show) {
        Intent intent = new Intent(activity, ActRegisterAndLogin.class);
        intent.putExtra(TYPE_SHOW, show);
        activity.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setSwipeBackEnable(false);
        EventBus.getDefault().register(this);

        fragRegister = new FragRegister();
        fragLoginByVerifyCode = new FragLoginByVerifyCode();
        fragLoginByPwd = new FragLoginByPwd();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragRegister);
        ft.add(R.id.frag_container, fragLoginByVerifyCode);
        ft.add(R.id.frag_container, fragLoginByPwd);
        ft.commit();

        getTitleBar().addBackButton();
        getTitleBar().hideBottomLine();

        int showType = getIntent().getIntExtra(TYPE_SHOW, TYPE_REGISTER);
        if (showType == TYPE_REGISTER) {
            showFragRegister = true;
            showRegisterFragment("", "");
        } else {
            showFragRegister = false;
            showFragByVerifyCode("", "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EBLogin eb) {
        if (eb.action == EBLogin.LOGIN_BY_VERFIFY_CODE) {
            String str = (String) eb.getObj();
            if (str != null && str.contains(",")) {
                String[] split = str.split(",");
                if (split.length == 2) {
                    showFragByVerifyCode(split[0], split[1]);
                } else {
                    showFragByVerifyCode(null, null);
                }
            }
        }
    }

    /**
     * 显示注册Fragment
     */
    private void showRegisterFragment(String mobileNum, String countryCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragRegister);
        ft.hide(fragLoginByVerifyCode);
        ft.hide(fragLoginByPwd);
        ft.commitAllowingStateLoss();
        fragRegister.setMobile(mobileNum, countryCode);
        getTitleBar().setTitle("使用手机号注册");
    }

    /**
     * 显示验证码登录Fragment
     */
    public void showFragByVerifyCode(String mobileNum, String countryCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragRegister);
        ft.show(fragLoginByVerifyCode);
        ft.hide(fragLoginByPwd);
        ft.commitAllowingStateLoss();
        fragLoginByVerifyCode.setMobile(mobileNum, countryCode);
        getTitleBar().setTitle("使用短信验证码登录");
    }

    /**
     * 显示密码登录Fragment
     */
    public void showFragByPwd(String mobileNum, String countryCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragRegister);
        ft.hide(fragLoginByVerifyCode);
        ft.show(fragLoginByPwd);
        ft.commitAllowingStateLoss();
        fragLoginByPwd.setMobile(mobileNum, countryCode);
        getTitleBar().setTitle("使用密码登录");
    }


    @Override
    public void onBackPressed() {
        if (showFragRegister && !fragRegister.isVisible()) {
            showRegisterFragment("", "");
            return;
        }
        ActGuide.invoke(this);
        super.onBackPressed();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
