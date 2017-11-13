package com.zhisland.android.blog.contacts.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.FragBaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆后提醒用户去邀请好友
 */
public class ActGotoInvite extends FragBaseActivity {

    public static void invoke(Activity activity, int reqCode) {
        Intent intent = new Intent(activity, ActGotoInvite.class);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setContentView(R.layout.frag_get_invite_code);
        ButterKnife.inject(this);
        PrefUtil.Instance().setShouldPopInviteView();
    }

    @OnClick(R.id.btnFinish)
    public void onClickBtnFinish() {
        finish();
    }

    @OnClick(R.id.btnGotoInvite)
    public void onClickGoToInvite() {
        setResult(RESULT_OK);
        finish();
    }
}