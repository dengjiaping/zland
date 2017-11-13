package com.zhisland.android.blog.common.comment.eb;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.CommentSubject;

/**
 * Created by Mr.Tui on 2016/7/11.
 */
public class EBComment {

    public static final int ACTION_ADD = 1;
    public static final int ACTION_DELETE = 2;
    public static final int ACTION_UPDATA_COUNT = 3;

    public Comment comment;
    public int action;
    public String subjectId;
    public CommentSubject subjectType;

    public EBComment(CommentSubject subject, int action, String subjectId, Comment comment) {
        this.subjectType = subject;
        this.subjectId = subjectId;
        this.action = action;
        this.comment = comment;
    }
}
