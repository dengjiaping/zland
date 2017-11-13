package com.zhisland.android.blog.info.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * 资讯的观点
 * Created by Mr.Tui on 2016/7/5.
 */
public class Comment extends OrmDto {

    //观点id
    @SerializedName("viewpointId")
    public long commentId;

    //观点内容
    @SerializedName("content")
    public String content;

    //发布时间
    @SerializedName("publishTime")
    public String publishTime;

    //相关数量集合
    @SerializedName("countCollect")
    public CountCollect countCollect;

    //发布人
    @SerializedName("publisher")
    public User publisher;

    //观点的回复
    @SerializedName("replys")
    public ArrayList<Reply> replys;

}
