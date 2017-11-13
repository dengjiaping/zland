package com.zhisland.im.event;


/**
 * 当收到一条新的消息时，XMPP类库会通过eventbus抛出这个事件
 *
 * @author arthur
 */
public class EventMsg {

    private String jid;
    private int unreadCount;
    private String msgBody;

    public EventMsg(String jid, int unreadCount, String msgBody) {
        this.jid = jid;
        this.unreadCount = unreadCount;
        this.msgBody = msgBody;
    }

    public String getJid() {
        return jid;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public String getMsgBody() {
        return msgBody;
    }

}
