package com.zhisland.android.blog.contacts.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IMPageData<T> implements Serializable {

	private static final long serialVersionUID = -5607262592780784855L;

	//同步的状态 0-未结束 1-同步完成
	@SerializedName("syncStatus")
	public int syncStatus;

	@SerializedName("version")
	public String version;

	//同步的数据
	@SerializedName("data")
	public ArrayList<T> data;
}
