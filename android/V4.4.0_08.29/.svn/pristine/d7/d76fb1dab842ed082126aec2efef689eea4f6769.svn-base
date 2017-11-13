package com.zhisland.lib.mvp.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;

import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.mvp.presenter.PresenterManager;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * mvp开发基类，hold一个presenter实例，并负责presenter的保存和销毁，以及presenter对view引用的管理
 *
 * @param <P>
 */
@Deprecated
public abstract class FragBaseMvp<P extends BasePresenter> extends FragBaseMvps {

    private static final String P_DEFAULT = "presenter_default";


    @Override
    protected final Map<String, BasePresenter> createPresenters() {
        Map<String, BasePresenter> presenters = new HashMap<>(1);
        P presenter = this.createPresenter();
        presenters.put(P_DEFAULT, presenter);
        return presenters;
    }

    /**
     * 创建主导器
     *
     * @return
     */
    protected abstract P createPresenter();

    /**
     * 获取当前主导器
     *
     * @return
     */
    protected P presenter() {
        return presenter(P_DEFAULT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter(), outState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    @Override
    @CallSuper
    public void onPause() {
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
        super.onDetach();
    }

}
