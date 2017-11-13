package com.zhisland.android.blog.event.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.lib.OrmDto;
import com.zhisland.lib.component.adapter.ZHPageData;

/**
 * 活动列表结构
 */
public class EventListStruct extends OrmDto {

    private static final long serialVersionUID = 1L;

    private static final int HAS_PAST_EVENT = 1;

    // 当前活动列表
    @SerializedName("current")
    public ZHPageData<Event> current;

    // 往期活动列表
    @SerializedName("past")
    public ZHPageData<Event> past;

    // 往期活动列表状态 0 无往期 1有往期
    @SerializedName("pastStatus")
    private Integer pastStatus;

    /**
     * 是否有current活动
     */
    public boolean isHasCurrentEvent(){
        if (current != null && current.data != null && current.data.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否有往期活动
     */
    public boolean isHasPastEvent() {
        if (past != null && past.data != null && past.data.size() > 0) {
            return true;
        }
        if (pastStatus != null && pastStatus == HAS_PAST_EVENT) {
            return true;
        } else {
            return false;
        }
    }

}
