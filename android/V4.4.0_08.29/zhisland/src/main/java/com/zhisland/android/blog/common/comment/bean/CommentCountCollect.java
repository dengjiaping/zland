package com.zhisland.android.blog.common.comment.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 评论（观点）的相关数量集合
 * Created by Mr.Tui on 2016/8/31.
 */
public class CommentCountCollect extends OrmDto {

    public static final int LIKE_STATE_NULL = 0;
    public static final int LIKE_STATE_DONE = 1;

    /**
     * 评论（观点）回复总数
     * */
    @SerializedName("replyCount")
    public int replyCount;

    // 评论（观点）被赞总数
    @SerializedName("likeCount")
    public int likeCount;

    // 用户点赞状态（0 未点赞 1 己点赞)
    @SerializedName("likedState")
    public int likedState;
}
