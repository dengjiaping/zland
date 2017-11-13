package com.zhisland.lib.component.lifeprovider;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;

/**
 * 主导器生命周期事件提供者
 * Created by 向飞 on 2016/6/2.
 */
public interface IPresenterLifecycleProvider {

    /**
     * 发布一个主导器生命周期事件
     *
     * @param event
     */
    void onNext(PresenterEvent event);

    /**
     * @return a sequence of {@link android.app.Activity} lifecycle events
     */
    @NonNull
    @CheckResult
    Observable<PresenterEvent> lifecycle();

    /**
     * 用于将一个RXJava的观察者回调绑定到主导器的一个事件上
     */
    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event);


}
