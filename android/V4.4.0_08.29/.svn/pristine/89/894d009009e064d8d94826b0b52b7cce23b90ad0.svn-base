package com.zhisland.android.blog.find.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.IntentUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 没有通讯录权限fragment
 * Created by Mr.Tui on 2016/5/24.
 */
public class FragNoPermission extends FragBase {

    @InjectView(R.id.tvCRTitle)
    public TextView tvTitle;
    @InjectView(R.id.tvCRDesc)
    public TextView tvDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contacts_refuse, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvTitle.setText("无法找到可能认识的人");
        tvDesc.setVisibility(View.VISIBLE);
        tvDesc.setText("您已拒绝授权使用通讯录，无法帮助您找到可能认识的人");
    }

    @OnClick(R.id.tvOpenContactsPermission)
    public void onClickOpenPermission() {
        //去打开通信录授权
        IntentUtil.intentToSystemSetting(getActivity());
    }

    @Override
    public String getPageName() {
        return "FragNoPermission";
    }
}
