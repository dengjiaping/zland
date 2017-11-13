package com.zhisland.android.blog.info.model.impl;

import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.info.bean.RInfoPageData;
import com.zhisland.android.blog.info.bean.SectionInfo;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.remote.NewsApi;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * 精选资讯列表model
 * Created by Mr.Tui on 2016/6/29.
 */
public class FeaturedInfoModel implements IMvpModel {

    private NewsApi api;

    public FeaturedInfoModel() {
        api = RetrofitFactory.getInstance().getApi(NewsApi.class);
    }

    /**
     * 获取资讯推荐列表
     */
    public Observable<ZHPageData<SectionInfo>> getFeaturedList(final String nextId) {
        return Observable.create(new AppCall<ZHPageData<SectionInfo>>() {
            @Override
            protected Response<ZHPageData<SectionInfo>> doRemoteCall() throws Exception {
                Call<ZHPageData<SectionInfo>> call = api.getFeaturedInfo(2, nextId);
                return call.execute();
            }
        });
    }
}
