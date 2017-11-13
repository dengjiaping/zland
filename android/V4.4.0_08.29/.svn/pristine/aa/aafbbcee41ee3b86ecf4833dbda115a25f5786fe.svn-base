package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.common.view.ContactRefuseView;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 授权通讯录时，拒绝通讯录访问权限页面
 */
public class FragVisitContactsRefuse extends FragBase {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragVisitContactsRefuse.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "建立信任链";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContactRefuseView refuseView = new ContactRefuseView(getActivity());
        refuseView.setContent("未授权通讯录\n无法与岛上企业家建立信任链");
        refuseView.setTracker(getPageName(), TrackerAlias.CLICK_PERMISSIONS_OPEN);
        refuseView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        refuseView.setGravity(Gravity.CENTER);
        return refuseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (PhoneContactUtil.hasContactData()) {
            FragRegisterSuccess.invoke(getActivity());
            getActivity().finish();
        }
        super.onActivityCreated(savedInstanceState);
        KillSelfMgr.getInstance().setCurrentPage(KillSelfMgr.FRAG_VISIT_CONTACT_REFUSE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PhoneContactUtil.hasContactData()) {
            FragRegisterSuccess.invoke(getActivity());
            getActivity().finish();
        }
    }

    @Override
    public boolean onBackPressed() {
        FragVisitContact.invoke(getActivity(), false);
        return super.onBackPressed();
    }

    @Override
    public String getPageName() {
        return "ContactPermissionsUnauthorized";
    }
}
