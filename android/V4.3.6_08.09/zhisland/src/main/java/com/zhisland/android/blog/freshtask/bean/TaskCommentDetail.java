package com.zhisland.android.blog.freshtask.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * 神评论任务详情对象
 */
public class TaskCommentDetail extends OrmDto {
    /**
     * 神评论分享方案标题
     */
    @SerializedName("shareTitle")
    public String shareTitle;

    /**
     * 神评论分享方案描述
     */
    @SerializedName("shareDesc")
    public String shareDesc;

    /**
     * 神评论分享方案链接路径
     */
    @SerializedName("shareLinkUrl")
    public String shareLinkUrl;

    /**
     * 神评论任务条数
     */
    @SerializedName("taskCount")
    public Integer taskCount;

    /**
     * 己完成条数
     */
    @SerializedName("finishedCount")
    public Integer finishedCount;

    /**
     * 当前任务状态
     */
    @SerializedName("state")
    public CustomState state;

    /**
     * 神评论列表
     */
    @SerializedName("comments")
    public List<UserComment> comments;


}
