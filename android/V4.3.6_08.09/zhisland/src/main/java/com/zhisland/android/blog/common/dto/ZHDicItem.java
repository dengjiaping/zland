package com.zhisland.android.blog.common.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 字典数据
 */
public class ZHDicItem extends OrmDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public ZHDicItem() {
	}

	public ZHDicItem(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public ZHDicItem(int code, String name) {
		this.code = code;
		this.name = name;
	}

	// 字典代码
	public int code;

	// 字典key,解决字符串类型code
	@SerializedName("tagId")
	public String key;

	// 字典名称
	@SerializedName("tagName")
	public String name;

}
