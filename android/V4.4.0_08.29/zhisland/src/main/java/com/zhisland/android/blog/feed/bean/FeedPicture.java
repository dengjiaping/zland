package com.zhisland.android.blog.feed.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

/**
 * feed图片
 * Created by arthur on 2016/9/1.
 */
public class FeedPicture extends OrmDto {

    @SerializedName("picId")
    public Long picId;

    //图片的URL
    @GsonExclude
    public String localPath;

    @SerializedName("url")
    public String url;

    @SerializedName("width")
    public Integer width;

    @SerializedName("height")
    public Integer height;
}
