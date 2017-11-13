package com.beem.project.beem.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.MUCUser;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.beem.service.aidl.IChatRoom;
import com.beem.project.beem.service.aidl.IMessageListener;
import com.zhisland.im.data.DBMgr;
import com.zhisland.im.smack.MessageMetadataExtension;
import com.zhisland.im.util.Constants;

public class ChatRoomAdapter extends IChatRoom.Stub {
	private static final String TAG = "ChatRoomAdapter";

	private final MultiUserChat mAdaptee;
	private final String mRoom;
	private boolean mIsOpen;
	private int mUnreadMsgCount;
	private boolean mIsOwner;
	private String mOwnerJID;
	private String mSuccessorJID;
	private String mUserJID;
	private HashSet<String> mOccupants = new HashSet<String>();

	public ChatRoomAdapter(final MultiUserChat muc) {
		mAdaptee = muc;
		mRoom = muc.getRoom();
		mUnreadMsgCount = DBMgr.getHelper().getChatDao().getUnreadCount(mRoom);
		mAdaptee.addParticipantStatusListener(mParticipantStatusListener);
		mAdaptee.addParticipantListener(new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				Presence presence = (Presence) packet;

				MUCUser mucUser = (MUCUser) presence.getExtension("x",
						"http://jabber.org/protocol/muc#user");
				MUCUser.Item item = mucUser.getItem();
				// if (mSuccessorJID == null) {
				// mSuccessorJID = item.getJid();
				// }
				mOccupants.add(item.getJid());
			}
		});
	}

	public void setOpen(boolean isOpen) {
		mIsOpen = isOpen;
		if (isOpen) {
			mUnreadMsgCount = 0;
			DBMgr.getHelper().getChatDao().clearUnreadCount(mRoom);
		}
	}

	public boolean isOpen() {
		return mIsOpen;
	}

	@Override
	public boolean isOwner() {
		return mIsOwner;
	}

	public void setOwner(boolean isOwner) {
		mIsOwner = isOwner;
	}

	public void setOwnerJID(String ownerJID) {
		mOwnerJID = ownerJID;
	}

	public void setUserJID(String userJID) {
		mUserJID = userJID;
	}

	public MultiUserChat getMuc() {
		return mAdaptee;
	}

	public String getRoom() {
		return mRoom;
	}

	private void sendMultimedia(com.beem.project.beem.service.Message message) {
		// TODO
	}

	@Override
	public void sendMessage(com.beem.project.beem.service.Message message)
			throws RemoteException {
		if (message.getSubType() == Constants.MSG_TYPE_TEXT) {
			sendSmackMessage(message);
		} else {
			sendMultimedia(message);
		}
	}

	private void sendSmackMessage(com.beem.project.beem.service.Message message) {
		int status = Constants.MSG_STATUS_NORMAL;
		try {
			Message msg = mAdaptee.createMessage();
			msg.setPacketID(message.getMessageThread());
			msg.setBody(message.getBody());
			MessageMetadataExtension metadata = new MessageMetadataExtension();
			MessageMetadataExtension.Info info;
			info = new MessageMetadataExtension.Info();
			info.setType(message.getSubType());
			info.setUrl(message.getUrl());
			info.setToken(message.getToken());
			info.setSize(message.getSize());
			info.setDuration(message.getDuration());
			info.setTitle(message.getTitle());
			info.setDescription(message.getDescription());
			info.setMessageThread(message.getMessageThread());
			metadata.setInfo(info);

			msg.addExtension(metadata);

			mAdaptee.sendMessage(msg);
		} catch (XMPPException e) {
			status = Constants.MSG_STATUS_ERROR;
			e.printStackTrace();
		}
		addMessage(message, Constants.MSG_OUTGOING);
		message.setStatus(status);
		notifyMessageStatus(message, status, 100);
	}

	private void notifyMessageStatus(
			com.beem.project.beem.service.Message message, int status,
			int progress) {
		final int n = mRemoteListeners.beginBroadcast();
		for (int i = 0; i < n; i++) {
			IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
			try {
				listener.onStatusChanged(message.getMessageThread(), status,
						progress);
			} catch (RemoteException e) {
				Log.w(TAG, e.getMessage());
			}
		}
		mRemoteListeners.finishBroadcast();

		message.setStatus(status);
		message.setProgress(progress);
		DBMgr.getHelper().getMsgDao().updateMessage(message);
	}

	private final RemoteCallbackList<IMessageListener> mRemoteListeners = new RemoteCallbackList<IMessageListener>();

	@Override
	public void addMessageListener(IMessageListener listen) {
		if (listen != null)
			mRemoteListeners.register(listen);
	}

	@Override
	public void removeMessageListener(IMessageListener listen) {
		if (listen != null) {
			mRemoteListeners.unregister(listen);
		}
	}

	@Override
	public List<com.beem.project.beem.service.Message> getMessages(long time,
			long limit) throws RemoteException {
		return DBMgr.getHelper().getMsgDao()
				.loadRoomMessages(mRoom, time, limit);
	}

	/**
	 * Add a message in the chat history.
	 *
	 * @param msg
	 *            the message to add
	 */
	private void addMessage(com.beem.project.beem.service.Message msg,
			int direction) {
		if (!isOpen())
			mUnreadMsgCount++;
		if (!"".equals(msg.getBody()) && msg.getBody() != null) {
			logMessage(msg, direction);
		}
	}

	private void logMessage(com.beem.project.beem.service.Message message,
			int direction) {
		DBMgr.getHelper().getMsgDao()
				.logRoomMessage(mRoom, message, direction, mUnreadMsgCount);
	}

	void deliver(com.beem.project.beem.service.Message message) {
		// message.setThread(threadID);
		//
		// for (MessageListener listener : listeners) {
		// listener.processMessage(this, message);
		// }

		addMessage(message, Constants.MSG_INCOMING);
		final int n = mRemoteListeners.beginBroadcast();
		for (int i = 0; i < n; i++) {
			IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
			try {
				if (listener != null)
					listener.processMessage(null, message);
			} catch (RemoteException e) {
				Log.w(TAG, "Error while diffusing message to listener", e);
			}
		}
		mRemoteListeners.finishBroadcast();
	}

	@Override
	public int getUnreadMessageCount() throws RemoteException {
		return mUnreadMsgCount;
	}

	@Override
	public void leaveRoom() throws RemoteException {
		if (mIsOwner) {
			for (String item : mOccupants) {
				mSuccessorJID = item;
				break;
			}
			if (mSuccessorJID != null) {
				try {
					mAdaptee.grantOwnership(mSuccessorJID);
					String nickname = StringUtils.parseName(mUserJID);
					// TODO what if the user is not a user?
					mAdaptee.kickParticipant(nickname, "Delete&Leave");
				} catch (XMPPException e) {
					Log.e(TAG, "Grant Ownership failed!", e);
				}
			}
		}
		Contact c = new Contact(mRoom);
		// DBMgr.getHelper().getContactDao().removeContact(c);
		mAdaptee.leave();
	}

	@Override
	public void kickOccupant(String nickname, String message)
			throws RemoteException {
		try {
			mAdaptee.kickParticipant(nickname, message);
		} catch (XMPPException e) {
			Log.e(TAG, "Kick Occupant failed!", e);
		}
	}

	@Override
	public void inviteOccupants(List<Contact> contacts, String message)
			throws RemoteException {
		for (Contact contact : contacts) {
			mAdaptee.invite(contact.getJID(), message);
		}
	}

	@Override
	public List<Contact> getOccupants() throws RemoteException {
		List<Contact> contacts = new ArrayList<Contact>(mOccupants.size());

		String jid;
		Contact contact;

		contact = new Contact(mOwnerJID);
		contacts.add(contact);

		for (String item : mOccupants) {
			jid = StringUtils.parseBareAddress(item);
			if (mOwnerJID.equals(jid)) {
				continue;
			}
			contact = new Contact(jid);
			contacts.add(contact);
		}

		return contacts;
	}

	private ParticipantStatusListener mParticipantStatusListener = new DefaultParticipantStatusListener() {
		public void joined(String participant) {
			String jid = StringUtils.parseResource(participant);
			jid = StringUtils.parseBareAddress(jid);
			Log.e(TAG, "Join Occupant:" + jid);
		};

		public void left(String participant) {
			String jid = StringUtils.parseResource(participant);
			jid = StringUtils.parseBareAddress(jid);
			Log.e(TAG, "Left Occupant:" + jid);
		};

		public void kicked(String participant, String actor, String reason) {
			String jid = StringUtils.parseResource(participant);
			jid = StringUtils.parseBareAddress(jid);
			Log.e(TAG, "Kick Occupant:" + jid + " Actor:" + actor + " Reason:"
					+ reason);
		};
	};
}
