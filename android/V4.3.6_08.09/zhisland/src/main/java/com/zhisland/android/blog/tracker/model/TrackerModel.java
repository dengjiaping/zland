package com.zhisland.android.blog.tracker.model;


import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.tracker.bean.TrackerEvent;
import com.zhisland.android.blog.tracker.model.remote.ITrackerApi;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.net.URLEncoder;
import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by 向飞 on 2016/5/25.
 */
public class TrackerModel implements IMvpModel {

//    private IStore store;
    private ITrackerApi api;

    public TrackerModel() {
//        store = DBMgr.getMgr().getTrackerDao();
        api = RetrofitFactory.getInstance().getApi("http://s.zhisland.com", ITrackerApi.class);
    }

    /**
     * 存储事件
     *
     * @param event
     */
    public void logEvent(TrackerEvent event) {
//        store.log(event);
        DBMgr.getMgr().getTrackerDao().log(event);

    }

    /**
     * 获取还未上传的统计事件
     *
     * @param maxUploads
     * @return
     */
    public List<TrackerEvent> getUploadEvents(int maxUploads) {
        return DBMgr.getMgr().getTrackerDao().getUploadEvents(maxUploads);
    }

    /**
     * 上传统计事件
     *
     * @param ses
     * @param time
     * @return
     */
    public Observable<Void> uploadEvent(final List<TrackerEvent> ses, final long time) {
        AppCall<Void> appCall = new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                String json = GsonHelper.GetCommonGson().toJson(ses);
                json = URLEncoder.encode(json, "utf-8");
                Call<Void> call = api.uploadPageEvent(json, time);
                return call.execute();
            }
        };
        appCall.setIsBackgroundTask();
        return Observable.create(appCall);
    }

    public void finishUpload(long maxId) {
        DBMgr.getMgr().getTrackerDao().finishUpload(maxId);
    }
}
