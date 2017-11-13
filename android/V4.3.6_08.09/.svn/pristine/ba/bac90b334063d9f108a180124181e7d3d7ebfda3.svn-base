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
	 * 可见范围：全部可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_ALL = 1;
	/**
	 * 可见范围：岛邻可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_VIP = 2;
	/**
	 * 可见范围：好友可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_FRIEND = 3;
	
	public static final String NAME_CONTACT_VISIBLE_RANGE_ALL = "全部用户可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_VIP = "岛邻可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_FRIEND = "仅好友可见";
	
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
