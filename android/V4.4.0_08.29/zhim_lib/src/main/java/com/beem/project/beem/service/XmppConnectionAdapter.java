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
package com.beem.project.beem.service;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.service.aidl.IBeemConnectionListener;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IXmppConnection;
import com.beem.project.beem.smack.ping.PingExtension;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.R;
import com.zhisland.im.event.EventConnection;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.net.ConnectionManager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;

import de.greenrobot.event.EventBus;

/**
 * This class implements an adapter for XMPPConnection.
 *
 * @author darisk
 */
public class XmppConnectionAdapter extends IXmppConnection.Stub {

    /**
     * Beem connection closed Intent name.
     */

    private static final String TAG = "XMPPConnectionAdapter";
    private final XMPPConnection mAdaptee;
    private IChatManager mChatManager;
    private final String mLogin;
    private final String mPassword;
    private String mResource;
    private String mErrorMsg;
    private final BeemService mService;
    private final RemoteCallbackList<IBeemConnectionListener> mRemoteConnListeners = new RemoteCallbackList<IBeemConnectionListener>();
    private final PingListener mPingListener = new PingListener();

    private final ConnexionListenerAdapter mConListener = new ConnexionListenerAdapter();


    /**
     * Constructor.
     *
     * @param config   Configuration to use in order to connect
     * @param login    login to use on connect
     * @param password password to use on connect
     * @param service  the background service associated with the connection.
     */
    public XmppConnectionAdapter(final ConnectionConfiguration config,
                                 final String login, final String password, final BeemService service) {
        this(new XMPPConnection(config), login, password, service);
    }

    /**
     * Constructor.
     *
     * @param serviceName name of the service to connect to
     * @param login       login to use on connect
     * @param password    password to use on connect
     * @param service     the background service associated with the connection.
     */
    public XmppConnectionAdapter(final String serviceName, final String login,
                                 final String password, final BeemService service) {
        this(new XMPPConnection(serviceName), login, password, service);
    }

