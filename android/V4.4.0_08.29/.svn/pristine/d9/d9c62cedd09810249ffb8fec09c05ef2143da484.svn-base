package com.zhisland.im.smack;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * PacketExtension to represent the Message metadata.
 * XML namespace zh:xmpp:message:metadata
 */
public class MessageMetadataExtension implements PacketExtension {
    private Info mInfo;

    /**
     * Create an MessageMetadataExtension.
     */
    public MessageMetadataExtension() {
    }

    public void setInfo(Info info) {
    	mInfo = info;
    }

    /**
     * Get the metadata informations.
     *
     * @return a information
     */
    public Info getInfo() {
        return mInfo;
    }

    @Override
    public String getElementName() {
        return "metadata";
    }

    @Override
    public String getNamespace() {
        return "zh:xmpp:message:metadata";
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder("<metadata xmlns=\"");
        builder.append(getNamespace()).append("\">");
        if (mInfo != null)
            builder.append(mInfo.toXML());
        builder.append("</metadata>");
        return builder.toString();
    }

    /**
     * A metadata information element.
     */
    public static class Info {
        private int mType;
        private String mUrl;
        private long mToken;
        private String mSize;
        private int mDuration;
        private String mTitle;
        private String mDescription;
        private String mMessageThread;

        public Info() {
        }

        public int getType() {
			return mType;
		}

		public void setType(int type) {
			mType = type;
		}

		public String getUrl() {
			return mUrl;
		}

		public void setUrl(String url) {
			mUrl = url;
		}

		public long getToken() {
			return mToken;
		}
		
		public void setToken(long token) {
			mToken = token;
		}

		public String getSize() {
			return mSize;
		}

		public void setSize(String size) {
			mSize = size;
		}

		public int getDuration() {
			return mDuration;
		}

		public void setDuration(int duration) {
			mDuration = duration;
		}

		public String getTitle() {
			return mTitle;
		}

		public void setTitle(String title) {
			mTitle = title;
		}

		public String getDescription() {
			return mDescription;
		}

		public void setDescription(String description) {
			mDescription = description;
		}

		public String getMessageThread() {
			return mMessageThread;
		}

		public void setMessageThread(String messageThread) {
			mMessageThread = messageThread;
		}
		/**
         * Return this information as an xml element.
         *
         * @return an xml element representing this information
         */
        public String toXML() {
            StringBuilder builder = new StringBuilder("<info ");
            builder.append("type=\"" + mType + "\"");
            if (mUrl != null)
                builder.append(" url=\"" + mUrl + "\"");
            if (mToken != 0)
            	builder.append(" token=\"" + mToken + "\"");
            if (mSize != null)
                builder.append(" size=\"" + mSize + "\"");
            if (mDuration != 0)
                builder.append(" duration=\"" + mDuration + "\"");
            if (mTitle != null)
            	builder.append(" title=\"" + mTitle + "\"");
            if (mDescription != null)
            	builder.append(" description=\"" + mDescription + "\"");
            if (mMessageThread != null)
            	builder.append(" messageThread=\"" + mMessageThread + "\"");
            builder.append(" />");
            return builder.toString();
        }
    }
}
