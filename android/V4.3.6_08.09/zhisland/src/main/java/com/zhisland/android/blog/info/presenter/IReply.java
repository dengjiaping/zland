package com.zhisland.android.blog.info.presenter;

import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.Reply;

/**
 * Created by Mr.Tui on 2016/8/1.
 */
public interface IReply {

    /**
     * 回复的内容被点击
     * */
    void onReplyContentClick(Comment comment,Reply reply);
}
