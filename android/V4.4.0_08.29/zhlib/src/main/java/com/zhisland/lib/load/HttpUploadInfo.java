package com.zhisland.lib.load;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 2014正和通版本的新文件上传协议
 * 
 */
@DatabaseTable(tableName = HttpUploadInfo.TABLE_NAME, daoClass = HttpUploadDao.class)
public class HttpUploadInfo extends BaseLoadInfo {

	private static final long serialVersionUID = 1L;

	public static final int TYPE_PIC = 1, TYE_AUDIO = 2, TYPE_VIDEO = 3;

	public static final String TABLE_NAME = "http_upload_info";
	public static final String COL_HASHCODE = "fhashcde";
	public static final String COL_TYPE = "filetype";
	public static final String COL_CUR_BLOCK = "current_block";
	public static final String COL_TOTAL_BLOCK = "total_block";
	public static final String COL_FILE_EXT = "file_extension";
	public static final String COL_TIME = "file_duration";
	public static final String COL_SIZE = "file_size";
	public static final String COL_BLOCK_SIZE = "file_block_size";

	// ===========local fields=========

	@DatabaseField(columnName = "pic_id")
	public String picId;

	@DatabaseField(columnName = "pic_url")
	public String picUrl;

	// ===========used to upload fields========
	/**
	 * 本次上传唯一ID
	 */
	@DatabaseField(columnName = COL_HASHCODE)
	public String hashcode;

	/**
	 * 资源类型{@link HttpUploadInfo#TYE_AUDIO, #TYPE_PIC, #TYPE_VIDEO}
	 */
	@DatabaseField(columnName = COL_TYPE)
	public int type;

	/**
	 * 当前上传块索引。如果cblock 为－1，表示查询已上传块数
	 */
	@DatabaseField(columnName = COL_CUR_BLOCK)
	public long curBlock;

	/**
	 * 总共块数
	 */
	@DatabaseField(columnName = COL_TOTAL_BLOCK)
	public long totalBlocks;

	/**
	 * 文件扩展名
	 */
	@DatabaseField(columnName = COL_FILE_EXT)
	public String ext;

	/**
	 * 音、视频文件播放时长
	 */
	@DatabaseField(columnName = COL_TIME)
	public long time;

	/**
	 * 视频文件大小
	 */
	@DatabaseField(columnName = COL_SIZE)
	public long size;

	/**
	 * 每块包大小
	 */
	@DatabaseField(columnName = COL_BLOCK_SIZE)
	public int blockSize;

	/**
	 * 获取上传的百分比
	 * 
	 * @return
	 */
	public int getPercent() {
		return (int) (((double) curBlock / (double) totalBlocks) * 100);
	}
}
