package com.zhisland.lib.retrofit;

import com.squareup.okhttp.Headers;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.ToastUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLDecoder;

import retrofit.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * 正和岛APPbase callBack
 */
public abstract class AppCallBase<T> implements Observable.OnSubscribe<T> {

    // 是否为后台task
    private boolean isBackgroundTask = false;

    @Override
    public void call(Subscriber<? super T> subscriber) {

        Response<T> response = null;
        Throwable error = null;
        try {
            response = doRemoteCall();
        } catch (Exception e) {
            error = e;
            MLog.d("asd", "remote error" + e.getLocalizedMessage());
        }

        try {
            handlerHeaders(response.headers());
        } catch (Exception e) {

        }

        //TODO 处理公共异常
        if (response != null && response.isSuccess()) {
            subscriber.onNext(response.body());
            subscriber.onCompleted();
        } else {
            if (response != null) {
                int code = response.code();
                String errorBody = null;
                try {
                    errorBody = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ApiError apiError = new ApiError();
                apiError.code = code;
                apiError.body = errorBody;
                Headers headers = response.headers();
                if (headers != null && headers.size() > 0) {
                    String message = headers.get("msg");
                    if (message != null) {
                        try {
                            apiError.message = URLDecoder.decode(message, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

                handlerError(code, apiError.message);
                subscriber.onError(apiError);
            } else {
                if (!isBackgroundTask) {
                    if (error instanceof ConnectException) {
                        ZHApplication.ShowToastFromBackground("无网络连接，请稍后重试");
                    } else {
                        ZHApplication.ShowToastFromBackground("连接超时，请稍后重试");
                    }
                }
                subscriber.onError(error);
            }

        }

    }

    /**
     * 实际的请求操作，同步
     */
    protected abstract Response<T> doRemoteCall() throws Exception;

    /**
     * 处理错误异常
     */
    protected void handlerError(int code, String message) {
    }

    /**
     * 处理错误异常
     */
    protected void handlerHeaders(Headers headers) {
    }

    /**
     * 设置是否为后台task, 如果是后台task，则当接口访问失败后，不弹失败toast
     */
    public void setIsBackgroundTask() {
        isBackgroundTask = true;
    }
}