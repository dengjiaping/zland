package com.zhisland.lib.load;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = HttpDownloadInfo.TABLE_NAME, daoClass = HttpDownloadDao.class)
public class HttpDownloadInfo extends BaseLoadInfo {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "download_info";
	public static final String COL_REMOTE_ID = "remote_id";

	/**
	 * 下载地址
	 * */
	@DatabaseField(columnName = COL_REMOTE_ID)
	public String downUrl;

	/**
	 * 文件总大小
	 * */
	@DatabaseField(columnName = "total_size")
	public long totalSize;

	/**
	 * 结束位置
	 * */
	@DatabaseField(columnName = "end_index")
	public long endIndex;
}
