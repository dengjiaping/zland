package com.zhisland.android.blog.contacts.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 机构
 */
public class Organization extends OrmDto {

	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public int id; // 机构ID

	@SerializedName("logo")
	public String logo; // 机构logo

	@SerializedName("name")
	public String name; // 机构名称

	@SerializedName("position")
	public String position; // 机构角色

	@SerializedName("useBadge")
	public int useBadge; // 是否使用徽章

	@SerializedName("badgeLevelImg")
	public String badgeLevelImg; // 徽章图片

	@SerializedName("joinedTime")
	public String joinedTime; // 颁发时间
}
