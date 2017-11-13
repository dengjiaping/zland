package com.zhisland.android.blog.aa.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.lib.component.application.EnvType;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 登录注册引导页
 */
public class FragGuide extends FragBase {

    @InjectView(R.id.tvModifyEnv)
    TextView tvModifyEnv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_guide, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        // 在开发和自定义环境下，可修改app的环境，其它环境下隐藏入口
        switch (Config.ENV_TYPE) {
            case EnvType.ENV_DEV:
            case EnvType.ENV_CUSTOM:
                tvModifyEnv.setVisibility(View.VISIBLE);
                break;
            default:
                tvModifyEnv.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 跳转注册
     */
    @OnClick(R.id.btnRegister)
    public void onClickbtnRegistere() {
        // 跳转注册 Fragment
        ActRegisterAndLogin.invoke(getActivity(), ActRegisterAndLogin.TYPE_REGISTER);
        getActivity().finish();
    }

    /**
     * 跳转登录
     */
    @OnClick(R.id.btnLogin)
    public void onClickbtnRegister() {
        // 跳转登录 Fragment
        ActRegisterAndLogin.invoke(getActivity(), ActRegisterAndLogin.TYPE_LOGIN);
        getActivity().finish();
    }

    @OnClick(R.id.tvModifyEnv)
    public void onClickModifyEnv() {
        // 跳转修改环境
        FragModifyEnv.invoke(getActivity());
    }

    @Override
    protected String getPageName() {
        return "FragGuide";
    }

}
