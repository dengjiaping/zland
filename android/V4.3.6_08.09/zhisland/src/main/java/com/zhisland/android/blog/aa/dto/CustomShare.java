package com.zhisland.android.blog.aa.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.util.StringUtil;

/**
 * 分享对象
 */
public class CustomShare extends OrmDto {


    private static final long serialVersionUID = 1L;

    @SerializedName("title")
    public String title;

    @SerializedName("desc")
    public String desc;

    @SerializedName("url")
    public String url;

    @SerializedName("img")
    public String img;

}
