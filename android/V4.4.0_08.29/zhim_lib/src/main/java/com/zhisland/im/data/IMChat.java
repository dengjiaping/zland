package com.zhisland.im.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = IMChat.TABLE_NAME, daoClass = IMChatDao.class)
public class IMChat extends IMBase {

	public static final String TABLE_NAME = "imchat";

	public static final String COL_ID = "chat_id";
	public static final String COL_CONTACT = "contact";
	public static final String COL_TYPE = "type";
	public static final String COL_NAME = "name";
	public static final String COL_AVATAR = "avatar";
	public static final String COL_MESSAGE = "message";
	public static final String COL_TIME = "time";
	public static final String COL_UNREAD = "unread";

	@DatabaseField(columnName = IMChat.COL_ID, generatedId = true)
	public Long id;

	/**
	 * jid the identifier of chat object
	 */
	@DatabaseField(columnName = IMChat.COL_CONTACT)
	public String contact;

	@DatabaseField(columnName = IMChat.COL_TYPE)
	public Integer type;

	@DatabaseField(columnName = IMChat.COL_NAME)
	public String name;

	@DatabaseField(columnName = IMChat.COL_AVATAR)
	public String avatar;

	@DatabaseField(columnName = IMChat.COL_MESSAGE)
	public String message;

	@DatabaseField(columnName = IMChat.COL_TIME, dataType = DataType.DATE_LONG)
	public java.util.Date time;

	@DatabaseField(columnName = IMChat.COL_UNREAD)
	public Integer unread;
}
