package com.zhisland.android.blog.profile.controller;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.MobileUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.act.TitleType;

import java.util.List;

/**
 * 选择标签
 */
public class ActSelTags extends FragBaseActivity {

	public static final int FROM_REGISTER = 0;
	public static final int FROM_PROFILE = 1;

	/**
	 * 兴趣爱好
	 */
	public static final int TYPE_INTERESTS = 0;
	/**
	 * 擅长领域
	 */
	public static final int TYPE_SPECIALTY = 1;
	/**
	 * 愿意结识
	 */
	public static final int TYPE_WANT_FRIEND = 2;

	public static final int TITLE_RIGHT_BUTTON_TAG = 606;
	public static final int TITLE_LEFT_BUTTON_TAG = 607;

	public static final String INTENT_KEY_FROM = "intent_key_from";
	public static final String INTENT_KEY_TYPE = "intent_key_type";

	FragProfileInterests fragProfileInterests;
	FragProfileSpecialty fragProfileSpecialty;
	FragProfileWantFriend fragProfileWantFriend;

	private int type;
	private int from;

	public static void invoke(Activity context, int from, int type) {
		Intent intent = new Intent(context, ActSelTags.class);
		Bundle bundle = new Bundle();
		bundle.putInt(INTENT_KEY_FROM, from);
		bundle.putInt(INTENT_KEY_TYPE, type);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, 333);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);

		fragProfileInterests = new FragProfileInterests();
		fragProfileSpecialty = new FragProfileSpecialty();
		fragProfileWantFriend = new FragProfileWantFriend();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, fragProfileInterests);
		ft.add(R.id.frag_container, fragProfileSpecialty);
		ft.add(R.id.frag_container, fragProfileWantFriend);
		ft.hide(fragProfileInterests);
		ft.hide(fragProfileSpecialty);
		ft.hide(fragProfileWantFriend);
		ft.commit();

		type = getIntent().getExtras().getInt(INTENT_KEY_TYPE);
		switch (type) {
		case TYPE_INTERESTS:
			showFragInterests();
			break;
		case TYPE_SPECIALTY:
			showFragSpecialy();
			break;
		case TYPE_WANT_FRIEND:
			showWantFriends();
			break;
		}

		from = getIntent().getIntExtra(INTENT_KEY_FROM, FROM_REGISTER);
		if (from == FROM_REGISTER) {
			setSwipeBackEnable(false);
			TextView rightBtn = TitleCreator.Instance().createTextButton(this,
					"上一步", R.color.color_dc);
			getTitleBar().addLeftTitleButton(rightBtn, TITLE_LEFT_BUTTON_TAG);
		} else if (from == FROM_PROFILE) {
			setSwipeBackEnable(true);
			getTitleBar().addBackButton();
			switch (type) {
			case TYPE_INTERESTS:
				getTitleBar().setTitle("兴趣爱好");
				break;
			case TYPE_SPECIALTY:
				getTitleBar().setTitle("擅长领域");
				break;
			case TYPE_WANT_FRIEND:
				getTitleBar().setTitle("愿意结识");
				break;
			default:
				break;
			}
			TextView rightBtn = TitleCreator.Instance().createTextButton(this,
					"保存", R.color.color_dc);
			getTitleBar().addRightTitleButton(rightBtn, TITLE_RIGHT_BUTTON_TAG);
		}
	}

	/**
	 * 兴趣爱好
	 */
	public void showFragInterests() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.show(fragProfileInterests);
		ft.hide(fragProfileSpecialty);
		ft.hide(fragProfileWantFriend);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 擅长领域
	 */
	public void showFragSpecialy() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.hide(fragProfileInterests);
		ft.show(fragProfileSpecialty);
		ft.hide(fragProfileWantFriend);
		ft.commitAllowingStateLoss();
	}

	/**
	 * 愿意结识
	 */
	public void showWantFriends() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.hide(fragProfileInterests);
		ft.hide(fragProfileSpecialty);
		ft.show(fragProfileWantFriend);
		ft.commitAllowingStateLoss();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}

	public void onTitleClicked(View view, int tagId) {
		switch (tagId) {
		case TITLE_RIGHT_BUTTON_TAG:
			if (fragProfileInterests.isVisible()) {
				fragProfileInterests.saveIntroduction();
				List<String> selTags = fragProfileInterests.getSelTags();
			} else if (fragProfileSpecialty.isVisible()) {
				fragProfileSpecialty.saveIntroduction();
				List<String> selTags = fragProfileSpecialty.getSelTags();
			} else if (fragProfileWantFriend.isVisible()) {
				fragProfileWantFriend.saveIntroduction();
				List<String> selTags = fragProfileWantFriend.getSelTags();
			}
			break;
		case TITLE_LEFT_BUTTON_TAG:
			back();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (from == FROM_REGISTER) {
			back();
		} else {
			super.onBackPressed();
		}
	}

	private void back() {
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (fragProfileInterests.isVisible()) {
			List<String> interestsTags = fragProfileInterests.getSelTags();
			user.interests = MobileUtil.listToString(interestsTags);
			finish();
		} else if (fragProfileSpecialty.isVisible()) {
			List<String> specialtyTags = fragProfileSpecialty.getSelTags();
			user.specialties = MobileUtil.listToString(specialtyTags);
			showFragInterests();
		} else if (fragProfileWantFriend.isVisible()) {
			List<String> wantFriendTags = fragProfileWantFriend.getSelTags();
			user.wantFriends = MobileUtil.listToString(wantFriendTags);
			showFragSpecialy();
		}
		DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
	}

	public void updateUser() {
		List<String> interestsTags = fragProfileInterests.getSelTags();
		List<String> specialtyTags = fragProfileSpecialty.getSelTags();
		List<String> wantFriendTags = fragProfileWantFriend.getSelTags();
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		user.interests = MobileUtil.listToString(interestsTags);
		user.specialties = MobileUtil.listToString(specialtyTags);
		user.wantFriends = MobileUtil.listToString(wantFriendTags);
		DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(user);
		perfectInformation(user);
	}

	private void perfectInformation(User user) {
		// 服务器数据库瞎改，正常来说user.lastEvent本来就为空
		user.lastEvent = null;
		showProgressDlg();
		ZHApis.getUserApi().updateUser(ActSelTags.this, user, TaskUpdateUser.TYPE_UPDATE_OTHER,
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						PrefUtil.Instance().setIsCanLogin(true);
					}

					@Override
					public void onFailure(Throwable error) {
					}

					@Override
					public void onFinish() {
						super.onFinish();
						hideProgressDlg();
					}
				});
	}
}