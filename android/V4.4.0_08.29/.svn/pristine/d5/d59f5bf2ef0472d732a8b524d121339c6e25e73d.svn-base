package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册成功页
 */
public class FragRegisterSuccess extends FragBase {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragRegisterSuccess.class;
        param.swipeBackEnable = false;
        param.noTitle = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_register_success, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PrefUtil.Instance().setIsCanLogin(true);
        KillSelfMgr.getInstance().setCurrentPage(KillSelfMgr.FRAG_REGISTER_SUCCESS);
    }


    //开启岛上之旅
    @OnClick(R.id.tvGoToHome)
    public void onClickToHome() {
        HomeUtil.NavToHome(getActivity());
        getActivity().finish();
    }

    @Override
    public String getPageName() {
        return "InviteHaveInvitor";
    }
}
