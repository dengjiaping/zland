package com.zhisland.android.blog.common.util;

import java.util.UUID;

import com.zhisland.lib.load.HttpUploadInfo;
import com.zhisland.lib.load.LoadStatus;
import com.zhisland.lib.load.ZHLoadManager;

import de.greenrobot.event.EventBus;

public class AvatarUploader {

	private static final AvatarUploader instance = new AvatarUploader();
	private OnUploaderCallback callback;

	public static interface OnUploaderCallback {
		public void callBack(String backUrl);
	}

	private AvatarUploader() {

	}

	public static AvatarUploader instance() {
		boolean isRegistered = EventBus.getDefault().isRegistered(instance);
		if (!isRegistered) {
			EventBus.getDefault().register(instance);
		}
		return instance;
	}

	long avatarUploadToken;

	public void onEventMainThread(HttpUploadInfo info) {

		if (info.token == avatarUploadToken) {
			switch (info.status) {
			case LoadStatus.ERROR: {
				loadFinish();
				if (callback != null) {
					callback.callBack("");
				}
				break;
			}
			case LoadStatus.FINISH: {
				loadFinish();
				if (callback != null) {
					callback.callBack(info.picUrl);
				}
				break;
			}
			}
		}
	}

	public void loadFinish() {
		boolean isRegistered = EventBus.getDefault().isRegistered(instance);
		if (isRegistered) {
			EventBus.getDefault().unregister(instance);
		}
	}

	public void uploadAvatar(String localUrl, OnUploaderCallback callback) {
		this.callback = callback;
		avatarUploadToken = ZHLoadManager.uploadFile(localUrl, 0, UUID
				.randomUUID().toString(), HttpUploadInfo.TYPE_PIC, 0, 0);
	}

}
