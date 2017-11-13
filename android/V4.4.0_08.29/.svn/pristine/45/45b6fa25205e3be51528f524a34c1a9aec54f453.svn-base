package com.zhisland.android.blog.freshtask.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.List;

/**
 * 任务列表数据
 * Created by 向飞 on 2016/5/27.
 */
public class TaskCardList extends OrmDto {

    // 显示新手任务引导页
    public static final int SHOW_DISPLAY_GUIDE = 1;
    // 不弹拦截
    public static final int NOT_SHOW_POP_STATE = 0;

    @SerializedName("title")
    public String title; // 任务列表标题

    @SerializedName("desc")
    public String desc; // 任务列表描述

    @SerializedName("unfinishedCount")
    public Integer unfinishedCount;//未完成任务数量

    @SerializedName("display")
    public Integer display; //是否显示引导页，0：不显示，1：显示

    @SerializedName("guideTitle")
    public String guideTitle;//任务引导标题

    @SerializedName("cards")
    public List<TaskCard> cards; // 任务列表

    @SerializedName("popStatus")
    public Integer popStatus; //是否弹出拦截  0 不弹出   1 弹出 （5个任务卡片全部己读或己完成时，状态为0)
}