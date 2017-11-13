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

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DeliveryReceipt;
import org.jivesoftware.smackx.packet.DeliveryReceiptRequest;

import android.os.Handler;
import android.os.RemoteException;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.service.aidl.IChatManager;
import com.beem.project.beem.service.aidl.IChatRoom;
import com.beem.project.beem.service.aidl.IChatRoomManager;
import com.beem.project.beem.service.aidl.IXmppConnection;
import com.beem.project.beem.service.aidl.IXmppFacade;
import com.beem.project.beem.service.pcklistener.FriendRelationJson;
import com.zhisland.im.BeemApplication;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.data.IMContact;
import com.zhisland.im.event.EventRelation;
import com.zhisland.im.smack.ZMessage;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.util.MLog;

import de.greenrobot.event.EventBus;

/**
 * This class is a facade for the Beem Service.
 *
 * @author darisk
 */
public class XmppFacade extends IXmppFacade.Stub {

	private static final String TAG = "XmppFacade";
	private XmppConnectionAdapter mConnexion;
	private final BeemService service;

	/**
	 * Create an XmppFacade.
	 *
	 * @param service
	 *            the service providing the facade
	 */
	public XmppFacade(final BeemService service) {
		this.service = service;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectAsync() throws RemoteException {
		initConnection();
		mConnexion.connectAsync();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectSync() throws RemoteException {
		initConnection();
		mConnexion.connectSync();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IXmppConnection createConnection() throws RemoteException {
		initConnection();
		return mConnexion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() throws RemoteException {
		initConnection();
		mConnexion.disconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChatManager getChatManager() throws RemoteException {
		initConnection();
		return mConnexion.getChatManager();
	}

	@Override
	public void sendPresencePacket(PresenceAdapter presence)
			throws RemoteException {

		initConnection();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				org.jivesoftware.smack.packet.Message response = new org.jivesoftware.smack.packet.Message(
						"");
				String jid = IMContact.buildJid(BeemApplication.getInstance()
						.getXmppAccount().getUserId());
				response.setFrom(jid);
				response.setType(Message.Type.chat);
				DeliveryReceipt receipt = new DeliveryReceipt("");
				response.addExtension(receipt);
				mConnexion.sendPacket(response);
			}
		}, 30000); // 30 * 1000 = 30seconds;
	}

	@Override
	public UserInfo getUserInfo() throws RemoteException {
		initConnection();
		return mConnexion.getUserInfo();
	}

	/**
	 * Initialize the connection.
	 */
	private void initConnection() {
		if (mConnexion == null) {
			mConnexion = service.createConnection();
		}
	}

	@Override
	public IChatRoomManager getChatRoomManager() throws RemoteException {
		initConnection();
		return mConnexion.getChatRoomManager();
	}

	@Override
	public IChatRoom getChatRoom(String roomName) throws RemoteException {
		initConnection();
		return mConnexion.getChatRoomManager().getChatRoom(roomName);
	}

	@Override
	public boolean addFriend(String msgContent) throws RemoteException {
		boolean result = false;
		initConnection();

		try {
			FriendRelationJson fit = GsonHelper.GetCommonGson().fromJson(
					msgContent, FriendRelationJson.class);
			ZMessage msg = new ZMessage();
			msg.setTo(fit.toJid);
			msg.setFrom(fit.fromJid);
			msg.setType(ZMessage.Type.headline);
			msg.setBody(msgContent);
			DeliveryReceiptRequest request = new DeliveryReceiptRequest();
			msg.addExtension(request);
			result = mConnexion.sendPacket(msg);
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	@Override
	public boolean acceptFriend(String jid) throws RemoteException {
		boolean result = false;
		initConnection();

		try {
			IMContact ic = DBMgr.getHelper().getContactDao().getByJid(jid);
			if (ic == null)
				return result;

			FriendRelationJson fit = new FriendRelationJson();
			fit.ask = FriendRelationJson.ASK_ACCEPTED;

			fit.avatar = BeemApplication.getInstance().getXmppAccount()
					.getAvatar();
			fit.name = BeemApplication.getInstance().getXmppAccount()
					.getUserName();
			fit.atk = BeemApplication.getInstance().getXmppAccount().getToken();
			String body = GsonHelper.GetCommonGson().toJson(fit);

			ZMessage msg = new ZMessage();
			msg.setTo(ic.jid);
			String fromJid = IMContact.buildJid(BeemApplication.getInstance()
					.getXmppAccount().getUserId());
			msg.setFrom(fromJid);
			msg.setType(ZMessage.Type.headline);
			msg.setBody(body);
			DeliveryReceiptRequest request = new DeliveryReceiptRequest();
			msg.addExtension(request);
			result = mConnexion.sendPacket(msg);

			if(result){
				DBMgr.getHelper().getContactDao().acceptInvite(jid);
				EventRelation event = new EventRelation(jid, ic.name,
						EventRelation.REQUEST_ACCEPTED, null);
				EventBus.getDefault().post(event);
			}
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	/**
	 * 忽略好友请求
	 * */
	@Override
	public boolean ignoreInvite(String jid) throws RemoteException {
		boolean result = false;

		try {
			IMContact ic = DBMgr.getHelper().getContactDao().getByJid(jid);
			if (ic == null)
				return result;

			DBMgr.getHelper().getContactDao().ignoreInvite(jid);
			result = true;

			EventRelation event = new EventRelation(jid, ic.name,
					EventRelation.REQUEST_IGNORE, null);
			EventBus.getDefault().post(event);
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	/**
	 * 提升好友级别
	 * */
	@Override
	public boolean promoteFriend(String jid) throws RemoteException {
		boolean result = false;
		initConnection();

		try {
			IMContact ic = DBMgr.getHelper().getContactDao().getByJid(jid);
			if (ic == null)
				return result;

			result = sendHeadLineMsg(ic, FriendRelationJson.ASK_UPGRADE);

			if(result){
				DBMgr.getHelper().getContactDao().promoteFriend(jid);
			}
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	/**
	 * 降低好友级别
	 * */
	@Override
	public boolean downgradeFriend(String jid) throws RemoteException {
		boolean result = false;
		initConnection();

		try {
			IMContact ic = DBMgr.getHelper().getContactDao().getByJid(jid);
			if (ic == null)
				return result;

			result = sendHeadLineMsg(ic, FriendRelationJson.ASK_DOWNGRADE);

			if(result){
				DBMgr.getHelper().getContactDao().downgradeFriend(jid);
			}
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	@Override
	public boolean blockFriend(String jid) throws RemoteException {
		boolean result = false;
		initConnection();

		try {
			IMContact ic = DBMgr.getHelper().getContactDao().getByJid(jid);
			if (ic == null)
				return result;

			result = sendHeadLineMsg(ic, FriendRelationJson.ASK_DELETE);

			if(result){
				DBMgr.getHelper().getContactDao().deleteFriend(jid);
			}
		} catch (Exception ex) {
			MLog.e(TAG, ex.getLocalizedMessage(), ex);
		}

		return result;
	}

	private boolean sendHeadLineMsg(IMContact ic, int ask) {
		FriendRelationJson fit = new FriendRelationJson();
		fit.ask = ask;

		fit.atk = BeemApplication.getInstance().getXmppAccount().getToken();
		String body = GsonHelper.GetCommonGson().toJson(fit);

		ZMessage msg = new ZMessage();
		msg.setTo(ic.jid);
		String fromJid = IMContact.buildJid(BeemApplication.getInstance()
				.getXmppAccount().getUserId());
		msg.setFrom(fromJid);
		msg.setType(ZMessage.Type.headline);
		msg.setBody(body);
		DeliveryReceiptRequest request = new DeliveryReceiptRequest();
		msg.addExtension(request);
		return mConnexion.sendPacket(msg);
	}
}
