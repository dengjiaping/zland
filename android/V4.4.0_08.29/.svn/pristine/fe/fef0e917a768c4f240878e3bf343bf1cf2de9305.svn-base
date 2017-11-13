package com.zhisland.android.blog.freshtask.model.impl;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.freshtask.model.remote.FreshTaskApi;
import com.zhisland.lib.mvp.model.IMvpModel;

import retrofit.Call;
import retrofit.Response;
import rx.Observable;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class IntroductionModel implements IMvpModel {

    private FreshTaskApi api;

    public IntroductionModel() {
        api = RetrofitFactory.getInstance().getHttpsApi(FreshTaskApi.class);
    }

    /**
     * 更新用户个人简介
     */
    public Observable<Void> updateUserIntroduction(final String introduction) {
        return Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = api.updateUserIntroduction(introduction);
                return call.execute();
            }
        });
    }

    /**
     * 保存用户个人简介 to db
     */
    public void cachIntroduction(String introduction) {
        final User tmpUser = new User();
        tmpUser.uid = PrefUtil.Instance().getUserId();
        tmpUser.introduce = introduction;
        DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(tmpUser);
    }
}
