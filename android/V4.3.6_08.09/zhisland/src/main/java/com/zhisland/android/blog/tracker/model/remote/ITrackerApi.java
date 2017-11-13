package com.zhisland.android.blog.tracker.model.remote;


import com.zhisland.android.blog.tracker.bean.TrackerEvent;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by 向飞 on 2016/5/24.
 */
public interface ITrackerApi {

    /**
     * @param se   页面事件
     * @param time 上传时间
     * @return
     */

    @GET("/ad.gif")
    Call<Void> uploadPageEvent(@Query("data") String trackerJson, @Query("time") long time);

}
