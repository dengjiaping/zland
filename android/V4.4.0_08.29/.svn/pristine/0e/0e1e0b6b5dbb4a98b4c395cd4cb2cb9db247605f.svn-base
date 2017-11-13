package com.zhisland.lib.load;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.image.ImgBrowsable;
import com.zhisland.lib.util.StringUtil;

public class ZHPicture implements ImgBrowsable {

	private static final long serialVersionUID = 1L;

	public ZHPicture() {

	}

	public ZHPicture(String id, String url) {
		this.id = id;
		this.url = url;
	}

	public ZHPicture(String id, String url, String largeUrl) {
		this.id = id;
		this.url = url;
		this.largeUrl = largeUrl;
	}

	@SerializedName("id")
	public String id;

	@SerializedName("url")
	public String url;

	@SerializedName("large_url")
	public String largeUrl;

	@Override
	public String url() {
		if (!StringUtil.isNullOrEmpty(largeUrl)) {
			return largeUrl;
		} else if (url.startsWith("http")) {
			return ZHPicture.GetPicLUrl(url);
		} else {
			return url;
		}
	}

	/**
	 * 获取一个url的中等图片URL
	 * 
	 * @param avatarUrl
	 * @return
	 */
	public static String GetPicMUrl(String avatarUrl) {
		return InsertSubStrBeforeExt(avatarUrl, "_m");
	}

	/**
	 * 获取一个url的大图片URL
	 * 
	 * @param avatarUrl
	 * @return
	 */
	public static String GetPicLUrl(String avatarUrl) {
		return InsertSubStrBeforeExt(avatarUrl, "_l");
	}

	private static String InsertSubStrBeforeExt(String url, String subStr) {
		if (url != null && url.length() > 4) {
			int lastDotIdx = url.lastIndexOf('.');
			if (lastDotIdx > 0) {
				StringBuilder strBuilder = new StringBuilder(url);
				strBuilder.insert(lastDotIdx, subStr);
				return strBuilder.toString();
			}
		}

		return null;
	}

}
