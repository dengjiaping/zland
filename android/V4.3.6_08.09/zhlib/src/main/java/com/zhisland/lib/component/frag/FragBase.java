package com.zhisland.lib.component.frag;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.zhisland.lib.async.MyHandler;
import com.zhisland.lib.async.MyHandler.HandlerListener;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.component.lifeprovider.FragmengLifeProvider;

public abstract class FragBase extends Fragment implements HandlerListener {

    protected FragmengLifeProvider lifeProvider = new FragmengLifeProvider();
    protected MyHandler handler = new MyHandler(this);
    protected boolean isViewValid = false;// between view created and destroyed

    @Override
    @CallSuper
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifeProvider.onNext(FragmentEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (reportUMOnCreateAndOnDestory()) {
            Object obj = getActivity().getApplication();
            if (obj instanceof ZHApplication) {
                ((ZHApplication) obj).fragOnCreate(getPageName());
            }
        }
        lifeProvider.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    @CallSuper
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isViewValid = true;
        lifeProvider.onNext(FragmentEvent.CREATE);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        //注入
        lifeProvider.onNext(FragmentEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifeProvider.onNext(FragmentEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        lifeProvider.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifeProvider.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        this.isViewValid = false;
        lifeProvider.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifeProvider.onNext(FragmentEvent.DESTROY);
        if (reportUMOnCreateAndOnDestory()) {
            Object obj = getActivity().getApplication();
            if (obj instanceof ZHApplication) {
                ((ZHApplication) obj).fragOnDestory(getPageName());
            }
        }
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        lifeProvider.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    protected abstract String getPageName();

    public boolean onBackPressed() {
        return false;
    }

    protected boolean reportUMOnCreateAndOnDestory() {
        return true;
    }

    //==========rx java============
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return lifeProvider.bindUntilEvent(event);
    }

}
