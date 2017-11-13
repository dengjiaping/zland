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

import java.util.Date;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhisland.im.smack.MessageMetadataExtension;
import com.zhisland.im.util.Constants;
import com.zhisland.im.util.IdGenerator;
import com.zhisland.lib.util.StringUtil;

/**
 * This class represents a instant message.
 *
 * @author darisk
 */
public class Message implements Parcelable {

	/**
	 * Normal message type. Theese messages are like an email, with subject.
	 */
	public static final int MSG_TYPE_NORMAL = 100;

	/**
	 * Chat message type.
	 */
	public static final int MSG_TYPE_CHAT = 200;

	/**
	 * Group chat message type.
	 */
	public static final int MSG_TYPE_GROUP_CHAT = 300;

	/**
	 * Error message type.
	 */
	public static final int MSG_TYPE_ERROR = 400;

	/**
	 * Informational message type.
	 */
	public static final int MSG_TYPE_INFO = 500;

	/**
	 * Parcelable.Creator needs by Android.
	 */
	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {

		@Override
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};

	private int mType;
	private int mSubType;
	// Text message content or base64 string
	private String mBody;
	private String mSubject;
	private String mTo;
	private String mFrom;
	private String mThread;
	private Date mTimestamp;
	// Resource local location
	private String mLocal;
	private String mUrl;
	private long mToken;
	private String mSize;
	private int mDuration;
	private String mTitle;
	private String mDescription;
	private String mMessageThread;
	private int mStatus = Constants.MSG_STATUS_NORMAL;
	private int mProgress;
	private Object mTag;

	// TODO ajouter l'erreur

	/**
	 * Constructor.
	 *
	 * @param to
	 *            the destinataire of the message
	 * @param type
	 *            the message type
	 */
	public Message(final String to, final int type) {
		mTo = to;
		mType = type;
		mSubType = Constants.MSG_TYPE_TEXT;
		mBody = "";
		mSubject = "";
		mThread = "";
		mFrom = null;
		mTimestamp = new Date();
		mStatus = Constants.MSG_STATUS_SENDING;
		mMessageThread = IdGenerator.nextID();
	}

	/**
	 * Constructor a message of type chat.
	 *
	 * @param to
	 *            the destinataire of the message
	 */
	public Message(final String to) {
		this(to, MSG_TYPE_CHAT);
	}

	/**
	 * Construct a message from a smack message packet.
	 *
	 * @param smackMsg
	 *            Smack message packet
	 */
	public Message(final org.jivesoftware.smack.packet.Message smackMsg) {
		this(smackMsg.getTo());
		switch (smackMsg.getType()) {
		case chat:
			mType = MSG_TYPE_CHAT;
			break;
		case groupchat:
			mType = MSG_TYPE_GROUP_CHAT;
			break;
		case normal:
			mType = MSG_TYPE_NORMAL;
			break;
		// TODO gerer les message de type error
		// this a little work around waiting for a better handling of error
		// messages
		case error:
			mType = MSG_TYPE_ERROR;
			break;
		default:
			mType = MSG_TYPE_NORMAL;
			break;
		}
		this.mFrom = smackMsg.getFrom();
		// TODO better handling of error messages
		if (mType == MSG_TYPE_ERROR) {
			XMPPError er = smackMsg.getError();
			String msg = er.getMessage();
			if (msg != null)
				mBody = msg;
			else
				mBody = er.getCondition();
		} else {
			mBody = smackMsg.getBody();
			mSubject = smackMsg.getSubject();
			mThread = smackMsg.getThread();
		}

		try {
			MessageMetadataExtension meta = (MessageMetadataExtension) smackMsg
					.getExtension("zh:xmpp:message:metadata");
			MessageMetadataExtension.Info info = meta.getInfo();
			mSubType = info.getType();
			mUrl = info.getUrl();
			mToken = info.getToken();
			mSize = info.getSize();
			mDuration = info.getDuration();
			mTitle = info.getTitle();
			mDescription = info.getDescription();
			mMessageThread = info.getMessageThread();
		} catch (Exception e) {
			mSubType = Constants.MSG_TYPE_TEXT;
		}
		mMessageThread = smackMsg.getPacketID();

		PacketExtension pTime = smackMsg
				.getExtension("delay", "urn:xmpp:delay");
		if (pTime instanceof DelayInformation) {
			mTimestamp = ((DelayInformation) pTime).getStamp();
		} else {
			mTimestamp = new Date();
		}
	}

