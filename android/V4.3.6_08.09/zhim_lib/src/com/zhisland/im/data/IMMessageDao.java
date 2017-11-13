package com.zhisland.im.data;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.beem.project.beem.service.Message;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.zhisland.im.util.Constants;
import com.zhisland.lib.util.MLog;

public class IMMessageDao extends IMBaseDao<IMMessage, Long> {

	private static final String TAG = "msgDao";

	/**
	 * 
	 * @param contact
	 * @param mMessages
	 */
	public List<IMMessage> loadMessages(String contact, long id, long limit) {

		long chatId = DBMgr.getHelper().getChatDao().getChatId(contact);

		if (chatId == -1)
			return null;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.orderBy(IMMessage.col_id, false).limit(limit).where()
					.eq(IMMessage.col_chat, chatId).and()
					.lt(IMMessage.col_id, id);
			List<IMMessage> msgs = qb.query();
			int count = msgs == null ? 0 : msgs.size();
			MLog.d(TAG, "load message for: " + contact + " with count: "
					+ count);
			if (msgs != null) {
				Collections.reverse(msgs);
			}
			return msgs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 存储消息
	 * 
	 * @param contact
	 * @param message
	 * @param unread
	 */
	public void logMessage(String contact, Message message, int unread) {
		long chatId = DBMgr.getHelper().getChatDao()
				.feedChat(contact, Constants.CHAT_TYPE_NORMAL, message, unread);

		if (chatId == -1)
			return;

		if (updateMessage(message))
			return;

		String display = "";
		int direction = Constants.MSG_OUTGOING;
		if (!contact.equalsIgnoreCase(message.getTo())) {
			direction = Constants.MSG_INCOMING;
			display = contact;
		}
		storeMessage(contact, chatId, message, display, direction);
	}

	/**
	 * 增加一条section title样式的文本信息，多用来展示通知信息
	 * 
	 * @param chatId
	 * @param content
	 */
	public void logSectionMessage(long chatId, String content) {
		IMMessage msg = new IMMessage();
		msg.body = content;
		msg.chat = chatId;
		msg.time = System.currentTimeMillis();
		msg.isRead = true;
		msg.type = Constants.MSG_TYPE_SECTIOIN_TITLE;
		try {
			create(msg);
			notifyAdd(msg);
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
	}

	private void storeMessage(String contact, long chatId, Message message,
			String display, int direction) {
		IMMessage msg = new IMMessage();
		msg.chat = chatId;
		msg.contact = contact;
		msg.type = message.getSubType();
		msg.subject = message.getSubject();
		msg.body = message.getBody();
		msg.time = message.getTimestamp().getTime();
		msg.display = display;
		msg.direction = direction;
		msg.local = message.getLocal();
		msg.url = message.getUrl();
		msg.token = message.getToken();
		msg.size = message.getSize();
		msg.duration = message.getDuration();
		msg.title = message.getTitle();
		msg.description = message.getDescription();
		msg.status = message.getStatus();
		msg.progress = message.getProgress();
		msg.thread = message.getMessageThread();
		if (msg.type == Constants.MSG_TYPE_AUDIO
				&& direction == Constants.MSG_INCOMING) {
			msg.isRead = false;
		}
		try {
			create(msg);
			notifyAdd(msg);
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
	}

	public List<Message> loadRoomMessages(String room, long time, long limit) {
		List<Message> list = new LinkedList<Message>();
		long chatId = DBMgr.getHelper().getChatDao().getChatId(room);
		if (chatId == -1)
			return null;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.orderBy(IMMessage.col_time, false).limit(limit).where()
					.eq(IMMessage.col_chat, chatId).and()
					.lt(IMMessage.col_time, new Date(time));
			// qb.orderBy(IMMessage.col_id, false);
			List<IMMessage> msgs = qb.query();

			Message message;
			for (IMMessage msg : msgs) {
				message = generateMessage(msg);
				list.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void logRoomMessage(String room, Message message, int direction,
			int unread) {
		long chatId = DBMgr.getHelper().getChatDao()
				.feedChat(room, Constants.CHAT_TYPE_GROUP, message, unread);

		if (chatId == -1)
			return;

		if (updateMessage(message))
			return;

		String display = "";
		if (direction == Constants.MSG_INCOMING) {
			display = message.getFrom();
		}
		storeMessage(room, chatId, message, display, direction);
	}
	
	public void deleteMessage(IMMessage msg){
		try {
			delete(msg);
			IMMessage imMessage = getLastMsgForChat(msg.chat);
			DBMgr.getHelper().getChatDao().updateChatMessage(msg.chat, imMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IMMessage getLastMsgForChat(long chatId){
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.orderBy(IMMessage.col_time, false).limit(1).where()
					.eq(IMMessage.col_chat, chatId);
			List<IMMessage> msgs = qb.query();

			if(msgs != null && msgs.size() > 0){
				return msgs.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean updateMessage(Message message) {
		boolean result = false;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_thread, message.getMessageThread());
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				item.status = message.getStatus();
				item.progress = message.getProgress();
				item.local = message.getLocal();
				item.url = message.getUrl();
				item.token = message.getToken();
				update(item);
				notifyUpdate(item);
				result = true;
			}
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
		return result;
	}

	public Message fetchMessageByToken(long token) {
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_token, token);
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				return generateMessage(item);
			}
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}

		return null;
	}

	public Message fetchMessageByThread(String thread) {
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_thread, thread);
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				return generateMessage(item);
			}
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}

		return null;
	}

	private Message generateMessage(IMMessage item) {
		Message message;

		if (item.direction == Constants.MSG_INCOMING) {
			message = new Message("");
			message.setFrom(item.display);
		} else {
			message = new Message(item.display);
			message.setFrom("");
			message.setTo(item.contact);
		}
		message.setSubType(item.type);
		message.setSubject(item.subject);
		message.setBody(item.body);
		message.setTimestamp(new Date(item.time));
		message.setMessageThread(item.thread);

		message.setLocal(item.local);
		message.setUrl(item.url);
		message.setToken(item.token);
		message.setSize(item.size);
		message.setDuration(item.duration);
		message.setTitle(item.title);
		message.setDescription(item.description);
		message.setStatus(item.status);
		message.setProgress(item.progress);

		return message;
	}

	public List<IMMessage> getPicMessages(long chatId) {
		if (chatId <= 0)
			return null;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.orderBy(IMMessage.col_time, false).where()
					.eq(IMMessage.col_chat, chatId).and()
					.eq(IMMessage.col_type, Constants.MSG_TYPE_IMAGE);
			List<IMMessage> msgs = qb.query();
			return msgs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public void deleteAllMsgWithSomeOne(long chatId) {
		if (chatId <= 0)
			return;
		DeleteBuilder<IMMessage, Long> db = deleteBuilder();
		try {
			db.where().eq(IMMessage.col_chat, chatId);
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IMMessage getMessageByThread(String thread) {
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_thread, thread);
			IMMessage item = qb.queryForFirst();
			return item;
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
		return null;
	}

	public boolean updateToken(IMMessage message) {
		boolean result = false;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_id, message.id);
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				item.token = message.token;
				update(item);
				notifyUpdate(item);
				result = true;
			}
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
		return result;
	}

	public boolean updateTokenAndLocal(IMMessage message) {
		boolean result = false;
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_id, message.id);
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				item.token = message.token;
				item.local = message.local;
				update(item);
				notifyUpdate(item);
				result = true;
			}
		} catch (Exception e) {
			Log.d("db", e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 将指定消息的读取状态改为已读
	 * 
	 * @param id
	 */
	public void readMsg(long id) {
		// TODO Auto-generated method stub
		QueryBuilder<IMMessage, Long> qb = queryBuilder();
		try {
			qb.where().eq(IMMessage.col_id, id);
			IMMessage item = qb.queryForFirst();
			if (item != null) {
				item.isRead = true;
				if (update(item) > 0)
					notifyUpdate(item);
			}
		} catch (Exception e) {
			MLog.e(TAG, e.getMessage(), e);
		}

	}

	public void resetMsgLoadStatus() {
		UpdateBuilder<IMMessage, Long> ub = this.updateBuilder();
		try {
			ub.where().eq(IMMessage.col_status, Constants.MSG_STATUS_SENDING);
			ub.updateColumnValue(IMMessage.col_status,
					Constants.MSG_STATUS_ERROR);
			ub.update();
		} catch (Exception e) {
			MLog.e(TAG, e.getMessage(), e);
		}

	}

	public IMMessageDao(ConnectionSource connectionSource,
			DatabaseTableConfig<IMMessage> tableConfig) throws SQLException {
		super(connectionSource, tableConfig);
	}

	public IMMessageDao(ConnectionSource connectionSource,
			Class<IMMessage> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public IMMessageDao(Class<IMMessage> dataClass) throws SQLException {
		super(dataClass);
	}

}
