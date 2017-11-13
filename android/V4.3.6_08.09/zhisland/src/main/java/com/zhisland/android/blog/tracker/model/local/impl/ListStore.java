package com.zhisland.android.blog.tracker.model.local.impl;



import com.zhisland.android.blog.tracker.bean.TrackerEvent;
import com.zhisland.android.blog.tracker.model.local.IStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个纯内存的存储
 * Created by 向飞 on 2016/5/24.
 */
public class ListStore implements IStore {

    private List<TrackerEvent> events;

    public ListStore() {
        events = new ArrayList<>();
    }

    @Override
    public void log(TrackerEvent se) {
        events.add(se);
    }

    @Override
    public List<TrackerEvent> getUploadEvents(int maxUploads) {
        return events;
    }

    @Override
    public void finishUpload(long maxId) {
        events.clear();
    }
}
