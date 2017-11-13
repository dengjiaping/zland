package com.zhisland.android.blog.profile.eb;

import com.zhisland.android.blog.profile.dto.SimpleBlock;

/**
 * 个人页 eventbus
 */
public class EBProfile {

	/**
	 * 基本信息变更
	 */
	public static final int TYPE_CHANGE_INTRODUCTION = SimpleBlock.TYPE_USER_INTRODUCTION;
	
	/**
	 * 资源变更
	 */
	public static final int TYPE_CHANGE_SUPPLY = SimpleBlock.TYPE_SUPPLY;
	
	/**
	 * 需求变更
	 */
	public static final int TYPE_CHANGE_DEMAND = SimpleBlock.TYPE_DEMAND;
	
	/**
	 * 评论变更
	 */
	public static final int TYPE_CHANGE_COMMENT = SimpleBlock.TYPE_USER_COMMENT;
	
	/**
	 * 任职公司变更
	 */
	public static final int TYPE_CHANGE_POSITION = SimpleBlock.TYPE_POSITION;
	
	/**
	 * 联系方式变更
	 */
	public static final int TYPE_CHANGE_CONTACT_INFO = SimpleBlock.TYPE_CONTACT;
	
	/**
	 * 助理变更
	 */
	public static final int TYPE_CHANGE_ASSISTANT = SimpleBlock.TYPE_ASSISTANT;
	
	/**
	 * 荣誉变更
	 */
	public static final int TYPE_CHANGE_HONOR = SimpleBlock.TYPE_HONOR;
	
	/**
	 * 点滴变更
	 */
	public static final int TYPE_CHANGE_DRIP = SimpleBlock.TYPE_DRIP;

	private int type;
	private Object obj;

	public EBProfile(int type) {
		this.type = type;
	}

	public EBProfile(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getData() {
		return obj;
	}

}
