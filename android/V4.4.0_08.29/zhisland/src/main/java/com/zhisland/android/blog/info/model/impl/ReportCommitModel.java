package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 举报model
 * Created by Mr.Tui on 2016/7/6.
 */
public class ReportCommitModel implements IMvpModel {

    private NewsApi api;

    public ReportCommitModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    public Observable<Void> reportInfo(final long newsId, final String reasonCode, final String reasonDesc) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.reportInfo(newsId, reasonCode, reasonDesc);
                return call.execute();
            }
        });
    }

}