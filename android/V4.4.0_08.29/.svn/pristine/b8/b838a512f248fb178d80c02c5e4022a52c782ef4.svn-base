package com.zhisland.android.blog.im.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.KeyEvent;

import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMUser;
import com.zhisland.lib.R;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;

public class ActChat extends FragBaseActivity {
    public static final String INK_CONTACT = "ink_contact";

    /**
     * 尝试发起和某个人的对话
     *
     * @param context
     * @param uid
     */
    public static void invoke(Context context, long uid) {
        String jid = IMUser.buildJid(uid);
        invoke(context, jid);
    }

    public static void invoke(Context context, String jid) {
        IMUser contact = DBMgr.getHelper().getUserDao().getByJid(jid);
        if (contact != null) {
            Intent intent = new Intent(context, ActChat.class);
            intent.putExtra(INK_CONTACT, contact);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } else {
            ToastUtil.showShort("你和TA已不是好友！");
        }
    }



    FragChat frag;
    private IMUser contact;
    private XmppTitleListener conListener;

    @Override
    public boolean shouldContinueCreate(Bundle savedInstanceState) {
        contact = (IMUser) getIntent().getSerializableExtra(INK_CONTACT);
        return contact != null;
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        this.setTitle(contact.name);
        this.getTitleBar().addBackButton();
        this.setSwipeBackEnable(true);

        conListener = new XmppTitleListener(getTitleBar(), contact.name);
        EventBus.getDefault().register(conListener);

        frag = new FragChat();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, frag);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(conListener);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        frag.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (frag.collapsePanel()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }
}
