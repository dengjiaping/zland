package com.zhisland.im.smack;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * A PacketExtensionProvider to parse the Message metadata.
 * XML namespace zh:xmpp:message:metadata
 */
public class MessageMetadataProvider implements PacketExtensionProvider {

    /**
     * Creates a new AvatarMetadataProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
     */
    public MessageMetadataProvider() {
    }

    @Override
    public PacketExtension parseExtension(XmlPullParser parser)
            throws Exception {
        MessageMetadataExtension metadata = new MessageMetadataExtension();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if ("info".equals(parser.getName())) {
                    String sType = parser.getAttributeValue(null, "type");
                    String url = parser.getAttributeValue(null, "url");
                    String sToken = parser.getAttributeValue(null, "token");
                    String size = parser.getAttributeValue(null, "size");
                    String sDuration = parser.getAttributeValue(null, "duration");
                    String title = parser.getAttributeValue(null, "title");
                    String description = parser.getAttributeValue(null, "description");
                    String messageThread = parser.getAttributeValue(null, "messageThread");
                    int type = 200;
                    long token = 0;
                    int duration = 0;
                    MessageMetadataExtension.Info info = null;
                    try {
                        if (sType != null)
                            type = Integer.parseInt(sType);
                        if (sToken != null)
                            token = Long.parseLong(sToken);
                        if (sDuration != null)
                        	duration = Integer.parseInt(sDuration);
                    } catch (NumberFormatException e) {
                    }
                    info = new MessageMetadataExtension.Info();
                    info.setType(type);
                    info.setUrl(url);
                    info.setToken(token);
                    info.setSize(size);
                    info.setDuration(duration);
                    info.setTitle(title);
                    info.setDescription(description);
                    info.setMessageThread(messageThread);

                    metadata.setInfo(info);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals(metadata.getElementName())) {
                    done = true;
                }
            }
        }
        return metadata;
    }
}
