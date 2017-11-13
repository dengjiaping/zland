package com.zhisland.android.blog.freshtask.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.android.blog.freshtask.model.impl.ContactNoPowerModel;
import com.zhisland.android.blog.freshtask.presenter.ContactNoPowerPresenter;
import com.zhisland.android.blog.freshtask.view.IContactNoPower;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.IntentUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 建立好友圈，通讯录没有权限
 */
public class FragContactNoPower extends FragBaseMvp<ContactNoPowerPresenter>
        implements IContactNoPower {

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragContactNoPower.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "打造你的商界形象";
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_contact_no_power, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected ContactNoPowerPresenter createPresenter() {
        ContactNoPowerPresenter presenter = new ContactNoPowerPresenter();
        ContactNoPowerModel model = new ContactNoPowerModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PhoneContactUtil.hasContactData()) {
            FragFriendAdd.invoke(getActivity());
            finish();
        }
    }

    @Override
    protected String getPageName() {
        return "RookieTaskContactUnauthorized";
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    //================UI event start================

    @OnClick(R.id.tvOpenContactPower)
    public void onClickOpenContactPower() {
        // 跳转开启通讯录权限
        IntentUtil.intentToSystemSetting(getActivity());
    }

    @OnClick(R.id.tvNotAddFriend)
    public void onClickNotAddFriend() {
        // 关闭此页，移动卡片
        presenter().notAddFriend();
    }
    //===============UI event end=============

}
