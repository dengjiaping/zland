package com.zhisland.android.blog.info.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.OrmDto;

/**
 * Created by Mr.Tui on 2016/7/1.
 */
public class ZHInfo extends OrmDto {

    public static final int VALUE_OFFICIAL_TAG_NULL = 0;
    public static final int VALUE_OFFICIAL_TAG_ORIGINAL = 1;
    public static final int VALUE_OFFICIAL_TAG_RECOMMEND = 2;

    public static final String dayFormat= "yyyy'-'MM'-'dd";

    // 资讯id
    @SerializedName("newsId")
    public long newsId;

    // 资讯标题
    @SerializedName("title")
    public String title;

    @SerializedName("countCollect")
    public CountCollect countCollect;

    // 资讯大图
    @SerializedName("coverLarge")
    public String coverLarge;

    // 资讯缩略图
    @SerializedName("coverSmall")
    public String coverSmall;

    // 推荐语/岛读
    @SerializedName("recommendText")
    public String recommendText;

    // 摘要
    @SerializedName("summary")
    public String summary;

    @SerializedName("shareUrl")
    public String infoShareUrl;

    @SerializedName("contentCollect")
    public ContentCollect contentCollect;

    //资讯推荐人
    @SerializedName("recommendUser")
    public User recommendUser;

    //官方标记（0 无 1 官方资讯  2 官方推荐
    @SerializedName("officialTag")
    public int officialTag;

    //正文原始URL路径
    @SerializedName("srcUrl")
    public String srcUrl;

    //资讯发布时间（用于列表显示）
    @SerializedName("displayTime")
    public String displayTime;

    public boolean isFirst;
    public boolean isLast;
    public String day;

}
