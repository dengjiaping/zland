package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 投票选择项
 * 
 * @author arthur
 * 
 */
public class VoteOptionTo extends OrmDto {

	private static final long serialVersionUID = 1L;

	/** 投票选项ID */
	@SerializedName("id")
	public long id;
	/** 投票ID */
	@SerializedName("voteId")
	public long voteId;
	/** 选项的顺序 */
	@SerializedName("order")
	public int order;
	/** 选项的描述 */
	@SerializedName("desc")
	public String desc;

}
