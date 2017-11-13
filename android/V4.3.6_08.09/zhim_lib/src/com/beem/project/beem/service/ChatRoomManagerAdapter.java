package com.beem.project.beem.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.collections.ReferenceMap;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.beem.BeemService;
import com.beem.project.beem.service.aidl.IChatRoomManager;
import com.beem.project.beem.service.aidl.IChatRoomManagerListener;
import com.beem.project.beem.service.aidl.IMessageListener;
import com.zhisland.im.util.IdGenerator;

public class ChatRoomManagerAdapter extends IChatRoomManager.Stub {
	private static final String TAG = "ChatRoomManagerAdapter";
	public static final String CHAT_ROOM_DOMAIN = "conference";

	private final Connection connection;
	private List<String> roomNames = null;

	private Map<String, ChatRoomAdapter> threadChats = Collections
			.synchronizedMap(new ReferenceMap<String, ChatRoomAdapter>(
					ReferenceMap.HARD, ReferenceMap.WEAK));
	private Map<String, ChatRoomAdapter> jidChats = Collections
			.synchronizedMap(new ReferenceMap<String, ChatRoomAdapter>(
					ReferenceMap.HARD, ReferenceMap.WEAK));
	private Map<String, ChatRoomAdapter> bareNameChats = Collections
			.synchronizedMap(new ReferenceMap<String, ChatRoomAdapter>(
					ReferenceMap.HARD, ReferenceMap.WEAK));

	private final BeemService mService;

	public ChatRoomManagerAdapter(XMPPConnection connection,
			final BeemService service) {
		this.connection = connection;
		mService = service;
		addMessageListener();
	}

	private void addMessageListener() {
		MessageTypeFilter messageFilter = new MessageTypeFilter(
				Message.Type.groupchat);
		connection.addPacketListener(new PacketListener() {
			public void processPacket(Packet packet) {
				Message message = (Message) packet;

				Log.i("muc", "message.getFrom():" + message.getFrom());
				Log.i("muc",
						"StringUtils.parseBareAddress:"
								+ StringUtils.parseBareAddress(message
										.getFrom()));
				Log.i("muc",
						"StringUtils.parseName:"
								+ StringUtils.parseName(message.getFrom()));
				Log.i("muc",
						"StringUtils.parseServer:"
								+ StringUtils.parseServer(message.getFrom()));
				Log.i("muc",
						"StringUtils.parseResource:"
								+ StringUtils.parseResource(message.getFrom()));
				Log.i("muc", "message.getTo():" + message.getTo());
				String body = message.getBody();
				if (body == null || "".equals(body))
					return;

				String from = message.getFrom();
				String resource = StringUtils.parseResource(from);
				if (message.getTo().equals(resource))
					return;
				String name = StringUtils.parseName(from);
				if (name.equals(resource))
					return;

				com.beem.project.beem.service.Message msg = new com.beem.project.beem.service.Message(
						message);
				msg.setFrom(StringUtils.parseBareAddress(resource));

				String roomJID = StringUtils.parseBareAddress(from);

				ChatRoomAdapter chat;
				if (message.getThread() == null) {
					chat = getRoomChat(roomJID);
				} else {
					chat = getThreadChat(message.getThread());
					if (chat == null) {
						// Try to locate the chat based on the sender of the
						// message
						chat = getRoomChat(roomJID);
					}
				}
				if (chat == null) {
					chat = createChat(message, roomJID);
				}
				deliverMessage(chat, msg);
			}
		}, messageFilter);
	}

	private void deliverMessage(ChatRoomAdapter chat,
			com.beem.project.beem.service.Message message) {
		// Here we will run any interceptors
		chat.deliver(message);
	}

	public ChatRoomAdapter createChat(String roomJID, String thread,
			IMessageListener listener) {
		if (thread == null) {
			thread = IdGenerator.nextID();
		}
		ChatRoomAdapter chat = threadChats.get(thread);
		if (chat != null) {
			throw new IllegalArgumentException("ThreadID is already used");
		}
		chat = createChat(roomJID, thread, true);
		chat.addMessageListener(listener);
		return chat;
	}

	private ChatRoomAdapter createChat(String roomJID, String threadID,
			boolean createdLocally) {
		MultiUserChat muc = new MultiUserChat(connection, roomJID);
		ChatRoomAdapter chat = createChatRoom(roomJID, createdLocally, muc);
		// TODO make the thread id independent
		// threadChats.put(threadID, chat);

		return chat;
	}

	private ChatRoomAdapter createChatRoom(String roomJID,
			boolean createdLocally, MultiUserChat muc) {
		ChatRoomAdapter chat = new ChatRoomAdapter(muc);
		jidChats.put(roomJID, chat);
		String roomName = StringUtils.parseName(roomJID);
		bareNameChats.put(roomName, chat);

		final int n = mRemoteListeners.beginBroadcast();
		for (int i = 0; i < n; i++) {
			IChatRoomManagerListener listener = mRemoteListeners
					.getBroadcastItem(i);
			try {
				if (listener != null)
					listener.chatRoomCreated(chat, createdLocally);
			} catch (RemoteException e) {
				Log.w(TAG,
						"Error while deliver chatRoomCreated message to listener",
						e);
			}
		}
		mRemoteListeners.finishBroadcast();
		return chat;
	}

