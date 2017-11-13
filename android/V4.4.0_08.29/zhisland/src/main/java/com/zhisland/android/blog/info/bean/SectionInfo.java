package com.zhisland.android.blog.info.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

public class SectionInfo extends OrmDto {

	private static final long serialVersionUID = 1L;

	@SerializedName("day")
	public String day;

	@SerializedName("data")
	public ArrayList<ZHInfo> infos;
}
