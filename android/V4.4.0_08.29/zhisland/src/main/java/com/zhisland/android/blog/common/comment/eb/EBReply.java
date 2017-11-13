package com.zhisland.android.blog.common.comment.eb;

import com.zhisland.android.blog.common.comment.bean.CommentSubject;
import com.zhisland.android.blog.common.comment.bean.Reply;

/**
 * Created by Mr.Tui on 2016/7/11.
 */
public class EBReply {

    public static final int ACTION_ADD = 1;
    public static final int ACTION_DELETE = 2;

    public Reply reply;
    public int action;
    public long commentId;
    public CommentSubject subjectType;

    public EBReply(CommentSubject subject,int action, long commmentId, Reply reply) {
        this.subjectType = subject;
        this.commentId = commmentId;
        this.action = action;
        this.reply = reply;
    }

}