	private ChatRoomAdapter createChat(Message message, String roomJID) {
		String threadID = message.getThread();
		if (threadID == null) {
			threadID = IdGenerator.nextID();
		}

		return createChat(roomJID, threadID, false);
	}

	public ChatRoomAdapter getChatRoom(String roomName) throws RemoteException {
		ChatRoomAdapter chat = bareNameChats.get(roomName);
		if (chat == null) {
			chat = joinMultiUserChat(roomName, null, null, false);
		}
		return chat;
	}

	private ChatRoomAdapter getRoomChat(String roomJID) {
		return jidChats.get(roomJID);
	}

	public ChatRoomAdapter getThreadChat(String thread) {
		return threadChats.get(thread);
	}

	// blog.csdn.net/h7870181/article/details/12500231
	@Override
	public void createRoom(String roomName, String password)
			throws RemoteException {
		MultiUserChat muc = null;
		try {
			// 创建一个MultiUserChat
			String roomJID = roomName + "@" + CHAT_ROOM_DOMAIN + "."
					+ connection.getServiceName();
			muc = new MultiUserChat(connection, roomJID);
			// 创建聊天室
			muc.create(roomName);
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(connection.getUser());// �û�JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (password != null && !password.equals("")) {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);

			if (!jidChats.containsKey(roomJID)) {
				createChatRoom(roomJID, true, muc);
				saveMuc(muc);
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			// return null;
		}
		// return muc;
	}

	@Override
	public ChatRoomAdapter joinMultiUserChat(String roomName, String userName,
			String password, boolean daemon) throws RemoteException {
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			// MultiUserChat muc = multiUserChats.get(roomName).getMuc();

			// if (!multiUserChats.containsKey(roomName)) {
			String roomJID = roomName + "@" + CHAT_ROOM_DOMAIN + "."
					+ connection.getServiceName();
			MultiUserChat muc = new MultiUserChat(connection, roomJID);
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(0);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(userName == null ? connection.getUser() : userName,
					password, history,
					SmackConfiguration.getPacketReplyTimeout());
			Log.i("MultiUserChat", "�����ҡ�" + roomName + "������ɹ�........");

			// if (!multiUserChats.containsKey(roomName)) {
			if (!daemon) {
				if (!jidChats.containsKey(roomJID)) {
					createChatRoom(roomJID, true, muc);
					saveMuc(muc);
				}
				Collection<Affiliate> af = muc.getOwners();
				String userJID = connection.getUser();
				userJID = StringUtils.parseBareAddress(userJID);
				for (Affiliate affiliate : af) {
					if (affiliate.getJid().equals(userJID)) {
						jidChats.get(roomJID).setOwner(true);
					}
					jidChats.get(roomJID).setOwnerJID(affiliate.getJid());
					jidChats.get(roomJID).setUserJID(userJID);
				}
			}
			return getRoomChat(roomJID);
			// }
			// return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			Log.i("MultiUserChat", "�����ҡ�" + roomName + "������ʧ��........");
			// return null;
		}

		return null;
	}

	@Override
	public List<String> getHostRooms() throws RemoteException {
		if (roomNames != null)
			return roomNames;

		roomNames = new ArrayList<String>();
		try {
			new ServiceDiscoveryManager(connection);
			Collection<HostedRoom> hostrooms = MultiUserChat.getHostedRooms(
					connection,
					CHAT_ROOM_DOMAIN + "." + connection.getServiceName());
			for (HostedRoom entry : hostrooms) {
				roomNames.add(entry.getName());
				Log.i("room",
						"���֣�" + entry.getName() + " - ID:" + entry.getJid());
			}
			Log.i("room", "�����������:" + roomNames.size());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return roomNames;
	}

	private final RemoteCallbackList<IChatRoomManagerListener> mRemoteListeners = new RemoteCallbackList<IChatRoomManagerListener>();

	@Override
	public void addChatRoomCreationListener(IChatRoomManagerListener listen) {
		if (listen != null)
			mRemoteListeners.register(listen);
	}

	@Override
	public void removeChatRoomCreationListener(IChatRoomManagerListener listen) {
		if (listen != null) {
			mRemoteListeners.unregister(listen);
		}
	}

	public void registerRooms() {
		// MultiUserChat.addInvitationListener(connection, mInvitationListener);
		//
		// List<Contact> list = DBMgr.getHelper().getContactDao()
		// .fetchBeemContacts(Constants.CHAT_TYPE_GROUP, null);
		// if (list != null) {
		// String room;
		// for (Contact contact : list) {
		// room = contact.getName();
		// try {
		// joinMultiUserChat(room, null, null, true);
		// } catch (RemoteException e) {
		// Log.e(TAG, "join muc failed!", e);
		// }
		// }
		// }

	}

	private void saveMuc(MultiUserChat muc) {
		// comment out for find user only
		// Contact c = new Contact(muc.getRoom());
		// c.setName(StringUtils.parseName(muc.getRoom()));
		// c.setAvatarId("");
		// c.setType(Constants.CHAT_TYPE_GROUP);
		// DBMgr.getHelper().getContactDao().feedContact(c);
	}

	private InvitationListener mInvitationListener = new InvitationListener() {

		@Override
		public void invitationReceived(Connection conn, String room,
				String inviter, String reason, String password, Message message) {
			try {
				room = StringUtils.parseName(room);
				joinMultiUserChat(room, null, null, false);
			} catch (RemoteException e) {
				Log.e(TAG, "accept invitation failed!", e);
			}
		}
	};

}
