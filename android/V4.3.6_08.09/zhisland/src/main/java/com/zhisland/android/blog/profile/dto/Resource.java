package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

import java.util.List;

/**
 * 用户资源与需求
 */
public class Resource extends OrmDto {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_SUPPLY = 1;
	public static final int TYPE_DEMAND = 2;

	/**
	 * 供给id
	 * */
	@SerializedName("urId")
	public Long id;

	/**
	 * 供给内容
	 * */
	@SerializedName("content")
	public String content;

	/**
	 * 行业标签ID
	 * */
	@SerializedName("industryTags")
	public String industryTags;

	/**
	 * 类别标签ID
	 * */
	@SerializedName("categoryTags")
	public String categoryTags;

	/**
	 * 需求还是供给。 1:需求 2：供给
	 * */
	@SerializedName("type")
	public Integer type;

	/**
	 * 创建时间
	 * */
	@SerializedName("publicTime")
	public String publicTime;

	/**
	 * 发布人信息
	 * */
	@SerializedName("userinfo")
	public User user;

	/**
	 * 获取行业名称
	 * */
	public String getIndustryName() {
		if (!StringUtil.isNullOrEmpty(industryTags)) {
			List<ZHDicItem> list = Dict.getInstance().getProfileIndustry();
			int count = list == null ? 0 : list.size();
			for (int i = 0; i < count; i++) {
				if (list.get(i).key.equals(industryTags)) {
					return list.get(i).name;
				}
			}
		}
		return "";
	}

	/**
	 * 获取类别名称
	 * */
	public String getCategoryName() {
		if (!StringUtil.isNullOrEmpty(categoryTags) && type != null) {
			List<ZHDicItem> list = null;
			if (type == TYPE_SUPPLY) {
				list = Dict.getInstance().getSupplyCategory();
			} else if (type == TYPE_DEMAND) {
				list = Dict.getInstance().getDemandCategory();
			}
			int count = list == null ? 0 : list.size();
			for (int i = 0; i < count; i++) {
				if (list.get(i).key.equals(categoryTags)) {
					return list.get(i).name;
				}
			}
		}
		return "";
	}

	/**
	 * 获取行业ZHDicItem对象
	 * */
	public ZHDicItem getIndustryObj() {
		if (!StringUtil.isNullOrEmpty(industryTags)) {
			List<ZHDicItem> list = Dict.getInstance().getProfileIndustry();
			int count = list == null ? 0 : list.size();
			for (int i = 0; i < count; i++) {
				if (list.get(i).key.equals(industryTags)) {
					return list.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 获取类别ZHDicItem对象
	 * */
	public ZHDicItem getCategoryObj() {
		if (!StringUtil.isNullOrEmpty(categoryTags) && type != null) {
			List<ZHDicItem> list = null;
			if (type == TYPE_SUPPLY) {
				list = Dict.getInstance().getSupplyCategory();
			} else if (type == TYPE_DEMAND) {
				list = Dict.getInstance().getDemandCategory();
			}
			int count = list == null ? 0 : list.size();
			for (int i = 0; i < count; i++) {
				if (list.get(i).key.equals(categoryTags)) {
					return list.get(i);
				}
			}
		}
		return null;
	}

}
