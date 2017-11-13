/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */

package com.zhisland.im;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.zhisland.lib.util.MLog;

/**
 * 此类是一个单例模式，负责与使用类库的上层应用进行对接，通常在应用的Application中管理、配置此类
 * 
 */
public class BeemApplication {

	public static final String NOTIFICATION_VIBRATE_KEY = "notification_vibrate";
	public static final String NOTIFICATION_SOUND_KEY = "notification_sound";

	public static final String TAG = "beemapp";

	private Context appContext;
	private boolean mIsConnected;
	private ZHXMPPConfig xmppConfig;
	private ZHXMPPAccount xmppAccount;

	/**
	 * Constructor.
	 */
	private BeemApplication() {
	}

	/**
	 * 设置应用的上下文
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.appContext = context;
		this.getServiceIntent();
	}

	/**
	 * 获取app上下文
	 * 
	 * @return
	 */
	public Context getContext() {
		return appContext;
	}

	/**
	 * Tell if Beem is connected to a XMPP server.
	 * 
	 * @return false if not connected.
	 */
	public boolean isConnected() {
		return mIsConnected;
	}

	/**
	 * Set the status of the connection to a XMPP server of BEEM.
	 * 
	 * @param isConnected
	 *            set for the state of the connection.
	 */
	public void setConnected(boolean isConnected) {
		mIsConnected = isConnected;
	}

	/**
	 * 设置xmpp的配置
	 * 
	 */
	public void setXmppConfig(String domain, String resource, String server,
			String port) {
		this.xmppConfig = new ZHXMPPConfig(domain, resource, true);
		this.xmppConfig.setServer(server);
		this.xmppConfig.setPort(port);
	}

	/**
	 * 获取XMPP的配置
	 */
	public ZHXMPPConfig getXmppConfig() {
		return this.xmppConfig;
	}

	/**
	 * 获取上层应用设置的IM账号信息
	 * 
	 * @return
	 */
	public ZHXMPPAccount getXmppAccount() {
		return xmppAccount;
	}

	/**
	 * 设置IM的账号信息，在<b>调用{@link #bindXMPPService()}方法之前</b>设置此信息
	 * 
	 */
	public void setXmppAccount(long userId, String userName, String token,
			String avatar) {
		this.xmppAccount = new ZHXMPPAccount(userId, userName, token);
		this.xmppAccount.setAvatar(avatar);
	}

	// ==========XMPP account info start ======

	/**
	 * IM用户账号、密码、以及头像
	 * 
	 * @author arthur
	 *
	 */
	public static class ZHXMPPAccount {
		private long userId;
		private String userName;
		private String token;
		private String avatar;

		private ZHXMPPAccount(long userId, String userName, String token) {
			this.userId = userId;
			this.userName = userName;
			this.token = token;
		}

		/**
		 * 当前登录用户的ID
		 * 
		 * @return
		 */
		public long getUserId() {
			return userId;
		}

		/**
		 * 当前登录用户的Token
		 * 
		 * @return
		 */
		public String getToken() {
			return token;
		}

		/**
		 * 当前登录用户的头像
		 * 
		 * @return
		 */
		public String getAvatar() {
			return avatar;
		}

		/**
		 * 设置当前用户的头像
		 * 
		 * @param avatar
		 */
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		/**
		 * 获取用户名称
		 * 
		 * @return
		 */
		public String getUserName() {
			return userName;
		}

	}

	// ==========xmpp account info end========

	// ==========XMPP configuration start===========//
	/**
	 * XMPP域名、服务地址及服务端口号
	 * 
	 * @author arthur
	 *
	 */
	public static class ZHXMPPConfig {
		private String domain;
		private String resource;
		private String server;
		private String port;
		private boolean debuggable = true;

		private ZHXMPPConfig(String domain, String resource, boolean debuggable) {
			this.domain = domain;
			this.resource = resource;
			this.debuggable = debuggable;
		}

		public String getDomain() {
			return domain;
		}

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getResource() {
			return resource;
		}

		public boolean isDebuggable() {
			return debuggable;
		}

	}

	// ==========XMPP configuration end===========//

	// ===============xmpp service============//

	// public static final Intent SERVICE_INTENT = new Intent();
	// static {
	// SERVICE_INTENT.setComponent(new ComponentName("com.zhisland.people",
	// "com.beem.project.beem.BeemService"));
	// }
	private Intent serviceIntent;
	private final ServiceConnection mServConn = new BeemServiceConnection();

	private IXmppFacade mXmppFacade;
	private boolean mBinded;

	public IChatManager getChatMgr() {
		try {
			return mXmppFacade.getChatManager();
		} catch (Exception e) {
			return null;
		}
	}

	public Intent getServiceIntent() {
		if (serviceIntent == null) {
			serviceIntent = new Intent(getContext(), BeemService.class);
		}
		return serviceIntent;
	}

	private class BeemServiceConnection implements ServiceConnection {

		/**
		 * Constructor.
		 */
		public BeemServiceConnection() {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MLog.d(BeemService.TAG, "on service connected");
			mXmppFacade = IXmppFacade.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			MLog.d(TAG, "unbinded");
			mXmppFacade = null;
			mBinded = false;
		}
	}

	/**
	 * 当登录成功后调用这个方法启动XMPP服务
	 * 
	 * @param context
	 *            用来启动xmpp service
	 */
	public void startXmpp(Context context) {
		if (this.getXmppConfig() == null) {
			throw new RuntimeException("xmpp的服务器配置项没有设置");
		}
		if (this.getXmppAccount() == null) {
			throw new RuntimeException("xmpp 服务的登录信息没有配置");
		}

		context.startService(serviceIntent);
		bindXMPPService();
	}

	/**
	 * 当程序结束后嗲用此方法停止XMPP服务
	 * 
	 * @param context
	 *            用来停止XMPP service
	 */
	public void stopXmpp(Context context) {
		unbindXmppService();
		context.stopService(serviceIntent);
	}

	/**
	 * 绑定xmpp service
	 */
	private void bindXMPPService() {
		MLog.d(BeemService.TAG, "bing service from application");
		if (!mBinded) {
			MLog.d(BeemService.TAG, "bing service from application");
			mBinded = appContext.bindService(serviceIntent, mServConn,
					Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * 取消绑定xmpp service
	 */
	private void unbindXmppService() {
		if (!mBinded)
			return;

		MLog.d(BeemService.TAG, "unbind service from application");
		appContext.unbindService(mServConn);
		mXmppFacade = null;
		mBinded = false;
	}

	/**
	 * 获取xmpp的组合代理类
	 * 
	 * @return
	 */
	public IXmppFacade getXmppFacade() {
		return mXmppFacade;
	}

	// ===============xmpp service============//

	// ============singleton methods========
	public static BeemApplication getInstance() {
		return Factory.instance;
	}

	/**
	 * BeemApplication的单一工厂类
	 */
	private static class Factory {

		private static BeemApplication instance = new BeemApplication();

	}

}
