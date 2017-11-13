package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * 用户联系方式数据结构
 */
public class UserContactInfo extends OrmDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 可见范围：全部可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_ALL = 1;
	/**
	 * 可见范围：岛邻可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_FRIEND = 3;
	/**
	 * 可见范围：好友可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_NO = 4;
	
	public static final String NAME_CONTACT_VISIBLE_RANGE_ALL = "岛邻/海客/岛丁 可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_FRIEND = "仅好友可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_NO = "保密";
	
	/**
	 * 用户姓名
	 * */
	@SerializedName("userName")
    public String userName;
	
	/**
	 * 用户头像
	 * */
	@SerializedName("userAvatar")
    public String userAvatar;
	
	/**
	 * 联系手机国家码
	 * */
	@SerializedName("countryCode")
    public String countryCode;
	
	/**
	 * 联系手机号
	 * */
	@SerializedName("mobile")
	public String mobile;
	
	/**
	 * 邮箱
	 * */
	@SerializedName("email")
	public String email;
	
	/**
	 * 可见范围
	 * */
	@SerializedName("visibleRange")
	public Integer visibleRange;
	
	/**
	 * 基础信息
	 * */
	@SerializedName("sex")
	public Integer sex;
	
	/**
	 * 用户类型
	 */
	@SerializedName("userType")
	public Integer userType;

	/**
	 * 当前查看人类型
	 * "USERE",  //当前查看的用户信息
	 * "GARDANER", //当前查看用户的岛丁
	 * "ASSISTANT", //当前查看用户的助理
	 * "REFEREE",//当前查看用户的认证人
	 */
	@SerializedName("contactType")
	public String contactType;

	/**
	 * 用户姓名
	 */
	@SerializedName("userId")
	public long userId;

	@SerializedName("activity")
	public int activity = User.VALUE_ACTIVITY_NORMAL;

}
