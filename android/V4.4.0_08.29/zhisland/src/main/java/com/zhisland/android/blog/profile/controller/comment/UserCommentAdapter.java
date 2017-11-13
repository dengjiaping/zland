package com.zhisland.android.blog.profile.controller.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.component.adapter.BaseListAdapter;

public class UserCommentAdapter extends BaseListAdapter<UserComment> {

	private Context context;
	private int from;
	private Boolean isUserSelf;

	public UserCommentAdapter(Context context, int from, Boolean isUserSelf) {
		this.context = context;
		this.from = from;
		this.isUserSelf = isUserSelf;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserCommentHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_user_comment, null);
			holder = new UserCommentHolder(convertView, context, isUserSelf);
			convertView.setTag(holder);
		} else {
			holder = (UserCommentHolder) convertView.getTag();
		}
		holder.fill(getItem(position), from);
		return convertView;
	}

	@Override
	protected void recycleView(View view) {
	}

}
