package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 自定义字典
 */
public class CustomDict extends OrmDto {

	/**
	 * 常驻城市的别名，城市弹出城市选择器
	 */
	public static final String ALIAS_CITY = "city";
	/**
	 * 行业选择
	 */
	public static final String ALIAS_INDUSTRY = "industry";

	private static final long serialVersionUID = 1L;

	/**
	 * 分隔符
	 */
	private static final String SEPARATOR = ",";

	/**
	 * 是否可编辑 1 可编辑 0 不能编辑
	 */
	private static final int CAN_EDIT = 1;

	/**
	 * 是否是必选 1 是 0 否
	 */
	private static final int IS_MUST = 1;

	public CustomDict() {
	}

	public CustomDict(String name) {
		this.name = name;
	}

	@SerializedName("cdId")
	public String id;

	@SerializedName("name")
	public String name;// 名称

	@SerializedName("alias")
	public String alias;// 别名

	@SerializedName("value")
	public String value;// 值

	@SerializedName("prompt")
	public String hint;// 值

	@SerializedName("edit")
	public Integer edit;// 是否可编辑

	@SerializedName("must")
	public Integer must;// 是否必填

	/**
	 * length
	 */
	@SerializedName("length")
	public Integer length;// 总字符数

	/**
	 * count
	 */
	@SerializedName("count")
	public Integer count;

	/**
	 * 建立时间
	 */
	@SerializedName("createTime")
	public Long createTime;

	/**
	 * 是否可编辑
	 */
	public boolean isCanEdit() {
		if (edit != null) {
			return edit == CAN_EDIT;
		}
		return true;
	}

	/**
	 * 是否必填
	 */
	public boolean isMust() {
		if (must != null) {
			return must == IS_MUST;
		}
		return false;
	}

	/**
	 * 获取省份id
	 */
	public int getProvinceId() {
		if (alias != null && StringUtil.isEquals(alias, ALIAS_CITY)
				&& value.contains(SEPARATOR)) {
			String[] split = value.split(SEPARATOR);

			int proviceId = 0;
			try {
				proviceId = Integer.parseInt(split[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return proviceId;
		}
		return 0;
	}

	/**
	 * 获取城市id
	 */
	public int getCityId() {
		if (alias != null && StringUtil.isEquals(alias, ALIAS_CITY)
				&& value.contains(SEPARATOR)) {
			String[] split = value.split(SEPARATOR);

			int cityId = 0;
			if (split.length > 1) {
				try {
					cityId = Integer.parseInt(split[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return cityId;
		}
		return 0;
	}

	/**
	 * 设置城市
	 */
	public void setDictCity(int proviceId, int cityId) {
		if (alias != null && StringUtil.isEquals(alias, ALIAS_CITY)) {
			value = proviceId + SEPARATOR + cityId;
		}
	}
}
