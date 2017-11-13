package com.zhisland.android.blog.tabhome.model.impl;

import com.zhisland.android.blog.aa.controller.ResultCodeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.freshtask.bean.TaskCardList;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.tabhome.bean.FreshTaskAndComment;
import com.zhisland.android.blog.tabhome.model.IHomeInterceptModel;

import java.util.List;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 首页拦截model
 * Created by 向飞 on 2016/5/28.
 */
public class HomeInterceptModel implements IHomeInterceptModel {

    private static final String PREF_TASK_SHOW_TIME = "pref_task_show_time";//
    private static final String PREF_PRIZE_SHOW_TIME = "pref_prize_show_time";

    private FreshTaskApi freshApi;

    public HomeInterceptModel() {
        freshApi = RetrofitFactory.getInstance().getApi(FreshTaskApi.class);
    }

    @Override
    public Observable<FreshTaskAndComment> checkFreshTaskAndRecommendComment() {
        AppCall<FreshTaskAndComment> appCall = new AppCall<FreshTaskAndComment>() {
            @Override
            protected Response<FreshTaskAndComment> doRemoteCall() throws Exception {
                Call<FreshTaskAndComment> call = freshApi.checkFreshTaskAndRecommendComment();
                return call.execute();
            }

            @Override
            protected void handlerError(int code, String message) {
                if (code != CodeUtil.CODE_NO_PERMISSION) {
                    super.handlerError(code, message);
                }
            }
        };
        appCall.setIsBackgroundTask();
        return Observable.create(appCall);
    }

    /**
     * 检查好友任务状态
     *
     * @return
     */
    @Override
    public Observable<TaskCardList> checkFreshTask() {
        return Observable.create(new AppCall<TaskCardList>() {
            @Override
            protected Response<TaskCardList> doRemoteCall() throws Exception {
                Call<TaskCardList> call = freshApi.getFreshTask();
                return call.execute();
            }
        });

    }

    @Override
    public long getLastTaskShowTime() {
        long userId = PrefUtil.Instance().getUserId();
        return PrefUtil.Instance().getByKey(PREF_TASK_SHOW_TIME + userId, (long) 0);
    }

    @Override
    public void saveTaskShowTime(long time) {
        long userId = PrefUtil.Instance().getUserId();
        PrefUtil.Instance().setKeyValue(PREF_TASK_SHOW_TIME + userId, time);
    }

    @Override
    public long getLastPrizeShowTime() {
        long userId = PrefUtil.Instance().getUserId();
        return PrefUtil.Instance().getByKey(PREF_PRIZE_SHOW_TIME + userId, (long) 0);
    }

    @Override
    public void savePrizeShowTime(long time) {
        long userId = PrefUtil.Instance().getUserId();
        PrefUtil.Instance().setKeyValue(PREF_PRIZE_SHOW_TIME + userId, time);
    }


}
