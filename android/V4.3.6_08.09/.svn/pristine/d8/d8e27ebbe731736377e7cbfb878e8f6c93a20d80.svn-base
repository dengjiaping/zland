package com.zhisland.android.blog.find.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.util.PhoneContactUtil;
import com.zhisland.lib.component.act.TitleType;


/**
 * 可能认识的人页面
 * Created by Mr.Tui on 2016/5/17.
 */
public class ActContactFriend extends FragBaseActivity {

    public static void invoke(Context context) {
        Intent intent = new Intent(context, ActContactFriend.class);
        context.startActivity(intent);
    }

    FragNoPermission fragNoPermission;
    FragContactFriend fragContactFriend;

    private boolean hasPermission = false;

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setSwipeBackEnable(true);
        getTitleBar().addBackButton();
        getTitleBar().setTitle("可能认识的人");
        fragNoPermission = new FragNoPermission();
        fragContactFriend = new FragContactFriend();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragNoPermission);
        ft.add(R.id.frag_container, fragContactFriend);
        ft.hide(fragNoPermission);
        ft.hide(fragContactFriend);
        ft.commit();
    }

    @Override
    protected void onResume() {
        //在onResume判断联系人权限,无权限显示fragNoPermission,有权限显示fragContactFriend
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (PhoneContactUtil.hasContactData()) {
            boolean hiddenBefore = fragContactFriend.isHidden();
            ft.hide(fragNoPermission);
            ft.show(fragContactFriend);
            //fragContactFriend由隐藏到显示状态,刷新数据
            if (hiddenBefore) {
                fragContactFriend.refreshData();
            }
        } else {
            ft.show(fragNoPermission);
            ft.hide(fragContactFriend);
        }
        ft.commitAllowingStateLoss();
        super.onResume();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
