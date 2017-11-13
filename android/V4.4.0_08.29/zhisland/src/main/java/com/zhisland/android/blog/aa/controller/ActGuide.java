package com.zhisland.android.blog.aa.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.lib.component.act.TitleType;

/**
 * 登录注册引导页
 */
public class ActGuide extends FragBaseActivity {

    public static void invoke(Context context) {
        Intent intent = new Intent(context, ActGuide.class);
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setSwipeBackEnable(false);

        //清除
        KillSelfMgr.getInstance().setCurrentPage(-1);

        FragGuide fragGuide = new FragGuide();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragGuide);
        ft.commit();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_NONE;
    }

}