package com.zhisland.android.blog.common.push;

import java.lang.reflect.Type;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushConsts;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.MLog;

/**
 * push 接收 receiver
 * 
 */
public class MessageReceiver extends BroadcastReceiver {

	public static final String TAG = "tpush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		int action = bundle.getInt(PushConsts.CMD_ACTION);

		if (action == PushConsts.GET_MSG_DATA) {
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				try{
					String json = new String(payload);
					Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					Map<String, String> maps = GsonHelper.GetPureGson().fromJson(
							json, type);
					// 进行分发push
					PushMgr.getInstance().dispatchPush(maps);
				}catch (Exception e){
					MLog.e(TAG, e.getMessage(), e);
				}
			}
		}
	}
}
