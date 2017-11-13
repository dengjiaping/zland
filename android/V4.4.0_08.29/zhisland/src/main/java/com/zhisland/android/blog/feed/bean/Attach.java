package com.zhisland.android.blog.feed.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 基本的附件数据结构
 * Created by arthur on 2016/9/1.
 */
public class Attach extends OrmDto {

    @SerializedName("dataId")
    public String dataId;

    @SerializedName("title")
    public String title;

    @SerializedName("content")
    public String info;

    @SerializedName("uri")
    public String uri;

    @SerializedName("time")
    public Long time;

}
