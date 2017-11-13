package com.zhisland.android.blog.common.comment.presenter;

import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;

/**
 * Created by Mr.Tui on 2016/8/1.
 */
public interface OnReplyListener {

    /**
     * 点击事件，回复的内容被点击
     * */
    void onReplyContentClick(Comment comment, Reply reply);
}
