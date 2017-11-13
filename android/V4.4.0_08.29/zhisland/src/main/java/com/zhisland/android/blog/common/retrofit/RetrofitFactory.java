package com.zhisland.android.blog.common.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.retrofit.gson.CommentGsonAdapter;
import com.zhisland.android.blog.common.retrofit.gson.FeedGsonAdapter;
import com.zhisland.android.blog.common.retrofit.gson.GsonCreater;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.async.http.task.DateDeserializer;
import com.zhisland.lib.async.http.task.GsonExclusionStrategy;
import com.zhisland.lib.component.application.EnvType;
import com.zhisland.lib.retrofit.RetrofitFactoryBase;

import java.util.Date;

public class RetrofitFactory {

    RetrofitFactoryBase retrofitFactoryBase;

    private static class RetrofitHolder {
        // 静态初始化器，由JVM来保证线程安全
        private static RetrofitFactory INSTANCE = new RetrofitFactory();
    }

    public static RetrofitFactory getInstance() {
        return RetrofitHolder.INSTANCE;
    }

    private RetrofitFactory() {
        retrofitFactoryBase = RetrofitFactoryBase.getInstance();


        Gson gson = GsonCreater.CreateGson();
        retrofitFactoryBase.setGson(gson);

        // 添加 header interceptor
        retrofitFactoryBase.addHeaderInterceptor(new HeaderInterceptor());
        // 非release环境下需要忽略CA，否则 https 请求失败
        if (Config.ENV_TYPE != EnvType.ENV_RELEASE) {
            retrofitFactoryBase.ignoreCA();
        }
    }

    /**
     * 创建API代理
     */
    public <T> T getApi(Class<T> cls) {
        return retrofitFactoryBase.getApiService(Config.getRetrofitBaseUrl(), cls);
    }

    /**
     * 创建API代理,带baseUrl
     */
    public <T> T getApi(String baseUrl, Class<T> cls) {
        return retrofitFactoryBase.getApiService(baseUrl, cls);
    }

    /**
     * 创建https API代理
     */
    public <T> T getHttpsApi(Class<T> cls) {
        return retrofitFactoryBase.getApiService(Config.getRetrofitHttpsBaseUrl(), cls);
    }
}
