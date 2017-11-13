package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
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
	public static final int VALUE_CONTACT_VISIBLE_RANGE_VIP = 2;
	/**
	 * 可见范围：好友可见
	 * */
	public static final int VALUE_CONTACT_VISIBLE_RANGE_FRIEND = 3;
	
	public static final String NAME_CONTACT_VISIBLE_RANGE_ALL = "全部用户可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_VIP = "岛邻可见";
	public static final String NAME_CONTACT_VISIBLE_RANGE_FRIEND = "仅好友可见";
	
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
}
