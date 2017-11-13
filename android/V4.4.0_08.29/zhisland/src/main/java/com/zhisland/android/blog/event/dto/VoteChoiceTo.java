package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

import java.util.ArrayList;

/**
 * 投票选择数据
 * 
 * @author arthur
 * 
 */
public class VoteChoiceTo extends OrmDto {

	private static final long serialVersionUID = 1L;

	/**
	 * 投票ID
	 * */
	@SerializedName("voteId")
	public long voteId;

	/**
	 * 已选的投票选项id拼接成的string ，例如：1,3,5
	 * */
	@SerializedName("choice")
	public String choice;

	/**
	 * 已选的投票选项。
	 * */
	@GsonExclude
	public ArrayList<VoteOptionTo> optionChoiced = new ArrayList<VoteOptionTo>();

	/**
	 * 根据已选的投票选项(optionChoiced)生成投票选项id拼接成的string（choice），并返回自身对象。
	 * 
	 * @return VoteChoiceTo 自身对象。
	 * */
	public synchronized VoteChoiceTo makeChoiceStringByList() {
		choice = "";
		for (int i = 0; i < optionChoiced.size(); i++) {
			choice += optionChoiced.get(i).id;
			if (i != optionChoiced.size() - 1) {
				choice += ",";
			}
		}
		return this;
	}

}