	/**
	 * Construct a message from a parcel.
	 *
	 * @param in
	 *            parcel to use for construction
	 */
	private Message(final Parcel in) {
		mType = in.readInt();
		mSubType = in.readInt();
		mTo = in.readString();
		mBody = in.readString();
		mSubject = in.readString();
		mThread = in.readString();
		mFrom = in.readString();
		mTimestamp = new Date(in.readLong());
		mLocal = in.readString();
		mUrl = in.readString();
		mToken = in.readLong();
		mSize = in.readString();
		mDuration = in.readInt();
		mTitle = in.readString();
		mDescription = in.readString();
		mMessageThread = in.readString();
		mStatus = in.readInt();
		mProgress = in.readInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mType);
		dest.writeInt(mSubType);
		dest.writeString(mTo);
		dest.writeString(mBody);
		dest.writeString(mSubject);
		dest.writeString(mThread);
		dest.writeString(mFrom);
		dest.writeLong(mTimestamp.getTime());
		dest.writeString(mLocal);
		dest.writeString(mUrl);
		dest.writeLong(mToken);
		dest.writeString(mSize);
		dest.writeInt(mDuration);
		dest.writeString(mTitle);
		dest.writeString(mDescription);
		dest.writeString(mMessageThread);
		dest.writeInt(mStatus);
		dest.writeInt(mProgress);
	}

	/**
	 * Get the type of the message.
	 *
	 * @return the type of the message.
	 */
	public int getType() {
		return mType;
	}

	/**
	 * Set the type of the message.
	 *
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		mType = type;
	}

	public int getSubType() {
		return mSubType;
	}

	public void setSubType(int subType) {
		mSubType = subType;
	}

	/**
	 * Get the body of the message.
	 *
	 * @return the Body of the message
	 */
	public String getBody() {
		return mBody;
	}

	/**
	 * Set the body of the message.
	 *
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		mBody = body;
	}

	/**
	 * Get the subject of the message.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * Set the subject of the message.
	 *
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		mSubject = subject;
	}

	/**
	 * Get the destinataire of the message.
	 *
	 * @return the destinataire of the message
	 */
	public String getTo() {
		return mTo;
	}

	/**
	 * Set the destinataire of the message.
	 *
	 * @param to
	 *            the destinataire to set
	 */
	public void setTo(String to) {
		mTo = to;
	}

	/**
	 * Set the from field of the message.
	 *
	 * @param from
	 *            the mFrom to set
	 */
	public void setFrom(String from) {
		this.mFrom = from;
	}

	/**
	 * Get the from field of the message.
	 *
	 * @return the mFrom
	 */
	public String getFrom() {
		return mFrom;
	}

	/**
	 * Get the thread of the message.
	 *
	 * @return the thread
	 */
	public String getThread() {
		return mThread;
	}

	/**
	 * Set the thread of the message.
	 *
	 * @param thread
	 *            the thread to set
	 */
	public void setThread(String thread) {
		mThread = thread;
	}

	/**
	 * Set the Date of the message.
	 *
	 * @param date
	 *            date of the message.
	 */
	public void setTimestamp(Date date) {
		mTimestamp = date;
	}

	/**
	 * Get the Date of the message.
	 *
	 * @return if it is a delayed message get the date the message was sended.
	 */
	public Date getTimestamp() {
		return mTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLocal() {
		return mLocal;
	}

	public void setLocal(String local) {
		this.mLocal = local;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public long getToken() {
		return mToken;
	}

	public void setToken(long token) {
		this.mToken = token;
	}

	public String getSize() {
		return mSize;
	}

	public void setSize(String size) {
		this.mSize = size;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		this.mDuration = duration;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getMessageThread() {
		return mMessageThread;
	}

	public void setMessageThread(String messageThread) {
		mMessageThread = messageThread;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		mStatus = status;
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
	}

	public boolean isSendBySelf() {
		return StringUtil.isNullOrEmpty(mFrom);
	}

	public long getMillions() {
		return mTimestamp == null ? 0 : mTimestamp.getTime();
	}

	public boolean isRead() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		mTag = tag;
	}
}
