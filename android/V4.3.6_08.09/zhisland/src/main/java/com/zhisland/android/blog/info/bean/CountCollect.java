package com.zhisland.android.blog.info.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * Created by Mr.Tui on 2016/7/5.
 */
public class CountCollect extends OrmDto {

    public static final int UP_DOWN_STATE_NULL = 0;
    public static final int UP_DOWN_STATE_UP = 1;
    public static final int UP_DOWN_STATE_DOWN = 2;

    public static final int LIKE_STATE_NULL = 0;
    public static final int LIKE_STATE_DONE = 1;


    // 资讯被顶总数
    @SerializedName("upCount")
    public int upCount;

    // 资讯被踩总数
    @SerializedName("downCount")
    public int downCount;

    // 资讯发布观点总数
    @SerializedName("viewpointCount")
    public int viewpointCount;

    // 用户顶踩状态 0 未操作 1 己顶  2 己踩
    @SerializedName("userUpDownState")
    public int userUpDownState;

    // 资讯已阅读总数
    @SerializedName("readedCount")
    public int readedCount;





    /**
     * 观点回复总数
     * */
    @SerializedName("replyCount")
    public int replyCount;

    // 观点被赞总数
    @SerializedName("likeCount")
    public int likeCount;

    // 用户点赞状态（0 未点赞 1 己点赞)
    @SerializedName("likedState")
    public int likedState;

}
