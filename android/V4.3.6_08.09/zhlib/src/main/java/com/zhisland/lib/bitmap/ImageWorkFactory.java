package com.zhisland.lib.bitmap;

import java.io.File;

import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;

/**
 * app图片处理器 ，包括加载、缓存、样式修改等
 * 
 * 使用方法：ImageWorkFactory.getFetcher().loadImage(imgPath, imageView,
 * R.drawable.palce);
 * 
 * 参数说明： 图片url/ImageView/图片未加载出来或者图片加载错误时显示默认图片
 */
public class ImageWorkFactory {

	/**
	 * 正常展示图片
	 */
	private static ImageWorker normalWorker = null;

	/**
	 * 圆形图片
	 */
	private static ImageWorker circleWorker = null;

	/**
	 * 本地大图
	 */
	private static ImageLocalFetcher localFeatcher = null;

	/**
	 * 初始化fetchers 和删除老的临时文件
	 */
	public static void initFetchers() {

		normalWorker = new ImageFetcher(ZHApplication.APP_CONTEXT);
		normalWorker.setImageCache(ImageCache.getInstance());

		circleWorker = new ImageCircleFetcher(ZHApplication.APP_CONTEXT);
		circleWorker.setImageCache(ImageCache.getHeaderCacheInstance());

		localFeatcher = new ImageLocalFetcher(ZHApplication.APP_CONTEXT);
		localFeatcher.setImageCache(ImageCache.getInstance());

		SSLHelper.trustAllHosts();

		try {
			File originalDir = FileMgr.Instance().getDir(DirType.TMP);
			if (originalDir.exists()) {
				File[] files = originalDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		} catch (Exception ex) {

		}

	}

	public static ImageWorker getFetcher() {
		return normalWorker;
	}

	public static ImageWorker getCircleFetcher() {
		return circleWorker;
	}

	public static ImageWorker getLocalFetcher() {
		return localFeatcher;
	}
}
