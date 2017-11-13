package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * 用户助理或服务岛丁 数据结构
 */
public class UserAssistant extends OrmDto {

	private static final long serialVersionUID = 1L;

	/**
	 * 可见范围：岛邻/海客/岛丁 可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_ALL = 1;
	/**
	 * 可见范围：仅好友可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_FRIEND = 3;
	/**
	 * 可见范围：保密
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_NO = 4;

	public static final String NAME_CONTACT_VISIBLE_RANGE_ALL = "岛邻/海客/岛丁 可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_FRIEND = "仅好友可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_NO = "保密";

	/**
	 * 助理 或 服务岛丁 User
	 * */
	@SerializedName("user")
	public User assistant;


	/**
	 * 可见范围
	 * */
	@SerializedName("visibleRange")
	public Integer visibleRange;
}
