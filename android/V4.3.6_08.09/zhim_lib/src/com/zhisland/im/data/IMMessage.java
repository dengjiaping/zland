package com.zhisland.im.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhisland.im.util.Constants;

@DatabaseTable(tableName = IMMessage.TABLE_NAME, daoClass = IMMessageDao.class)
public class IMMessage extends IMBase {

	public static final String TABLE_NAME = "im_message";

	public static final String col_id = "col_id";
	public static final String col_contact = "col_contact";
	public static final String col_type = "col_type";
	public static final String col_status = "col_status";
	public static final String col_progress = "col_progress";
	public static final String col_direction = "col_direction";
	public static final String col_subject = "col_subject";
	public static final String col_body = "col_body";
	public static final String col_display = "col_display";
	public static final String col_time = "col_time";
	public static final String col_thread = "col_thread";
	public static final String col_chat = "col_chat";

	public static final String col_local = "col_local";
	public static final String col_url = "col_url";
	public static final String col_token = "col_token";
	public static final String col_size = "col_size";
	public static final String col_duration = "col_duration";
	public static final String col_title = "col_title";
	public static final String col_description = "col_description";
	public static final String col_is_read = "col_is_read";

	@DatabaseField(columnName = IMMessage.col_id, generatedId = true)
	public Long id;

	/**
	 * same as com.beem.project.beem.service.MessageThread field, is a client
	 * generated id for a message, transparent for xmpp server
	 */
	@DatabaseField(columnName = IMMessage.col_thread)
	public String thread;

	@DatabaseField(columnName = IMMessage.col_contact)
	public String contact;

	@DatabaseField(columnName = IMMessage.col_type)
	public Integer type;

	/**
	 * {@code Constants#MSG_OUTGOING}}
	 */
	@DatabaseField(columnName = IMMessage.col_status)
	public Integer status;

	@DatabaseField(columnName = IMMessage.col_progress)
	public Integer progress;

	/**
	 * see {@link Constants#MSG_OUTGOING}
	 */
	@DatabaseField(columnName = IMMessage.col_direction)
	public Integer direction;

	@DatabaseField(columnName = IMMessage.col_subject)
	public String subject;

	/** Not-null value. */
	@DatabaseField(columnName = IMMessage.col_body)
	public String body;

	@DatabaseField(columnName = IMMessage.col_display)
	public String display;

	@DatabaseField(columnName = IMMessage.col_time)
	public long time;

	@DatabaseField(columnName = IMMessage.col_chat)
	public long chat;

	@DatabaseField(columnName = IMMessage.col_local)
	public String local;

	@DatabaseField(columnName = IMMessage.col_url)
	public String url;

	@DatabaseField(columnName = IMMessage.col_token)
	public long token;

	@DatabaseField(columnName = IMMessage.col_size)
	public String size;

	@DatabaseField(columnName = IMMessage.col_duration)
	public int duration;

	@DatabaseField(columnName = IMMessage.col_title)
	public String title;

	@DatabaseField(columnName = IMMessage.col_description)
	public String description;

	@DatabaseField(columnName = IMMessage.col_is_read)
	public boolean isRead = true;

	public boolean isSendBySelf() {
		return direction == Constants.MSG_OUTGOING;
	}
}
