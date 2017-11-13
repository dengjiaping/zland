package com.zhisland.lib.load;

public class LoadConstants {

	public static final String ENCODING = "UTF-8";
	public static final String RANGE_REQ = "Range";
	public static final String RANGE_RES = "Content-Range";
	public static final int BIG_FILE_BLOCK_SIZE = 400 * 1024;// upload block size
	public static final int RANGE_SIZE = 100 * 1024;// download rang size

	public static String getMediaMessageExt(int type) {
		switch (type) {
		case HttpUploadInfo.TYPE_PIC:
			return "jpg";
		case HttpUploadInfo.TYE_AUDIO:
			return "amr";
		case HttpUploadInfo.TYPE_VIDEO:
			return "mp4";
		}
		return null;
	}

}
