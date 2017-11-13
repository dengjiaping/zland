package com.zhisland.im.smack;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;

import com.zhisland.im.BeemApplication;
import com.zhisland.im.data.IMContact;

public class ZMessage extends Message {
	private Type type = Type.normal;

	public void setType(Type type) {
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null.");
		}
		this.type = type;
	}

	public enum Type {

		/**
		 * (Default) a normal text message used in email like interface.
		 */
		normal,

		/**
		 * Typically short text message used in line-by-line chat interfaces.
		 */
		chat,

		/**
		 * Chat message sent to a groupchat server for group chats.
		 */
		groupchat,

		/**
		 * Text message to be displayed in scrolling marquee displays.
		 */
		headline,

		/**
		 * indicates a messaging error.
		 */
		error,

		friend,

		subscribe,

		subscribed;

		public static Type fromString(String name) {
			try {
				return Type.valueOf(name);
			} catch (Exception e) {
				return normal;
			}
		}

	}

	private String from;
	private String body;

	public void setBody(String message) {
		body = message;
	}

	public String toXML() {
		from = IMContact.buildJid(BeemApplication.getInstance()
				.getXmppAccount().getUserId());
		StringBuilder buf = new StringBuilder();
		buf.append("<message");
		if (getXmlns() != null) {
			buf.append(" xmlns=\"").append(getXmlns()).append("\"");
		}
		// if (language != null) {
		// buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
		// }
		if (getPacketID() != null) {
			buf.append(" id=\"").append(getPacketID()).append("\"");
		}
		if (getTo() != null) {
			buf.append(" to=\"").append(StringUtils.escapeForXML(getTo()))
					.append("\"");
		}
		// if (getFrom() != null) {
		buf.append(" from=\"").append(StringUtils.escapeForXML(from))
				.append("\"");
		// }
		if (type != Type.normal) {
			buf.append(" type=\"").append(type).append("\"");
		}
		buf.append(">");

		buf.append("<body>").append(body).append("</body>");

		// Append the error subpacket if the message type is an error.
		if (type == Type.error) {
			XMPPError error = getError();
			if (error != null) {
				buf.append(error.toXML());
			}
		}
		// Add packet extensions, if any are defined.
		buf.append(getExtensionsXML());
		buf.append("</message>");
		return buf.toString();
	}
}
