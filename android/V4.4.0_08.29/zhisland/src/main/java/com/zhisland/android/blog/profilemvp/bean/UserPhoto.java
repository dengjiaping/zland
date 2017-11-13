package com.zhisland.android.blog.profilemvp.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.lib.OrmDto;

/**
 * Created by arthur on 2016/9/10.
 */
public class UserPhoto extends OrmDto {

    @SerializedName("photoId")
    public long photoId;

    @SerializedName("url")
    public String url;

    @SerializedName("text")
    public String text;

}
