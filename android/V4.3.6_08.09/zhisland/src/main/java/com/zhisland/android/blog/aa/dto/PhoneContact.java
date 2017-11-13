package com.zhisland.android.blog.aa.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

public class PhoneContact extends OrmDto {
	/**
	 * 将本地的通讯录变成json时，要用到这个数据结构
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("name")
	public String name;
	@SerializedName("phones")
	public ArrayList<String> phones;
}
