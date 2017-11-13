package com.zhisland.im.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zhisland.im.BeemApplication;

import de.greenrobot.event.EventBus;

public class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.d("mark", "网络状态已经改变");
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				String name = info.getTypeName();
				// TODO start service
				context.startService(BeemApplication.getInstance()
						.getServiceIntent());
				Log.d("mark", "当前网络名称：" + name);
				EventBus.getDefault().post(
						new EventNet(EventNet.TYPE_NET_AVAILABLE));
			} else {
				Log.d("mark", "没有可用网络");
				EventBus.getDefault().post(
						new EventNet(EventNet.TYPE_NET_NOT_AVAILABLE));
			}
		}
	}
};