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
package com.beem.project.beem;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.entitycaps.SimpleDirectoryPersistentCache;
import org.jivesoftware.smackx.entitycaps.packet.CapsExtension;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.DeliveryReceipt;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.CapsExtensionProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DeliveryReceiptProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.beem.service.XmppConnectionAdapter;
import com.beem.project.beem.service.XmppFacade;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.smack.ping.PingExtension;
import com.beem.project.beem.smack.sasl.SASLGoogleOAuth2Mechanism;
import com.beem.project.beem.smack.sasl.ScramSaslMechanism;
import com.beem.project.beem.utils.BeemBroadcastReceiver;
import com.beem.project.beem.utils.BeemConnectivity;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.smack.MessageMetadataProvider;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.net.ConnectionManager;
import com.zhisland.lib.util.net.NetworkAvailableListener;

import de.duenndns.ssl.MemorizingTrustManager;

/**
 * This class is for the Beem service. It must contains every global
 * informations needed to maintain the background service. The connection to the
 * xmpp server will be made asynchronously when the service will start.
 *
 * @author darisk
 */
public class BeemService extends Service {

	/**
	 * The id to use for status notification.
	 */
	public static final int NOTIFICATION_STATUS_ID = 100;

	public static final String TAG = "BeemService";
	private static final int DEFAULT_XMPP_PORT = 5222;

	private XmppConnectionAdapter mConnection;
	private String mLogin;
	private String mPassword;
	private String mHost;
	private int mPort;
	private ConnectionConfiguration mConnectionConfiguration;
	private IXmppFacade.Stub mBind;

	private BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver();

	private SSLContext sslContext;

	private Handler handlerConxtionCheck = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (ConnectionManager.isNetworkConnected(BeemApplication
					.getInstance().getContext())) {
				MLog.d(BeemService.TAG, "check connect ");
				createConnectAsync();
			}

