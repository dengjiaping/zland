package com.zhisland.android.blog.tabhome.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * 新手任务 及 开机神评论
 */
public class FreshTaskAndComment extends OrmDto {

    @SerializedName("taskCardList")
    public TaskCardList cardList;

    @SerializedName("bootCommentList")
    public List<UserComment> userComment;
}
