package com.zhisland.android.blog.profile.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 基础块，数据结构。
 */
public class SimpleBlock<T> extends OrmDto {

	private static final long serialVersionUID = 1L;

	/**
	 * (1, "简介"), (2, "职位"), (3, "联系方式"), (4, "岛丁"), (5, "助理"), (6, "评论"), (7,
	 * "荣誉"), (8, "资源"), (9, "需求"), (10, "点滴"),
	 */
	// 个人简介 type
	public static final int TYPE_USER_INTRODUCTION = 1;
	// 任职 type
	public static final int TYPE_POSITION = 2;
	// 联系方式 type
	public static final int TYPE_CONTACT = 3;
	// 服务岛丁 type
	public static final int TYPE_DING = 4;
	// 助理 type
	public static final int TYPE_ASSISTANT = 5;
	// 神评论 type
	public static final int TYPE_USER_COMMENT = 6;
	// 荣誉 type
	public static final int TYPE_HONOR = 7;
	// 供给 type
	public static final int TYPE_SUPPLY = 8;
	// 需求 type
	public static final int TYPE_DEMAND = 9;
	// 点滴 type
	public static final int TYPE_DRIP = 10;
	// 共同好友 type
	public static final int TYPE_COMMON_FRIENDS = 11;
	// 相册
	public static final int TYPE_PHOTO_GALLERY = 12;
	//资源需求 (包含资源和需求)
	public static final int TYPE_RESOURCE= 13;
	//联系人组 (个人基本信息，服务岛丁，助理，认证人 )
	public static final int TYPE_CONTACT_GROUP = 14;
	/**
	 * 块标题
	 * */
	@SerializedName("title")
	public String title;

	/**
	 * 数据列表
	 * */
	@SerializedName("datas")
	public List<T> data;

	/**
	 * 数据总数
	 * */
	@SerializedName("total")
	public Integer total;

	/**
	 * 类型
	 * */
	@SerializedName("type")
	public Integer type;

	/**
	 * 最大发布数
	 * */
	@SerializedName("maxCount")
	public Integer maxCount;

}
