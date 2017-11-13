package com.zhisland.android.blog.info.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.component.frag.FragBase;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 举报成功页面
 */
public class FragReportOk extends FragBase {

    private static final String INTENT_KEY_INFO = "intent_key_info";

    @Override
    protected String getPageName() {
        return "FragReportOk";
    }

    public static void invoke(Context context, ZHInfo info) {
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragReportOk.class;
        param.swipeBackEnable = false;
        param.title = "举报";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_INFO, info);
        context.startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_info_report_ok, null);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @OnClick(R.id.btnContinue)
    void continueClick() {
        goToInfoDetail();
    }

    @Override
    public boolean onBackPressed() {
        goToInfoDetail();
        return true;
    }

    private void goToInfoDetail() {
        ActReportType.invokeFinish(getActivity());
    }
}
