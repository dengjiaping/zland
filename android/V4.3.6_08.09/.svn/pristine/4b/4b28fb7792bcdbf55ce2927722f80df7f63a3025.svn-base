package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

import java.util.ArrayList;

/**
 * 活动当前状态类
 */
public class CurrentState extends OrmDto {

    private static final long serialVersionUID = 1L;

    public static final int STATE_SIGN_UP = 10;
    public static final int STATE_SIGN_AUDITE = 20;
    public static final int STATE_SIGN_OK = 30;
    public static final int STATE_EVENT_GOING = 40;
    public static final int STATE_WAIT_PAY = 50;
    public static final int STATE_EVENT_OVER = 60;
    public static final int STATE_EVENT_CANCEL = 70;
    public static final int STATE_MANAGER = 900;

    public static final int IS_OPERABLE_YES = 1;


    /**
     * 状态值
     */
    @SerializedName("state")
    public Integer state;

    /**
     * 状态文本
     */
    @SerializedName("stateName")
    public String stateName;

    /**
     * 是否可操作
     */
    @SerializedName("isOperable")
    public Integer isOperable;

    /**
     *  跳转 uri
     */
    @SerializedName("uri")
    public String uri;

}
