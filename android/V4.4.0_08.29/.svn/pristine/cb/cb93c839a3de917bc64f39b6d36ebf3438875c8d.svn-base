package com.zhisland.android.blog.freshtask.bean;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.dto.CustomState;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.async.http.task.GsonExclude;

/**
 * Created by 向飞 on 2016/5/26.
 */
public class TaskCard extends OrmDto {
    /**
     * 概要
     */
    @SerializedName("summary")
    public String summary;

    /**
     * 类型
     */
    @SerializedName("type")
    public Integer type;

    /**
     * 标题
     */
    @SerializedName("title")
    public String title;

    /**
     * 描述
     */
    @SerializedName("desc")
    public String desc;

    /**
     * 卡片的黑色大标题
     */
    @SerializedName("tips")
    public String tips;

    /**
     * 自定义状态
     */
    @SerializedName("state")
    public CustomState state;

    /**
     * 卡片使用的资源
     */
    @GsonExclude
    public int iconCardRes;

    /**
     * list使用的资源
     */
    @GsonExclude
    public int iconListRes;


    /**
     * 形象照照片
     */
    @GsonExclude
    public String figureUrl;


}
