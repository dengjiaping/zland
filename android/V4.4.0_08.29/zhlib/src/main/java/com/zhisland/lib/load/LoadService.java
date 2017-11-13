package com.zhisland.lib.load;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhisland.lib.util.MLog;

public class LoadService extends Service {

	private static final String TAG = "myupload";

	private ArrayList<BaseLoadMgr<?>> loads = new ArrayList<BaseLoadMgr<?>>();

	@Override
	public IBinder onBind(Intent i) {
		throw new UnsupportedOperationException(
				"Cannot bind to Download Manager Service");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MLog.v(TAG, "Service onCreate");
		ArrayList<Class<?>> classes = ZHLoadManager.Instance().getMgrClasses();
		for (Class<?> cls : classes) {
			BaseLoadMgr<?> mgr = ZHLoadManager.Instance().getLoadMgr(cls);
			mgr.init(this);
			mgr.onStart();
			loads.add(mgr);
		}

	}

	@Override
	public int onStartCommand(Intent intent, int startId, int flags) {

		for (BaseLoadMgr<?> load : loads) {
			load.tryFetchLoad();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Cleans up when the service is destroyed
	 */
	@Override
	public void onDestroy() {
		MLog.v(TAG, "Service onDestroy");
		for (BaseLoadMgr<?> load : loads) {
			load.onStop();
		}
		super.onDestroy();
	}

}