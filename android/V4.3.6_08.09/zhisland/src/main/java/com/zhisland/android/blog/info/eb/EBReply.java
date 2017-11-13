package com.zhisland.android.blog.info.eb;

import com.zhisland.android.blog.info.bean.Reply;

/**
 * Created by Mr.Tui on 2016/7/11.
 */
public class EBReply {

    public static final int ACTION_ADD = 1;
    public static final int ACTION_DELETE = 2;

    public Reply reply;
    public int action;
    public long commentId;

    public EBReply(int action, long commmentId, Reply reply) {
        this.commentId = commmentId;
        this.action = action;
        this.reply = reply;
    }

}
