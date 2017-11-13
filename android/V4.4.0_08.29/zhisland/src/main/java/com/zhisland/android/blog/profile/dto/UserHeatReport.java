package com.zhisland.android.blog.profile.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * Created by Mr.Tui on 2016/9/12.
 */
public class UserHeatReport extends OrmDto {

    private static final long serialVersionUID = 1L;

    //缓存的key值，后面拼接上uid。
    public static final String CACHE_KEY = "CACHE_KEY_HEAT_REPORT_";

    @SerializedName("attentionCount")
    public int attentionCount ;//关注数量

    @SerializedName("fansCount")
    public int fansCount;//粉丝数量

    @SerializedName("friendCount ")
    public int friendCount ;//好友数量

    @SerializedName("heatVal ")
    public int heatVal ;//热度值

    @SerializedName("userRanking ")
    public int userRanking ;//用户排名

    @SerializedName("indexPageViews ")
    public int indexPageViews ;//首页访问量

    @SerializedName("receiveCommentCount ")
    public int receiveCommentCount ;//收到的神评论数量

    @SerializedName("receiveLikeCount ")
    public int receiveLikeCount ;//收到赞的数量
}
