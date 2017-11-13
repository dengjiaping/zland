package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 求邀请入口
 */
public class FragVisitContact extends FragBase {

    private static final String INK_CONTINUE_REGISTER = "ink_continue_register";

    /**
     * @param isShowContinueRegister 是否显示继续注册dialog
     */
    public static void invoke(Context context, boolean isShowContinueRegister) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragVisitContact.class;
        param.swipeBackEnable = false;
        param.title = "建立信任链";
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INK_CONTINUE_REGISTER, isShowContinueRegister);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_visit_contact, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        KillSelfMgr.getInstance().setCurrentPage(KillSelfMgr.FRAG_VISIT_CONTACT);
        boolean isShowContinueRegister = getActivity().getIntent().getBooleanExtra(INK_CONTINUE_REGISTER, false);
        if (isShowContinueRegister) {
            DialogUtil.getInstatnce().showContinueRegisterDialog(getActivity(), "注册进行中", "继续完成注册，结识8000+企业家", "返回登录", "继续注册");
        }
    }

    @OnClick(R.id.tvVisitContact)
    public void onClickVisitContact() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_PERMISSIONS_CLICK_BUTTON);
        // 授权通讯录
        FragVisitContactsRefuse.invoke(getActivity());
        getActivity().finish();
    }

    @Override
    public String getPageName() {
        return "EntrancePermissionsDesc";
    }
}
