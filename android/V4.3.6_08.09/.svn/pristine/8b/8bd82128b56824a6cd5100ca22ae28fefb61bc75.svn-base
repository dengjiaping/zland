package com.zhisland.android.blog.im.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.KeyEvent;

import com.beem.project.beem.service.Contact;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.lib.R;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;

public class ActChat extends FragBaseActivity {
	public static final String INK_CONTACT = "ink_contact";

	public static void invoke(Context context, Contact contact) {
		Intent intent = new Intent(context, ActChat.class);
		intent.putExtra(INK_CONTACT, contact);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	public static void invoke(Context context, long uid) {
		String jid = IMContact.buildJid(uid);
		invoke(context, jid);
	}

	public static void invoke(Context context, String jid) {
		Contact contact = DBMgr.getHelper().getContactDao().fetchContact(jid);
		if (contact != null) {
			invoke(context, contact);
		} else {
			ToastUtil.showShort("你和TA已不是好友！");
		}
	}

	public static Intent getInvokeIntent(Context context, long uid) {
		String jid = IMContact.buildJid(uid);
		return getInvokeIntent(context, jid);
	}

	public static Intent getInvokeIntent(Context context, Contact contact) {
		if (contact != null) {
			Intent intent = new Intent(context, ActChat.class);
			intent.putExtra(INK_CONTACT, contact);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			return intent;
		} else {
			return null;
		}
	}

	public static Intent getInvokeIntent(Context context, String jid) {
		Contact contact = DBMgr.getHelper().getContactDao().fetchContact(jid);
		return getInvokeIntent(context, contact);
	}

	FragChat frag;
	private Contact contact;
	private XmppTitleListener conListener;

	@Override
	public boolean shouldContinueCreate(Bundle savedInstanceState) {
		contact = (Contact) getIntent().getParcelableExtra(INK_CONTACT);
		return contact != null;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		this.setTitle(contact.getName());
		this.getTitleBar().addBackButton();
		this.setSwipeBackEnable(true);

		conListener = new XmppTitleListener(getTitleBar(), contact.getName());
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
