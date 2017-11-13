package com.zhisland.lib.load;

import com.zhisland.lib.util.MLog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpDownLoadMgr extends BaseLoadMgr<HttpDownloadInfo> {

	private LoadDbHelper dbHelper;

	public HttpDownLoadMgr() {
		super();
		dbHelper = LoadDbHelper.getHelper();
	}

	@Override
	public List<Long> fetchLoad(int count, ArrayList<Long> curLoads) {
		return dbHelper.getHttpDownDao().fetchLoadInfo(count, curLoads);
	}

	@Override
	public void loadFile(final HttpDownloadInfo info) {
		MLog.d(TAG, "send download request " + info.token);
		downloadFile(info.token);
	}

	private void downloadFile(final Long token) {
		final HttpDownloadInfo downinfo;
		try {
			downinfo = dbHelper.getHttpDownDao().queryForId(token);
		} catch (SQLException e) {
			curLoads.remove(token);
			tryFetchLoad();
			MLog.d(TAG, "fetch down info exceptino for " + token);
			return;
		}
		if (downinfo == null || downinfo.status != LoadStatus.LOADING) {
			curLoads.remove(token);
			tryFetchLoad();
			MLog.d(TAG, "remove downloaded or invalid down info with token "
					+ token);
			return;
		}

		MLog.d(TAG, "send download request " + token);
		LoadModel model = new LoadModel();
		model.download(downinfo)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<DownloadFileRes>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						MLog.e(TAG, "upload failture", e);
						failLoad(token, LoadStatus.ERROR);
					}

					@Override
					public void onNext(DownloadFileRes content) {
						if (content != null) {
							if (content.isfinished == 1) {
								finishLoad(token, null);
								MLog.e(TAG, downinfo.downUrl + "finish ");
							} else {
								HttpDownloadInfo info = downinfo;
								info.endIndex = content.endIndex;
								info.totalSize = content.totalSize;
								dbHelper.getHttpDownDao().updateLoadInfo(info);
								postEvent(token);
								downloadFile(token);
								MLog.e(TAG, info.downUrl
										+ " will download range: "
										+ info.endIndex);
							}
						} else {
							failLoad(token, LoadStatus.ERROR);
						}
					}
				});

	}

	@Override
	public HttpDownloadInfo getLoadInfo(long token) {
		try {
			return dbHelper.getHttpDownDao().queryForId(token);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long insert(HttpDownloadInfo info) {
		return dbHelper.getHttpDownDao().insertLoadInfo(info);
	}

	@Override
	public int deleteByOwnerId(long ownerId) {
		return dbHelper.getHttpDownDao().deleteLoad(ownerId);
	}

	@Override
	public int updateStatusByToken(long token, int status) {
		return dbHelper.getHttpDownDao().updateLoadStatus(token, status);
	}

	@Override
	public int updateStatusByOwnerId(long ownerId, int status) {
		return dbHelper.getHttpDownDao().updateLoadStatusByOwner(ownerId,
				status);
	}

	/**
	 * 0: download info was not found, -1: not finished, 1: finished
	 * 
	 * @param token
	 * @return
	 */
	public static int getDownFinishStatus(long token) {
		HttpDownloadInfo info = LoadDbHelper.getHelper().getHttpDownDao()
				.getLoadInfo(token);
		if (info == null)
			return 0;
		else if (info.status == LoadStatus.FINISH) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int deleteByToken(long token) {
		return dbHelper.getHttpDownDao().deleteByToken(token);
	}

}
