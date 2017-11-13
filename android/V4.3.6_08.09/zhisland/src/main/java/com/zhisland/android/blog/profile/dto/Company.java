package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.OrmDto;

/**
 * 公司
 */
public class Company extends OrmDto {

	public static final int APPROVE_YES = 1;
	public static final int APPROVE_NO = 0;
	
	private static final long serialVersionUID = 1L;

	public static final String POSTFIX = "_company";
	/**
	 * 公司id
	 */
	@SerializedName("id")
	public long companyId;

	/**
	 * 公司logo
	 */
	@SerializedName("logo")
	public String logo;

	/**
	 * 公司名称
	 */
	@SerializedName("name")
	public String name;

	/**
	 * 公司行业
	 */
	@SerializedName("industry")
	public ZHDicItem industry;

	/**
	 * 公司是否已认证, 0 非， 1 是
	 */
	@SerializedName("isAuthentication")
	public Integer isAuthentication;

	/**
	 * 海客认证公司标识 1 为认证，空或0都不是。
	 */
	@SerializedName("isClientAuth")
	public Integer isClientAuth;

	/**
	 * 公司是否默认
	 */
	@SerializedName("isDefault")
	public Integer isDefault;
	
	/**
	 * 公司所在城市id
	 */
	@SerializedName("cityId")
	public Integer cityId;

	/**
	 * 公司所在省id
	 */
	@SerializedName("provinceId")
	public Integer provinceId;

	/**
	 * 公司官网
	 */
	@SerializedName("website")
	public String website;

	/**
	 * 公司股票代码
	 */
	@SerializedName("stockCode")
	public String stockCode;

	/**
	 * 公司简介
	 */
	@SerializedName("desc")
	public String desc;

	/**
	 * 公司职位
	 */
	@SerializedName("position")
	public String position;

	/**
	 * 公司发展阶段(初创，筹备，上市，其它等)
	 */
	@SerializedName("stage")
	public String stage;

	/**
	 * 公司类型(商业，非商业，社会机构等)
	 */
	@SerializedName("type")
	public String type;

}
