package com.zhisland.android.blog.feed.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.component.adapter.ZHPageData;

import java.util.List;

/**
 * 新鲜事列表数据结构
 * Created by arthur on 2016/8/31.
 */
public class TimelineData extends OrmDto{

    //运营推广位
    @SerializedName("spread")
    public RecommendData recommendData;

    //可能感兴趣的人
    @SerializedName("interestedPersons")
    public List<RecommendUser> intrestableUsers;

    @SerializedName("feeds")
    public ZHPageData<Feed> feeds;

    @SerializedName("message")
    public CountMessage count;
}
