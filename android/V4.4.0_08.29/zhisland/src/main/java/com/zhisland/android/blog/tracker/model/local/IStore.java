package com.zhisland.android.blog.tracker.model.local;



import com.zhisland.android.blog.tracker.bean.TrackerEvent;

import java.util.List;

/**
 * 用于实现统计日志的存储
 * Created by 向飞 on 2016/5/24.
 */
public interface IStore {

    /**
     * 存储一个统计事件
     *
     * @param se
     */
    void log(TrackerEvent se);

    /**
     * 获取上传的事件
     *
     * @return
     */
    List<TrackerEvent> getUploadEvents(int maxUploads);

    /**
     * 删除上传成功的事件
     * @param ses
     */
    void finishUpload(long maxId);
}