    /**
     * Constructor.
     *
     * @param con      The connection to adapt
     * @param login    The login to use
     * @param password The password to use
     * @param service  the background service associated with the connection.
     */
    public XmppConnectionAdapter(final XMPPConnection con, final String login,
                                 final String password, final BeemService service) {
        mAdaptee = con;
        PrivacyListManager.getInstanceFor(mAdaptee);
        mLogin = login;
        mPassword = password;
        mService = service;
        mResource = BeemApplication.getInstance().getXmppConfig().getResource();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnectionListener(IBeemConnectionListener listen)
            throws RemoteException {
        if (listen != null)
            mRemoteConnListeners.register(listen);
    }

    @Override
    public boolean connect() throws RemoteException {
        if (mAdaptee.isConnected())
            return true;
        else {
            try {
                MLog.d(BeemService.TAG, "start connect in adapter");
                mAdaptee.connect();
                MLog.d(BeemService.TAG, "success connect in adapter");
                mAdaptee.addConnectionListener(mConListener);
                return true;
            } catch (XMPPException e) {
                MLog.e(BeemService.TAG,
                        "fail connect in adapter " + e.getMessage(), e);
                try {
                    // TODO NIKITA DOES SOME SHIT !!! Fix this monstruosity
                    String str = mService.getResources().getString(
                            mService.getResources().getIdentifier(
                                    e.getXMPPError().getCondition()
                                            .replace("-", "_"),
                                    "string",
                                    BeemApplication.getInstance().getContext()
                                            .getPackageName()));
                    mErrorMsg = str;
                } catch (Exception e2) {
                    if (!"".equals(e.getMessage()))
                        mErrorMsg = e.getMessage();
                    else
                        mErrorMsg = e.toString();
                }
            } catch (Exception ex) {
                MLog.e(BeemService.TAG,
                        "fail connect in adapter " + ex.getMessage(), ex);
                mErrorMsg = ex.getLocalizedMessage();
            }
            return false;
        }
    }

    @Override
    public boolean login() throws RemoteException {
        MLog.d(BeemApplication.TAG, "login in connection adpter");
        if (mAdaptee.isAuthenticated())
            return true;
        if (!mAdaptee.isConnected())
            return false;
        try {

            this.initFeatures(); // pour declarer les features xmpp qu'on
            // supporte
            addFilterAndListener();

            PacketFilter filter = new PacketFilter() {

                @Override
                public boolean accept(Packet packet) {
                    if (packet instanceof Presence) {
                        Presence pres = (Presence) packet;
                        if (pres.getType() == Presence.Type.subscribe
                                || pres.getType() == Presence.Type.subscribed
                                || pres.getType() == Presence.Type.unsubscribed)
                            return true;
                    }
                    return false;
                }
            };
            filter = new PacketTypeFilter(PingExtension.class);
            mAdaptee.addPacketListener(mPingListener, filter);

            MLog.d("XmppConnectionAdapter", mLogin + " " + mPassword + " " + mResource);
            mAdaptee.login(mLogin, mPassword, mResource);

            mChatManager = new BeemChatManager(mAdaptee.getChatManager(),
                    mService, mAdaptee.getRoster());
            mService.initJingle(mAdaptee);
            MLog.d(TAG, "login success");
            BeemApplication.getInstance().setConnected(true);

            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_CONNECTED));
            return true;
        } catch (XMPPException e) {
            MLog.e(BeemApplication.TAG, "login failed " + e.getMessage(), e);
            Log.e(TAG, "Error while connecting", e);
            mErrorMsg = mService.getString(R.string.error_login_authentication);
            return false;
        } catch (Exception e) {
            MLog.e(BeemApplication.TAG, "login failed " + e.getMessage(), e);
            Log.e(TAG, "Error while connecting", e);
            mErrorMsg = mService.getString(R.string.error_login_authentication);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void connectAsync() throws RemoteException {
        if (mAdaptee.isConnected() && mAdaptee.isAuthenticated())
            return;
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    connectSync();
                } catch (RemoteException e) {
                    Log.e(TAG, "Error while connecting asynchronously", e);
                }
            }
        });
        t.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connectSync() throws RemoteException {
        MLog.d(BeemService.TAG, "sync connect in adapter");
        if (connect()) {
            MLog.d(BeemService.TAG, "sync connect ok and login");
            return login();
        } else {
            MLog.d(BeemService.TAG, "sync connect failed");
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean disconnect() {
        if (mAdaptee != null && mAdaptee.isConnected())
            mAdaptee.disconnect();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IChatManager getChatManager() throws RemoteException {
        return mChatManager;
    }

    /**
     * Returns true if currently authenticated by successfully calling the login
     * method.
     *
     * @return true when successfully authenticated
     */
    public boolean isAuthentificated() {
        return mAdaptee.isAuthenticated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnectionListener(IBeemConnectionListener listen)
            throws RemoteException {
        if (listen != null)
            mRemoteConnListeners.unregister(listen);
    }

    /**
     * Initialize the features provided by beem.
     */
    private void initFeatures() {
        ServiceDiscoveryManager.setIdentityName("APeople");
        ServiceDiscoveryManager.setIdentityType("phone");
        ServiceDiscoveryManager sdm = ServiceDiscoveryManager
                .getInstanceFor(mAdaptee);
        if (sdm == null)
            sdm = new ServiceDiscoveryManager(mAdaptee);

        sdm.addFeature("http://jabber.org/protocol/disco#info");
        // nikita: must be uncommented when the feature will be enabled
        // sdm.addFeature("jabber:iq:privacy");
        sdm.addFeature("http://jabber.org/protocol/caps");
        sdm.addFeature("urn:xmpp:avatar:metadata");
        sdm.addFeature("urn:xmpp:avatar:metadata+notify");
        sdm.addFeature("urn:xmpp:avatar:data");
        sdm.addFeature("http://jabber.org/protocol/nick");
        sdm.addFeature("http://jabber.org/protocol/nick+notify");
        sdm.addFeature(PingExtension.NAMESPACE);

        EntityCapsManager em = sdm.getEntityCapsManager();
        em.setNode("http://www.zhisland.com");
    }

    /**
     * Reset the application state.
     */
    private void resetApplication() {
        BeemApplication.getInstance().setConnected(false);
    }

    /**
     * Listener for XMPP connection events. It will calls the remote listeners
     * for connection events.
     */
    private class ConnexionListenerAdapter implements ConnectionListener {

        /**
         * Defaut constructor.
         */
        public ConnexionListenerAdapter() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void connectionClosed() {
            Log.d(TAG, "closing connection");
            resetApplication();
            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_COLSED));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void connectionClosedOnError(Exception exception) {
            Log.d(TAG, "connectionClosedOnError");

            if (exception instanceof XMPPException) {
                XMPPException ex = (XMPPException) exception;
                if (ex.getStreamError() != null
                        && ex.getStreamError().getCode() != null
                        && ex.getStreamError().getCode().equals("conflict")) {
                    EventConnection event = new EventConnection(
                            EventConnection.ACTION_CONFLICT);
                    EventBus.getDefault().post(event);
                    return;
                }
            }

            resetApplication();
            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_COLSED));

            if (ConnectionManager.isNetworkConnected(BeemApplication
                    .getInstance().getContext())) {
                try {
                    connectAsync();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Connection failed callback.
         *
         * @param errorMsg smack failure message
         */
        public void connectionFailed(String errorMsg) {
            Log.d(TAG, "Connection Failed");
            final int n = mRemoteConnListeners.beginBroadcast();

            for (int i = 0; i < n; i++) {
                IBeemConnectionListener listener = mRemoteConnListeners
                        .getBroadcastItem(i);
                try {
                    if (listener != null)
                        listener.connectionFailed(errorMsg);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing the
                    // dead listeners.
                    Log.w(TAG,
                            "Error while triggering remote connection listeners",
                            e);
                }
            }
            mRemoteConnListeners.finishBroadcast();
            resetApplication();
            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_CONNECTED));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reconnectingIn(int arg0) {
            Log.d(TAG, "reconnectingIn");
            final int n = mRemoteConnListeners.beginBroadcast();

            for (int i = 0; i < n; i++) {
                IBeemConnectionListener listener = mRemoteConnListeners
                        .getBroadcastItem(i);
                try {
                    if (listener != null)
                        listener.reconnectingIn(arg0);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing the
                    // dead listeners.
                    Log.w(TAG,
                            "Error while triggering remote connection listeners",
                            e);
                }
            }
            mRemoteConnListeners.finishBroadcast();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reconnectionFailed(Exception arg0) {
            Log.d(TAG, "reconnectionFailed");
            final int r = mRemoteConnListeners.beginBroadcast();

            for (int i = 0; i < r; i++) {
                IBeemConnectionListener listener = mRemoteConnListeners
                        .getBroadcastItem(i);
                try {
                    if (listener != null)
                        listener.reconnectionFailed();
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing the
                    // dead listeners.
                    Log.w(TAG,
                            "Error while triggering remote connection listeners",
                            e);
                }
            }
            mRemoteConnListeners.finishBroadcast();
            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_COLSED));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reconnectionSuccessful() {
            Log.d(TAG, "reconnectionSuccessful");
            BeemApplication.getInstance().setConnected(true);

            addFilterAndListener();

            final int n = mRemoteConnListeners.beginBroadcast();

            for (int i = 0; i < n; i++) {
                IBeemConnectionListener listener = mRemoteConnListeners
                        .getBroadcastItem(i);
                try {
                    if (listener != null)
                        listener.reconnectionSuccessful();
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing the
                    // dead listeners.
                    Log.w(TAG,
                            "Error while triggering remote connection listeners",
                            e);
                }
            }
            mRemoteConnListeners.finishBroadcast();
            EventBus.getDefault().post(
                    new EventConnection(EventConnection.ACTION_CONNECTED));
        }
    }

    private void addFilterAndListener() {
        // could add message filter and handler here
    }

    /**
     * Listener for Ping request. It will respond with a Pong.
     */
    private class PingListener implements PacketListener {

        /**
         * Constructor.
         */
        public PingListener() {

        }

        @Override
        public void processPacket(Packet packet) {
            if (!(packet instanceof PingExtension))
                return;
            PingExtension p = (PingExtension) packet;
            if (p.getType() == IQ.Type.GET) {
                PingExtension pong = new PingExtension();
                pong.setType(IQ.Type.RESULT);
                pong.setTo(p.getFrom());
                pong.setPacketID(p.getPacketID());
                sendPacket(pong);
            }
        }
    }

    // ===========wrapper of xmpp connection========

    /**
     * call xmpp connection to send packet
     *
     * @param packet
     */
    public boolean sendPacket(Packet packet) {
        try {
            this.mAdaptee.sendPacket(packet);
            return true;
        } catch (Exception ex) {
            MLog.e(BeemService.TAG, ex.getMessage(), ex);
            return false;
        }
    }

    public boolean isConnected() {
        return mAdaptee.isConnected();
    }

}
