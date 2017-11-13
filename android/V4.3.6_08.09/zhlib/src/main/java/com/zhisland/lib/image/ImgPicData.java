package com.zhisland.lib.image;

import java.io.Serializable;
import java.util.List;

public class ImgPicData {

	/**
	 * 单个文件
	 */
	public static class ImageItem implements Serializable {

		private static final long serialVersionUID = 1L;

		public String imageId;
		/**
		 * most is null
		 */
		public String thumbnailPath;
		public String imagePath;
		public boolean isSelected = false;
	}

	/**
	 * 相册文件夹
	 */
	public static class ImageBucket {

		public int selectCount;
		public int count = 0;
		public String bucketName;
		public List<ImageItem> imageList;
	}

}
