package com.zhisland.android.blog.find.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 筛选项
 * Created by Mr.Tui on 2016/5/20.
 */
public class FilterItem extends OrmDto {

    /**
     * 启用状态 1: 代表启用
     * */
    public static final int STATUS_ENABLED_TRUE = 1;

    /**
     * 激活状态 :未激活为0
     * */
    public static final int STATUS_ACTIVE_FALSE = 0;

    /**
     * 激活状态 :已激活为1
     * */
    public static final int STATUS_ACTIVE_TRUE = 1;

    /**
     * 筛选项名称
     */
    @SerializedName("code")
    public String code;

    /**
     * 筛选项名称
     */
    @SerializedName("name")
    public String name;

    /**
     * 启用状态（1 启用 0未启用，未启用时，页面选项置灰不可选）
     */
    @SerializedName("enabled")
    public int enabled;

    /**
     * 激活状态 (启用状态enabled为true时，此选项表示当前查询结果中，是否启用此筛选项，如果启用，则筛选页背景加重显示此项）
     */
    @SerializedName("active")
    public int active;
}
