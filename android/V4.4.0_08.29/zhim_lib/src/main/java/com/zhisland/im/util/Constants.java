package com.zhisland.im.util;

public class Constants {

	public static final int CHAT_TYPE_NORMAL = 0;
	public static final int CHAT_TYPE_GROUP = 1;

	public static final int CONTACT_SUB_NONE = -1;
	public static final int CONTACT_SUB_FROM = 1;
	public static final int CONTACT_SUB_TO = 2;
	public static final int CONTACT_SUB_BOTH = 3;
	public static final int CONTACT_SUB_REMOVE = 4;

	public static final int CONTACT_ASK_NONE = -1;
	public static final int CONTACT_ASK_REQUEST = 1;
	public static final int CONTACT_ASK_RECEIVE = 2;
	public static final int CONTACT_ASK_RESPONSE = 3;
	public static final int CONTACT_ASK_RES_FIN = 4;
	public static final int CONTACT_ASK_ACCEPT = 5;
	public static final int CONTACT_ASK_ACC_FIN = 6;
	public static final int CONTACT_ASK_DENY = 7;
	public static final int CONTACT_ASK_REMOVE = 8;

	public static final int MSG_TYPE_SECTIOIN_TITLE = 100;
	public static final int MSG_TYPE_TEXT = 200;
	public static final int MSG_TYPE_IMAGE = 300;
	public static final int MSG_TYPE_AUDIO = 400;
	public static final int MSG_TYPE_VIDEO = 500;
	public static final int MSG_TYPE_INFO = 600;
	public static final int MSG_TYPE_LOC = 700;
	public static final int MSG_TYPE_VCARD = 800;

	public static final int MSG_INCOMING = 0;
	public static final int MSG_OUTGOING = 1;

	public static final int MSG_STATUS_SENDING = 100;
	// no receiving status for each message
	// public static final int MSG_STATUS_RECEIVING = 200;
	public static final int MSG_STATUS_FILE_ERROR = 300;
	public static final int MSG_STATUS_CONTINUE = 400;
	public static final int MSG_STATUS_ERROR = 500;
	public static final int MSG_STATUS_NORMAL = 600;
}
