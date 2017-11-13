package com.zhisland.android.blog.common.upapp;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ZHUpgrade implements Serializable {

	public static final int FORCE_UPDATE = 1;
	
	private static final long serialVersionUID = 5571893237446830718L;

	/**
	 * 下载url
	 */
	@SerializedName("load_url")
	public String url;

	/**
	 * 最新版本号
	 */
	@SerializedName("version")
	public String version;

	/**
	 * 更新信息
	 */
	@SerializedName("msg")
	public String msg;

	/**
	 * 是否为强制升级
	 */
	@SerializedName("is_force")
	public int isForce;
}