			handlerConxtionCheck.postDelayed(runnable, 20000);

		}
	};

	/**
	 * Constructor.
	 */
	public BeemService() {
	}

	/**
	 * Initialize the connection.
	 */
	private void initConnectionConfig() {
		// TODO add an option for this ?
		// SmackConfiguration.setPacketReplyTimeout(30000);
		ProxyInfo proxyInfo = getProxyConfiguration();
		if (mConnectionConfiguration == null) {
			SASLAuthentication
					.unsupportSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME);
			mConnectionConfiguration = new ConnectionConfiguration(mHost,
					mPort, null, proxyInfo);
		}

		mConnectionConfiguration.setSecurityMode(SecurityMode.disabled);
		mConnectionConfiguration.setDebuggerEnabled(true);
		// /
		if (BeemApplication.getInstance().getXmppConfig().isDebuggable())
			mConnectionConfiguration.setDebuggerEnabled(true);
		mConnectionConfiguration.setSendPresence(true);
		mConnectionConfiguration.setRosterLoadedAtLogin(true);
		// maybe not the universal path, but it works on most devices (Samsung
		// Galaxy, Google Nexus One)
		mConnectionConfiguration.setTruststoreType("BKS");
		mConnectionConfiguration
				.setTruststorePath("/system/etc/security/cacerts.bks");
		if (sslContext != null)
			mConnectionConfiguration.setCustomSSLContext(sslContext);
	}

	/**
	 * Get the save proxy configuration.
	 *
	 * @return the proxy configuration
	 */
	private ProxyInfo getProxyConfiguration() {
		return ProxyInfo.forNoProxy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "ONBIND()");
		return mBind;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "ONUNBIND()");
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		MLog.d(TAG, "Create BeemService");
		registerReceiver(mReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		if (BeemApplication.getInstance().getXmppConfig() == null) {
			throw new RuntimeException("没有设置xmpp服务配置");
		}

		if (BeemApplication.getInstance().getXmppAccount() == null
				|| BeemApplication.getInstance().getXmppConfig() == null) {
			this.stopSelf();
			return;
		}

		mLogin = BeemApplication.getInstance().getXmppAccount().getUserId()
				+ "";
		mPassword = BeemApplication.getInstance().getXmppAccount().getToken();
		mPort = DEFAULT_XMPP_PORT;
		initMemorizingTrustManager();

		mHost = BeemApplication.getInstance().getXmppConfig().getServer()
				.trim();
		mPort = Integer.parseInt(BeemApplication.getInstance().getXmppConfig()
				.getPort());

		configure(ProviderManager.getInstance());

		Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
		mBind = new XmppFacade(this);

		handlerConxtionCheck.postDelayed(runnable, 30000);
		ConnectionManager.getInstance().setNetworkAvailableListener(
				new NetworkAvailableListener() {

					@Override
					public void onNetworkAvailableChanged(boolean available) {
						if(available){
							createConnectAsync();
						}else{
							if (mConnection != null) {
								mConnection.disconnect();
							}
						}
					}
				});

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MLog.d(TAG, "onStart " + startId + " " + flags);
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		if (mConnection != null && mConnection.isAuthentificated()
				&& BeemConnectivity.isConnected(this)) {
			mConnection.disconnect();
		}
		handlerConxtionCheck.removeCallbacks(runnable);
		Log.i(TAG, "Stopping the service");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d(TAG, "onStart");
		createConnectAsync();
	}

	/**
	 * Create the XmppConnectionAdapter. This method makes a network request so
	 * it must not be called on the main thread.
	 *
	 * @return the connection
	 */
	public XmppConnectionAdapter createConnection() {
		if (mConnection == null) {
			initConnectionConfig();
			mConnection = new XmppConnectionAdapter(mConnectionConfiguration,
					mLogin, mPassword, this);
		}
		return mConnection;
	}

	/**
	 * Initialize Jingle from an XmppConnectionAdapter.
	 *
	 * @param adaptee
	 *            XmppConnection used for jingle.
	 */
	public void initJingle(XMPPConnection adaptee) {
	}

	/**
	 * Return a bind to an XmppFacade instance.
	 *
	 * @return IXmppFacade a bind to an XmppFacade instance
	 */
	public IXmppFacade getBind() {
		return mBind;
	}

	/**
	 * Utility method to create and make a connection asynchronously.
	 */
	private synchronized void createConnectAsync() {
		if (mConnection == null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					createConnection();
					connectAsync();
				}
			}).start();
		} else
			connectAsync();
	}

	/**
	 * Utility method to connect asynchronously.
	 */
	private void connectAsync() {
		MLog.d(BeemService.TAG, "start connect async in service");
		try {
			mConnection.connectAsync();
		} catch (RemoteException e) {
			Log.w(TAG, "unable to connect", e);
		}
	}

	/**
	 * Install the MemorizingTrustManager in the ConnectionConfiguration of
	 * Smack.
	 */
	private void initMemorizingTrustManager() {
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, MemorizingTrustManager.getInstanceList(this),
					new java.security.SecureRandom());
		} catch (GeneralSecurityException e) {
			Log.w(TAG, "Unable to use MemorizingTrustManager", e);
		}
	}

	/**
	 * A sort of patch from this thread:
	 * http://www.igniterealtime.org/community/thread/31118. Avoid
	 * ClassCastException by bypassing the classloading shit of Smack.
	 *
	 * @param pm
	 *            The ProviderManager.
	 */
	private void configure(ProviderManager pm) {
		Log.d(TAG, "configure");
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Privacy
		// pm.addIQProvider("query", "jabber:iq:privacy", new
		// PrivacyProvider());
		// Delayed Delivery only the new version
		pm.addExtensionProvider("delay", "urn:xmpp:delay",
				new DelayInfoProvider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Chat State
		ChatStateExtension.Provider chatState = new ChatStateExtension.Provider();
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates", chatState);
		// capabilities
		pm.addExtensionProvider(CapsExtension.NODE_NAME, CapsExtension.XMLNS,
				new CapsExtensionProvider());

		// Pubsub
		pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub",
				new PubSubProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub",
				new ItemsProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub",
				new ItemsProvider());
		pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub",
				new ItemProvider());

		pm.addExtensionProvider("items",
				"http://jabber.org/protocol/pubsub#event", new ItemsProvider());
		pm.addExtensionProvider("item",
				"http://jabber.org/protocol/pubsub#event", new ItemProvider());
		pm.addExtensionProvider("event",
				"http://jabber.org/protocol/pubsub#event", new EventProvider());
		// TODO rajouter les manquants pour du full pubsub

		pm.addExtensionProvider("metadata", "zh:xmpp:message:metadata",
				new MessageMetadataProvider());

		// ping
		pm.addIQProvider(PingExtension.ELEMENT, PingExtension.NAMESPACE,
				PingExtension.class);

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Version");
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		// pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Open());
		// pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Close());
		// pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Data());
		//
		// pm.addIQProvider("command", COMMAND_NAMESPACE, new
		// AdHocCommandDataProvider());
		// pm.addExtensionProvider("malformed-action", COMMAND_NAMESPACE,
		// new AdHocCommandDataProvider.MalformedActionError());
		// pm.addExtensionProvider("bad-locale", COMMAND_NAMESPACE,
		// new AdHocCommandDataProvider.BadLocaleError());
		// pm.addExtensionProvider("bad-payload", COMMAND_NAMESPACE,
		// new AdHocCommandDataProvider.BadPayloadError());
		// pm.addExtensionProvider("bad-sessionid", COMMAND_NAMESPACE,
		// new AdHocCommandDataProvider.BadSessionIDError());
		// pm.addExtensionProvider("session-expired", COMMAND_NAMESPACE,
		// new AdHocCommandDataProvider.SessionExpiredError());

		// Message received receipt
		pm.addExtensionProvider("received", DeliveryReceipt.NAMESPACE,
				new DeliveryReceiptProvider());

		/* register additionnals sasl mechanisms */
		SASLAuthentication.registerSASLMechanism(
				SASLGoogleOAuth2Mechanism.MECHANISM_NAME,
				SASLGoogleOAuth2Mechanism.class);
		SASLAuthentication.registerSASLMechanism(
				ScramSaslMechanism.MECHANISM_NAME, ScramSaslMechanism.class);

		SASLAuthentication
				.supportSASLMechanism(ScramSaslMechanism.MECHANISM_NAME);
		// Configure entity caps manager. This must be done only once
		File f = new File(getCacheDir(), "entityCaps");
		f.mkdirs();
		try {
			EntityCapsManager
					.setPersistentCache(new SimpleDirectoryPersistentCache(f));
		} catch (IllegalStateException e) {
			Log.v(TAG, "EntityCapsManager already initialized", e);
		} catch (IOException e) {
			Log.w(TAG, "EntityCapsManager not able to reuse persistent cache");
		}
	}

}
