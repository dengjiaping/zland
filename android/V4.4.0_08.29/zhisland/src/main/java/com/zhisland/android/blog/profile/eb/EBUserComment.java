package com.zhisland.android.blog.profile.eb;

import com.zhisland.android.blog.profile.dto.UserComment;

/*
 * 用户神评论 event bus 
 */
public class EBUserComment {

	// 置顶评论
	public static final int TYPE_ADD_TOP_COMMENT = 1;
	// 取消置顶评论
	public static final int TYPE_CANCEL_TOP_COMMENT = 2;
	// 删除评论
	public static final int TYPE_DELETE_COMMENT = 3;

	private int type;
	private UserComment comment;

	public EBUserComment(int type, UserComment comment) {
		this.type = type;
		this.comment = comment;
	}

	public int getType() {
		return type;
	}

	public UserComment getUserComment() {
		return comment;
	}
}
