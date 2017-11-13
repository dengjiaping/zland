package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * Created by Mr.Tui on 2016/8/18.
 */
public class PriceTag extends OrmDto {

    public static final int TYPE_ALL = 1;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_VIP = 3;
    public static final int TYPE_HAIKE = 4;

    /**
     * 对应的价格类型。
     * 1  整体有优惠（与2、3、4互斥） 2、原价优惠标签 3、岛亲价优惠标签 4、海客价优惠标签
     */
    @SerializedName("type")
    public int type;

    /**
     * 标签文本
     */
    @SerializedName("tag")
    public String tag;

    /**
     * 标签描述
     */
    @SerializedName("content")
    public String content;
}
