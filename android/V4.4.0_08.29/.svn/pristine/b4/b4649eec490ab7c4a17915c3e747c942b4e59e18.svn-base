package com.zhisland.android.blog.im.dto;

import com.google.gson.annotations.SerializedName;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.lib.OrmDto;

/**
 * 消息未读取数量
 */
public class MessageCount extends OrmDto {

    /**
     * 新增粉丝数
     */
    @SerializedName("fans")
    private Long fans;

    /**
     * 互动消息
     */
    @SerializedName("interactive")
    private Long interactive;

    /**
     * 系统消息
     */
    @SerializedName("system")
    private Long system;

    /**
     * 获取新增粉丝数
     */
    public long getFansCount() {
        if (fans != null) {
            return fans;
        } else {
            return 0;
        }
    }

    /**
     * 获取互动消息
     */
    public long getInteractiveCount() {
        if (interactive != null) {
            return interactive;
        } else {
            return 0;
        }
    }

    /**
     * 获取系统消息
     */
    public long getSystemCount() {
        if (system != null) {
            return system;
        } else {
            return 0;
        }
    }

    /**
     * 消息数是否变化
     */
    public boolean isMsgCountChange() {
        long oldFansCount = PrefUtil.Instance().getNewlyAddedFansCount();
        long oldInteractiveCount = PrefUtil.Instance().getInteractiveCount();
        long oldSystemMsgCount = PrefUtil.Instance().getSystemMsgCount();

        if (getFansCount() > oldFansCount) {
            return true;
        } else if (getInteractiveCount() > oldInteractiveCount) {
            return true;
        } else if (getSystemCount() > oldSystemMsgCount) {
            return true;
        } else {
            return false;
        }
    }
}
