package com.zhisland.android.blog.common.retrofit;

import com.zhisland.android.blog.common.app.Config;
import com.zhisland.lib.component.application.EnvType;
import com.zhisland.lib.retrofit.RetrofitFactoryBase;

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
