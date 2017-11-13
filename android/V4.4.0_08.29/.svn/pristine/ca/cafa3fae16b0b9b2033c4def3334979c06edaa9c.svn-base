package com.zhisland.android.blog.common.retrofit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.lib.component.application.EnvType;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

import java.io.IOException;

/**
 * Created by 向飞 on 2016/5/16.
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        if (StringUtil.isNullOrEmpty(PrefUtil.Instance().getToken())) {
            if (Config.ENV_TYPE == EnvType.ENV_TEST_INTERNET) {
                newRequest = request.newBuilder()
                        .addHeader("device_id", AppUtil.Instance().getDeviceId())
                        .addHeader("version", AppUtil.Instance().getVersionName())
                        .addHeader("os", "android")
                        .addHeader("osVersion", android.os.Build.VERSION.RELEASE)
                        .addHeader("channelId", "prerelease")
                        .build();
            } else {
                newRequest = request.newBuilder()
                        .addHeader("device_id", AppUtil.Instance().getDeviceId())
                        .addHeader("version", AppUtil.Instance().getVersionName())
                        .addHeader("os", "android")
                        .addHeader("osVersion", android.os.Build.VERSION.RELEASE)
                        .build();
            }

        } else {
            if (Config.ENV_TYPE == EnvType.ENV_TEST_INTERNET) {
                newRequest = request.newBuilder()
                        .addHeader("device_id", AppUtil.Instance().getDeviceId())
                        .addHeader("version", AppUtil.Instance().getVersionName())
                        .addHeader("os", "android")
                        .addHeader("osVersion", android.os.Build.VERSION.RELEASE)
                        .addHeader("uid", "" + PrefUtil.Instance().getUserId())
                        .addHeader("atk", PrefUtil.Instance().getToken())
                        .addHeader("channelId", "prerelease")
                        .build();
            } else {
                newRequest = request.newBuilder()
                        .addHeader("device_id", AppUtil.Instance().getDeviceId())
                        .addHeader("version", AppUtil.Instance().getVersionName())
                        .addHeader("os", "android")
                        .addHeader("osVersion", android.os.Build.VERSION.RELEASE)
                        .addHeader("uid", "" + PrefUtil.Instance().getUserId())
                        .addHeader("atk", PrefUtil.Instance().getToken())
                        .build();
            }
        }

        long t1 = System.nanoTime();
        MLog.d("asd", String.format("Sending %s request %s on %s%n%s", newRequest.method(),
                newRequest.url(), chain.connection(), newRequest.headers()));

        Response response = chain.proceed(newRequest);

        long t2 = System.nanoTime();
        MLog.d("asd", String.format("Received response \n" +
                        " %d \n for %s in %.1fms%n%s", response.code(),
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
