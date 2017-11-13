package com.zhisland.android.blog.info.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * 对观点的回复
 * Created by Mr.Tui on 2016/7/5.
 */
public class Reply extends OrmDto {

    //回复id
    @SerializedName("replyId")
    public long replyId;

    //回复类型 1 对观点的回复 2 对回复的回复
    @SerializedName("type")
    public int type;

    //父级回复ID（如果type=2,则此字段有值,表示此回复是对某回复ID的回复)
    @SerializedName("parentReplyId")
    public long parentReplyId;

    @SerializedName("publisher")
    public User publisher;

    //被回复用户Uid (如果type=2,则此字段有值)
    @SerializedName("reviewedUid")
    public long reviewedUid;

    //被回复用户名称 (如果type=2,则此字段有值)
    @SerializedName("reviewedName")
    public String reviewedName;

    //回复时间
    @SerializedName("pulblishTime")
    public String pulblishTime;

    //回复内容
    @SerializedName("content")
    public String content;

}
