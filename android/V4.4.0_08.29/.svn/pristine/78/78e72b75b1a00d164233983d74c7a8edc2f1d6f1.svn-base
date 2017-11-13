package com.zhisland.android.blog.common.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;

/**
 * 自定义状态类
 */
public class CustomState extends OrmDto {

    //不可操作
    public static final int CAN_NOT_OPERABLE = 0;
    //可操作
    public static final int CAN_OPERABLE = 1;

    // 状态值
    @SerializedName("state")
    private Integer state;

    // 状态文本
    @SerializedName("stateName")
    private String stateName;

    // 是否可操作 1:可操作
    @SerializedName("isOperable")
    private Integer isOperable;

    /**
     * 返回状态码
     */
    public int getState() {
        if (state != null) {
            return state;
        } else {
            return -1;
        }
    }

    public void setStateName(String stateName){
        this.stateName = stateName;
    }

    public void setIsOperable(int isOperable){
        this.isOperable = isOperable;
    }

    /**
     * 状态文本
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * 是否是可操作状态
     */
    public boolean isOperable() {
        if (isOperable != null && isOperable == CAN_OPERABLE) {
            return true;
        } else {
            return false;
        }
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
