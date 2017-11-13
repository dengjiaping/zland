package com.zhisland.android.blog.info.view.impl;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.component.act.TitleType;

/**
 * 举报原因选择页面
 * Created by Mr.Tui on 2016/7/11.
 */
public class ActReportType extends FragBaseActivity {

    FragReportType fragReportType;

    public static void invoke(Context context, ZHInfo info) {
        if (info == null) {
            return;
        }
        Intent intent = new Intent(context, ActReportType.class);
        intent.putExtra(FragReportType.INTENT_KEY_REPORT_INFO, info);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 通过FLAG_ACTIVITY_CLEAR_TOP清除已存在ActReportType之上的页面，再关闭自己。
     * */
    public static void invokeFinish(Context context) {
        Intent intent = new Intent(context, ActReportType.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //不传INTENT_KEY_REPORT_INFO inf会finish 自己
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        getTitleBar().setTitle("举报");
        getTitleBar().addBackButton();
        fragReportType = new FragReportType();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragReportType);
        ft.commit();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
