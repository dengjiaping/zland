package com.zhisland.android.blog.im.eb;

/**
 * 消息eventbus
 */
public class EBMessage {

    /**
     * 新增粉丝/互动消息/系统消息 数目刷新
     */
    public static final int TYPE_MESSAGE_REFRESH = 1;

    public int type;
    public Object obj;

    public EBMessage(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

}
