package com.zhisland.lib.load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.zhisland.lib.component.application.ZHApplication;

public class ZHLoadManager {

	private static final Object lockObj = new Object();
	private static ZHLoadManager instance;

	private HttpUploadMgr httpUploadMgr;
	private HttpDownLoadMgr httpDownMgr;
	private UploadMultiMgr upmultiMgr;

	private ArrayList<Class<?>> classes;
	private HashMap<Class<?>, BaseLoadMgr<?>> mgrs;

	public static ZHLoadManager Instance() {
		if (instance == null) {
			synchronized (lockObj) {
				if (instance == null) {
					instance = new ZHLoadManager();
				}
			}
		}
		return instance;
	}

	private ZHLoadManager() {
		mgrs = new HashMap<Class<?>, BaseLoadMgr<?>>();
		classes = new ArrayList<Class<?>>();

		registerLoadMgr(HttpUploadMgr.class);
		registerLoadMgr(HttpDownLoadMgr.class);
		registerLoadMgr(UploadMultiMgr.class);
	}

	/**
	 * register an load manager, which will be scheduled in future
	 * 
	 * @param cls
	 *            the class of manager
	 */
	public void registerLoadMgr(Class<? extends BaseLoadMgr<?>> cls) {
		try {
			mgrs.put(cls, cls.newInstance());
			classes.add(cls);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Class<?>> getMgrClasses() {
		return classes;
	}

	public BaseLoadMgr<?> getLoadMgr(Class<?> cls) {
		return mgrs.get(cls);
	}

	public BaseLoadMgr<UploadMultiInfo> getUploadMultiMgr() {
		upmultiMgr = (UploadMultiMgr) mgrs.get(UploadMultiMgr.class);
		return upmultiMgr;
	}

	public BaseLoadMgr<HttpUploadInfo> getHttpUploadMgr() {
		httpUploadMgr = (HttpUploadMgr) mgrs.get(HttpUploadMgr.class);
		return httpUploadMgr;
	}

	public BaseLoadMgr<HttpDownloadInfo> getHttpDownMgr() {
		httpDownMgr = (HttpDownLoadMgr) mgrs.get(HttpDownLoadMgr.class);
		return httpDownMgr;
	}

	public void stop(long uid) {
		httpDownMgr.stopByOwnerId(uid);
	}

	public void delete(long uid) {
		httpDownMgr.deleteByOwnerId(uid);
	}

	/**
	 * 上传多张图片
	 * @param context
	 * @param ownerId 所属人标识
	 * @param imgAllPaths 上传的图片本地路径集合
	 * */
	public static long UploadImages(Context context, long ownerId,
			ArrayList<String> imgAllPaths) {
		return UploadMultiMgr.UploadImages(context, ownerId, imgAllPaths);
	}

	/**
	 * 上传文件
	 * @param localPath 文件本地地址
	 * @param ownerId 所属人标识
	 * @param hashCode 本次上传唯一ID
	 * @param type 文件类型
	 *            see {@link HttpNewUploadInfo#TYE_AUDIO,
	 *            HttpNewUploadInfo#TYPE_PIC, HttpNewUploadInfo#TYPE_VIDEO}
	 * @param duration 音、视频文件播放时长
	 * @param priority 优先级
	 * @return
	 */
	public static long uploadFile(String localPath, long ownerId,
			String hashCode, int type, long duration, int priority) {
		File file = new File(localPath);
		if (!file.exists()) {
			return -1;
		}

		HttpUploadInfo upInfo = new HttpUploadInfo();
		upInfo.hashcode = hashCode;
		upInfo.ownerId = ownerId;
		upInfo.curBlock = 0;
		upInfo.ext = LoadConstants.getMediaMessageExt(type);
		upInfo.type = type;
		upInfo.filePath = localPath;
		upInfo.blockSize = LoadConstants.BIG_FILE_BLOCK_SIZE;
		upInfo.time = duration;
		upInfo.totalBlocks = (file.length() + LoadConstants.BIG_FILE_BLOCK_SIZE - 1)
				/ LoadConstants.BIG_FILE_BLOCK_SIZE;
		upInfo.size = file.length();
		upInfo.status = LoadStatus.LOADING;

		if (priority < 0) {
			priority = LoadPriority.HIGH;
		}
		upInfo.priority = priority;
		upInfo = ZHLoadManager.Instance().getHttpUploadMgr()
				.startLoad(ZHApplication.APP_CONTEXT, upInfo);

		return upInfo.token;
	}

	/**
	 * 下载文件
	 * @param context
	 * @param token 下载任务的唯一标示
	 * @param url 下载地址
	 * @param localPath 本地地址
	 * @param ownerId 持有者id
	 * */
	public static long download(Context context, long token, String url,
			String localPath, long ownerId) {
		HttpDownloadInfo info = LoadDbHelper.getHelper().getHttpDownDao()
				.getLoadInfo(token);
		if (info == null) {
			// new download
			info = new HttpDownloadInfo();
			info.endIndex = 0;
			info.downUrl = url;
			info.ownerId = ownerId;
			info.filePath = localPath;
			info.priority = LoadPriority.HIGH;
			info.status = LoadStatus.LOADING;
			info = ZHLoadManager.Instance().getHttpDownMgr()
					.startLoad(context, info);
		}

		return info.token;
	}

}
