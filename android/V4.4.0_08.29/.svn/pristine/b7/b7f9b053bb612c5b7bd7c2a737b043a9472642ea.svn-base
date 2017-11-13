package com.zhisland.android.blog.common.comment.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.feed.bean.CustomIcon;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * 评论（观点）
 * Created by Mr.Tui on 2016/7/5.
 */
public class Comment extends OrmDto {

    //评论（观点）id
    @SerializedName("commentId")
    public long commentId;

    /**
     * 观点id.资讯中的字段,只用于兼容接口返回的别名不同，将其赋值到commentId，统一处理。
     * ps：不要使用该字段。
     */
    @SerializedName("viewpointId")
    public long viewpointId;

    //评论（观点）内容
    @SerializedName("content")
    public String content;

    //发布时间
    @SerializedName("publishTime")
    public String publishTime;

    //相关数量集合
    @SerializedName("countCollect")
    public CommentCountCollect countCollect;

    //发布人
    @SerializedName("publisher")
    public User publisher;

    //赞的状态
    @SerializedName("like")
    public CustomIcon likeCustomIcon;

    //赞的状态
    @SerializedName("replyCount")
    public int replyCount;

    //评论（观点）的回复
    @SerializedName("replyList")
    public ArrayList<Reply> replyList;

    //评论（观点）的回复
    @SerializedName("replys")
    public ArrayList<Reply> replys;

}
