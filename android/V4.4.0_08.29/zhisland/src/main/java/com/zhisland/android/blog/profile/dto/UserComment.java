package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * 个人页神评论
 */
public class UserComment extends OrmDto {

	private static final long serialVersionUID = 1L;

	@SerializedName("ucId")
    public Long id;//神评论ID

	@SerializedName("content")
	public String content;//评论内容

	@SerializedName("isNew")
	public Integer isNew;//是否为最新评论

	@SerializedName("publisher")
	public User publisher;//发布者对象

	@SerializedName("toUser")
	public User toUser; //被评价人对象

	@SerializedName("topTime")
	public Long topTime;//置顶时间

	@SerializedName("createTime")
	public long createTime;

}
