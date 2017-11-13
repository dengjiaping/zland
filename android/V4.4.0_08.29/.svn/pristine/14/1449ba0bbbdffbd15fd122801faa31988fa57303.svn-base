package com.zhisland.lib.load;

import com.j256.ormlite.field.DatabaseField;
import com.zhisland.lib.OrmDto;

public class BaseLoadInfo extends OrmDto {

	private static final long serialVersionUID = 1L;

	public static final String COL_TOKEN = "ftoken";
	public static final String COL_OWNER_ID = "owner_id";
	public static final String COL_PRIORITY = "priority";
	public static final String COL_STATUS = "status";
	public static final String COL_STATUS_DESC = "status_desc";
	public static final String COL_FILEPATH = "filpath";

	/**
	 * 本地纪录的一个唯一的上传标识
	 */
	@DatabaseField(generatedId = true, columnName = COL_TOKEN)
	public long token;

	/**
	 * 所属人标识
	 */
	@DatabaseField(columnName = COL_OWNER_ID)
	public long ownerId;

	/**
	 * 优先级。（可选值在LoadPriority）
	 * */
	@DatabaseField(columnName = COL_PRIORITY)
	public int priority;

	/**
	 * 任务状态。（可选值在LoadStatus）
	 * */
	@DatabaseField(columnName = COL_STATUS)
	public int status;

	/**
	 * 任务状态描述
	 * */
	@DatabaseField(columnName = COL_STATUS_DESC)
	public int statusDesc;

	/**
	 * 文件本地地址
	 * */
	@DatabaseField(columnName = COL_FILEPATH)
	public String filePath;

	@Override
	public String toString() {
		return String.format("token:%d, status:%d", token, status);
	}
}
