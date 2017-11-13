package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.common.view.ContactRefuseView;
import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.lib.component.frag.FragBase;

import java.util.ArrayList;

/**
 * 建立好友圈，通讯录没有权限
 */
public class FragInviteRequestNoPower extends FragBase {

    private static final int TAG_ID_RIGHT = 111;
    private static final String KEY_IS_FROM_FRESH_TASK = "key_is_from_fresh_task";

    // 是否是新手任务跳转过来的
    private boolean isFromTask;

    public static void invoke(Context context, boolean isFromTask) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragInviteRequestNoPower.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "升级为海客";
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;

        param.runnable = titleRunnable;
        param.titleBtns = new ArrayList<>();
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = context.getString(R.string.str_haike);
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.white);
        param.titleBtns.add(tb);

        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(KEY_IS_FROM_FRESH_TASK, isFromTask);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (tagId == TAG_ID_RIGHT) {
                FragWebViewActivity.launch(context, Config.getUserPermissionUrl(1), "会员权限");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContactRefuseView refuseView = new ContactRefuseView(getActivity());
        refuseView.setContent("未授权通讯录\n无法找到可帮你升级海客的好友");
        refuseView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        refuseView.setGravity(Gravity.CENTER);
        return refuseView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFromTask = getActivity().getIntent().getBooleanExtra(KEY_IS_FROM_FRESH_TASK, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PhoneContactUtil.hasContactData()) {
            FragInviteRequest.invoke(getActivity(), isFromTask);
            getActivity().finish();
        }
    }

    @Override
    protected String getPageName() {
        return "FragInviteRequestNoPower";
    }


}
