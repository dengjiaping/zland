package com.zhisland.lib.view.web.cookie;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;

import android.util.Pair;

import com.zhisland.lib.async.http.AsyncHttpClient;
import com.zhisland.lib.async.http.PersistentCookieStore;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;

/**
 * cookie的辅助类
 * 
 * @author arthur
 *
 */
public class CookieUtil {

	/**
	 * 设置httpclient的cookie
	 */
	public static void SetCookieString(String domain,
			ArrayList<Pair<String, String>> cookieString) {
		AsyncHttpClient client = AsyncHttpClient.Factory.getSingletonInstance();
		CookieStore cookieStore = client.getCookieStore();
		if (cookieStore == null) {
			cookieStore = new PersistentCookieStore(ZHApplication.APP_CONTEXT);
			client.setCookieStore(cookieStore);
		}
		cookieStore.clear();
		for (Pair<String, String> pair : cookieString) {
			cookieStore.addCookie(new DomainCookie(pair.first, pair.second,
					domain));
			MLog.e("aa", String.format("%s=%s", pair.first, pair.second));
		}
	}
}
