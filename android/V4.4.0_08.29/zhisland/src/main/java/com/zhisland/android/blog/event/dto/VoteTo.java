package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * 投票题
 * 
 * @author arthur
 *
 */
public class VoteTo extends OrmDto {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 投票ID
	 * */
	@SerializedName("id")
	public long id;

	/**
	 * 活动ID
	 * */
	@SerializedName("eventId")
	public long eventId;

	/**
	 * 投票的顺序
	 * */
	@SerializedName("order")
	public int order;

	/**
	 * 投票的主题描述
	 * */
	@SerializedName("desc")
	public String desc;
	
	/**
	 * 投票对应的选项，并且保证选项的顺序
	 * */
	@SerializedName("options")
	public ArrayList<VoteOptionTo> options;

	/**
	 * 是否多选
	 * */
	@SerializedName("isMulti")
	public boolean isMulti;

	/**
	 * 是否必选
	 * */
	@SerializedName("required")
	public boolean required;
	
}
