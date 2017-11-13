package com.zhisland.lib.load;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.lib.util.StringUtil;

@DatabaseTable(tableName = UploadMultiInfo.TABLE_NAME, daoClass = UploadMultiDao.class)
public class UploadMultiInfo extends BaseLoadInfo {

	private static final long serialVersionUID = 1L;

	public static final String PATH_SPLIT = ",";
	public static final String TABLE_NAME = "upload_multi_info";

	public static final String COL_IMG_PATHS = "upload_multi_image_path";
	public static final String COL_ALL_IMG_PATHS = "upload_multi_all_image_path";
	public static final String COL_IMG_IDS = "upload_multi_image_id";
	public static final String COL_CREATE_TIME = "upload_multi_create_itme";
	public static final String COL_UPLOAD_TOKEN = "upload_multi_upload_token";

	/**
	 * 上传的图片路径集合
	 * */
	@DatabaseField(columnName = COL_ALL_IMG_PATHS)
	public String imgAllPaths;

	/**
	 * 剩下的，还未上传的图片路径集合
	 */
	@DatabaseField(columnName = COL_IMG_PATHS)
	public String imgPaths;

	/**
	 * 已经上传过的图片id
	 */
	@DatabaseField(columnName = COL_IMG_IDS)
	public String imgIds;

	@DatabaseField(columnName = COL_CREATE_TIME)
	public long createTime;

	/**
	 * 正在上传的任务token
	 */
	@DatabaseField(columnName = COL_UPLOAD_TOKEN)
	public long uploadToken;

	/**
	 * 获取 imagepath's 数组
	 * 
	 * @return
	 */
	public String[] getPaths() {
		String[] ss = StringUtil.isNullOrEmpty(imgPaths) ? null : imgPaths
				.split(PATH_SPLIT);
		return ss;
	}

	public void appenImgId(String imgId) {
		if (StringUtil.isNullOrEmpty(imgIds)) {
			this.imgIds = imgId;
		} else {
			this.imgIds = this.imgIds + PATH_SPLIT + imgId;
		}
	}

	/**
	 * 获取image id 数组
	 * 
	 * @return
	 */
	public String[] getImgIds() {
		return StringUtil.isNullOrEmpty(imgIds) ? null : imgIds
				.split(PATH_SPLIT);
	}

}
