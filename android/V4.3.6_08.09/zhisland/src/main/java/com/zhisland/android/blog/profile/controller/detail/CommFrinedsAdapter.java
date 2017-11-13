package com.zhisland.android.blog.profile.controller.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.lib.component.adapter.BaseListAdapter;

/**
 * 共同好友 adapter
 */
public class CommFrinedsAdapter extends BaseListAdapter<User> {

	private Context context;

	public CommFrinedsAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_common_friend, null);
			holder = new UserHolder(convertView, context);
			convertView.setTag(holder);
		} else {
			holder = (UserHolder) convertView.getTag();
		}
		holder.fill(getItem(position), position);
		return convertView;
	}

	@Override
	protected void recycleView(View view) {

	}

	class UserHolder {

		User user;

		Context context;

		int position;

		@InjectView(R.id.ivAvatar)
		AvatarView avatarView;

		public UserHolder(View v, final Context context) {
			this.context = context;
			ButterKnife.inject(this, v);
		}

		public void fill(User user, int position) {
			this.user = user;
			this.position = position;
			avatarView.fill(user, false);
		}
	}
}
