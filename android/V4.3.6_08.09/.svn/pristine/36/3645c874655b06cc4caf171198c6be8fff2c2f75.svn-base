package com.zhisland.lib.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.mvp.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * mvp开发基类，hold一组presenter实例，并负责presenter的保存和销毁，以及presenter对view引用的管理
 */
public abstract class FragBaseMvps extends FragBase {

    private Map<String, BasePresenter> presenters;


    /**
     * 获取主导器
     *
     * @param tag
     * @param <T>
     * @return
     */
    protected <T> T presenter(String tag) {
        Object obj = presenters.get(tag);
        return (T) obj;
    }


    @Nullable
    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenters = this.createPresenters();
        if (presenters != null && savedInstanceState != null) {
            for (BasePresenter p : presenters.values()) {
                p.restoreState(savedInstanceState);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 创建主导器,以及通过Android上下文的参数对主导器进行初始化
     *
     * @return
     */
    protected abstract Map<String, BasePresenter> createPresenters();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        for (BasePresenter p : presenters.values()) {
            p.saveState(outState);
        }
        //TODO
//        PresenterManager.getInstance().savePresenter(presenters, outState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (BasePresenter presenter : presenters.values()) {
            presenter.setSchedulerSubscribe(Schedulers.io());
            presenter.setSchedulerObserver(AndroidSchedulers.mainThread());

            //注入
            presenter.bindView((IMvpView) this);
        }

    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        for (BasePresenter presenter : presenters.values()) {
            presenter.onVisible();
        }
    }

    @Override
    @CallSuper
    public void onPause() {
        for (BasePresenter presenter : presenters.values()) {
            presenter.onInvisible();
        }
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        for (BasePresenter presenter : presenters.values()) {
            presenter.unbindView();
        }
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        presenters.clear();
        super.onDetach();
    }

}
