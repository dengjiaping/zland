package com.zhisland.android.blog.profile.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.contacts.controller.CardViewHolder;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;

public class FragSelfCard extends FragBase {

	CardViewHolder viewHolder;

	private User user;

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragSelfCard.class;
		param.title = "我的名片";
		param.enableBack = true;
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	public String getPageName() {
		return "ProfilePreviewCard";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_self_card, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		viewHolder = new CardViewHolder(getActivity(), root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (user == null) {
			getActivity().finish();
			return;
		}
		fillUser(user);
		getDataFromInternet(user.uid);
	}

	private void fillUser(User user) {
		if (viewHolder != null && user != null) {
			viewHolder.fill(user);
		}
	}

	private void getDataFromInternet(long id) {
		ZHApis.getUserApi().getUserDetail(getActivity(), id, new TaskCallback<UserDetail>() {

			@Override
			public void onSuccess(UserDetail content) {
				if (content != null) {
					com.zhisland.im.data.DBMgr
							.getHelper()
							.getChatDao()
							.updateAvatar(content.user.uid,
									content.user.userAvatar);
				}
				if (getActivity() != null) {
					user = content.user;
					fillUser(user);
				}
			}

			@Override
			public void onFailure(Throwable error) {
			}

		});
	}
}
