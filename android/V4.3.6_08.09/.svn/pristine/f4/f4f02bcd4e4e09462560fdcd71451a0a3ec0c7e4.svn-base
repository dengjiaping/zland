package com.zhisland.android.blog.common.eb;

import com.zhisland.android.blog.common.dto.User;

public class EBUser {

	/**
	 * 登录用户的个人信息发生变化。所有和用户相关creat或update接口在成功时，需要将变更信息保存到数据库，并发该eventbus。
	 * */
	public static final int TYPE_USER_SELF_INFO_CHANGED = 1;
	public static final int TYPE_GET_USER_DETAIL = 2;

	private int type;
	private User user;

	public EBUser(int type, User user) {
		this.type = type;
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public User getUser() {
		return user;
	}
}
