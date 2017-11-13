package com.zhisland.lib.load;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

public class UploadMultiMgr extends BaseLoadMgr<UploadMultiInfo> {

	private static final String TAG = "pamgr";

	private LoadDbHelper dbHelper;

	public static UploadMultiMgr Instance() {
		return (UploadMultiMgr) ZHLoadManager.Instance().getLoadMgr(
				UploadMultiMgr.class);
	}

	/**
	 * 上传多个图片
	 * 
	 * @param imgIds
	 *            编辑时，已有的image id
	 * @param imgPaths
	 */
	public static long UploadImages(Context context, long ownerId,
			ArrayList<String> imgAllPaths) {
		if (imgAllPaths == null || imgAllPaths.size() < 1 )
			return -1;

		UploadMultiInfo pi = new UploadMultiInfo();
		pi.ownerId = ownerId;
		pi.imgAllPaths = StringUtil.convertFromArr(imgAllPaths,
				UploadMultiInfo.PATH_SPLIT);
		pi.imgPaths = StringUtil.convertFromArr(imgAllPaths,
				UploadMultiInfo.PATH_SPLIT);
		pi.createTime = System.currentTimeMillis() / 1000;
		UploadMultiInfo info = UploadMultiMgr.Instance().startLoad(context, pi);
		return info == null ? -1 : info.token;
	}

	// =============static methods==============
	public UploadMultiMgr() {
		super();
		dbHelper = LoadDbHelper.getHelper();

	}

	public void onEvent(HttpUploadInfo info) {
		long uploadToken = info.token;
		if (info.status == LoadStatus.FINISH) {
			HttpUploadInfo uploadInfo = ZHLoadManager.Instance()
					.getHttpUploadMgr().getLoadInfo(uploadToken);

			UploadMultiInfo postInfo = dbHelper.getUpmultiDao()
					.getLoadInfoByUploadToken(uploadToken);
			if (postInfo != null) {
				postInfo.appenImgId(uploadInfo.picId);
				postInfo.uploadToken = 0;
				dbHelper.getUpmultiDao().updateLoadInfo(postInfo);
				loadFile(postInfo);
			}
		} else if (info.status == LoadStatus.ERROR) {
			UploadMultiInfo postInfo = dbHelper.getUpmultiDao()
					.getLoadInfoByUploadToken(uploadToken);
			if (postInfo != null) {
				failLoad(postInfo.token, LoadStatus.ERROR);
			}
		}
	}

	@Override
	public List<Long> fetchLoad(int count, ArrayList<Long> curLoads) {
		return dbHelper.getUpmultiDao().fetchLoadInfo(count, curLoads);
	}

	@Override
	public void loadFile(UploadMultiInfo info) {
		MLog.d(TAG, "upload activity");
		String[] imgPaths = info.getPaths();
		if (info.uploadToken > 0) {
			ZHLoadManager.Instance().getHttpUploadMgr()
					.startByToken(context, info.uploadToken);
		} else if (imgPaths == null || imgPaths.length < 1) {
			// has no images or all image had been uploaded
			finishLoad(info.token, null);
		} else {
			String imgPath = imgPaths[0];
			File file = new File(imgPath);
			if (file.exists()) {
				long token = ZHLoadManager.uploadFile(imgPath, info.ownerId,
						UUID.randomUUID().toString(),
						(int) HttpUploadInfo.TYPE_PIC, 0, LoadPriority.HIGH);
				info.uploadToken = token;
				imgPaths[0] = null;
				info.imgPaths = StringUtil.convertFromArr(imgPaths,
						UploadMultiInfo.PATH_SPLIT);
				dbHelper.getUpmultiDao().updateLoadInfo(info);
			} else {
				info.appenImgId(imgPath);
				info.uploadToken = 0;
				imgPaths[0] = null;
				info.imgPaths = StringUtil.convertFromArr(imgPaths,
						UploadMultiInfo.PATH_SPLIT);
				dbHelper.getUpmultiDao().updateLoadInfo(info);
				loadFile(info);
			}

		}
	}

	@Override
	public UploadMultiInfo getLoadInfo(long token) {
		return dbHelper.getUpmultiDao().getLoadInfo(token);
	}

	@Override
	public long insert(UploadMultiInfo info) {
		return dbHelper.getUpmultiDao().insertLoadInfo(info);
	}

	@Override
	public int deleteByOwnerId(long ownerId) {
		return dbHelper.getUpmultiDao().deleteLoad(ownerId);
	}

	@Override
	public int updateStatusByToken(long token, int status) {
		return dbHelper.getUpmultiDao().updateLoadStatus(token, status);
	}

	@Override
	public int updateStatusByOwnerId(long ownerId, int status) {
		return dbHelper.getUpmultiDao()
				.updateLoadStatusByOwner(ownerId, status);
	}

	@Override
	public int deleteByToken(long token) {
		int i = dbHelper.getUpmultiDao().deleteByToken(token);
		if (i > 0) {
			postEvent(token);
		}
		return i;
	}

}
