package com.zhisland.android.blog.aa.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

public class LoginResponse extends OrmDto {

	private static final long serialVersionUID = 1L;

	@SerializedName("user")
	public User user;

	@SerializedName("access_token")
	public String token;

	@SerializedName("info_status")
	public Integer basicInfoStatus;
}
