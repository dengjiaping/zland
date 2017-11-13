package com.zhisland.lib.component.lifeprovider;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * android Activity或者Fragment的生命周期监听
 * Created by 向飞 on 2016/5/21.
 */
public class PresenterLifeProvider implements IPresenterLifecycleProvider {

    private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();

    /**
     * 发射信号
     */
    public void onNext(PresenterEvent event) {
        lifecycleSubject.onNext(event);
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<PresenterEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

}
