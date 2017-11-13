package com.zhisland.android.blog.feed.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

/**
 * Created by arthur on 2016/9/9.
 */
public class RecommendUser extends OrmDto {

    @GsonExclude
    public EbAction action;

    @SerializedName("user")
    public User user;

    @SerializedName("ignoreBtn")
    public CustomState ignoreBtn;

    @SerializedName("attentionBtn")
    public CustomState attentionBtn;
}
