package com.zhisland.lib.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.lib.R;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.pulltorefresh.PullRefeshListener;
import com.zhisland.lib.view.pulltorefresh.PullToRefreshProxy;

/**
 * Created by Mr.Tui on 2016/6/30.
 */
@Deprecated
public abstract class FragBasePullMvp<V extends View, P extends BasePresenter> extends FragBaseMvp<P> implements
        PullRefeshListener {

    private static final String TAG = "fragment";

    protected PullToRefreshProxy<V> pullProxy;

    // pullview的快捷方式，等同于pullProxy.getPullView()
    protected PullToRefreshBase<V> pullView;
    protected V internalView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pullProxy = createProxy();
        MLog.d(TAG, this.getClass().getSimpleName() + " onattach");
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = createDefaultFragView();
        view.setLayoutParams(layout);

        pullView = (PullToRefreshBase<V>) view.findViewById(
                R.id.pullRefreshAbsListView);
        internalView = pullView.getRefreshableView();
        pullProxy.setPullView(pullView);
        pullProxy.setPullListener(this);
        pullProxy.onCreate();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.d(TAG, this.getClass().getSimpleName() + " onCreate");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        pullProxy.onResume();
        MLog.d(TAG, this.getClass().getSimpleName() + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        MLog.d(TAG, this.getClass().getSimpleName() + " onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pullProxy.onDestroy();
        MLog.d(TAG, this.getClass().getSimpleName() + " onDestroyView");
    }

    @Override
    public void onDestroy() {
        if (pullProxy != null) {
            pullProxy.onDestroy();
        }
        super.onDestroy();
        MLog.d(TAG, this.getClass().getSimpleName() + " onDestroy");

    }

    ;

    @Override
    public void onDetach() {
        super.onDetach();
        MLog.d(TAG, this.getClass().getSimpleName() + " onDetach");
    }

    // ===============================================
    // abstract method or methods children should override
    // ================================================

    /**
     * 每个landing类可以通过此方法配置landing的页面布局
     *
     * @return
     */
    protected abstract View createDefaultFragView();

    /**
     * 为fragment页面创建刷新proxy，子类可以重在这个方法实现定制化
     *
     * @return
     */
    protected PullToRefreshProxy<V> createProxy() {
        return new PullToRefreshProxy<V>();
    }

    /**
     * 获取刷新的proxy
     *
     * @return
     */
    public PullToRefreshProxy<V> getPullProxy() {
        return pullProxy;
    }

}
