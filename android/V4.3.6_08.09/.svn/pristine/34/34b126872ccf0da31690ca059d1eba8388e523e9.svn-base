package com.zhisland.android.blog.profile.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.android.blog.event.controller.FragEvent;
import com.zhisland.android.blog.freshtask.view.impl.FragInviteRequest;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/8/15.
 */
public class FragNoPermissions extends FragBase {

    private static final String INTENT_KEY_PAGENAME_FROM = "intent_key_pagename_from";

    @InjectView(R.id.ivIcon)
    ImageView ivIcon;

    @InjectView(R.id.tvContentL)
    TextView tvContentL;

    @InjectView(R.id.tvContentS)
    TextView tvContentS;

    @InjectView(R.id.tvApply)
    TextView tvApply;

    String pageName;

    User user;

    public static void invoke(Context context, final String pageName) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragNoPermissions.class;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_PAGENAME_FROM, pageName == null ? "" : pageName);
        context.startActivity(intent);
    }

    @Override
    protected String getPageName() {
        return "EntranceNoPermissions";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().setTheme(android.R.style.Theme_Translucent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.view_user_permissions, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        tracker();
        return root;
    }

    private void initView() {
        user = DBMgr.getMgr().getUserDao().getSelfUser();
        if (user.isHaiKe() || user.isDaoDing()) {
            ivIcon.setImageResource(R.drawable.icon_task_greenlion);
            tvContentL.setText("该功能仅对岛邻开放");
            tvContentS.setText("升级为岛邻获取更多功能权限");
            tvApply.setBackgroundResource(R.drawable.sel_bgreen_c30);
            tvApply.setText("了解更多");
        } else {
            ivIcon.setImageResource(R.drawable.icon_task_dolphin);
            tvContentL.setText("该功能仅对海客开放");
            tvContentS.setText("升级为海客获取更多功能权限");
            tvApply.setBackgroundResource(R.drawable.sel_bblue_c30);
            tvApply.setText("立即升级");
        }
    }

    private void tracker() {
        pageName = getActivity().getIntent().getStringExtra(INTENT_KEY_PAGENAME_FROM);
        if (StringUtil.isEquals(pageName, FragEvent.PAGE_NAME)) {
            ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.PAGE_EVENT_USER_LEVEL_LIMITED);
        }
    }

    @OnClick(R.id.root)
    void onRootClick() {
        getActivity().finish();
    }

    @OnClick(R.id.tvApply)
    void onApplyClick() {
        if (StringUtil.isEquals(pageName, FragEvent.PAGE_NAME)) {
            ZhislandApplication.trackerClickEvent(pageName, TrackerAlias.CLICK_EVENT_USER_LEVEL);
        }
        if (user.isHaiKe()) {
            FragWebViewActivity.launch(getActivity(), Config.getUserPermissionUrl(DBMgr.getMgr().getUserDao().getSelfUser(), "2"), "会员权限");
        } else {
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_UPGRADE_HAIKE_PERMISSIONS_CONFIRM);
            FragInviteRequest.invoke(getActivity(), false);
        }
        getActivity().finish();
    }

}
