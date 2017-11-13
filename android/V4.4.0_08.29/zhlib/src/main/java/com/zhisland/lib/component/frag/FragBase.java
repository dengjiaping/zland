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
import com.zhisland.lib.component.lifeprovider.FragmentLifeProvider;
import com.zhisland.lib.mvp.view.IMvpView;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.text.ZHImageSpan;
import com.zhisland.lib.view.dialog.AProgressDialog;
import com.zhisland.lib.view.dialog.CommonDlgListener;
import com.zhisland.lib.view.dialog.IConfirmDlgMgr;

public abstract class FragBase extends Fragment implements HandlerListener, IMvpView, CommonDlgListener {

    protected FragmentLifeProvider lifeProvider = new FragmentLifeProvider();
    protected MyHandler handler = new MyHandler(this);
    protected boolean isViewValid = false;// between view created and destroyed
    private AProgressDialog progressDialog;
    private IConfirmDlgMgr confirmDlgMgr;

    @Override
    @CallSuper
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        //创建通用的确认对话框管理器
        confirmDlgMgr = ((ZHApplication) getActivity().getApplication()).createConfirmDlgMgr(getActivity());
        if (confirmDlgMgr != null) {
            confirmDlgMgr.setListener(this);
        }
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

    public abstract String getPageName();

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

    //========== dialog start ============

    @Override
    public void showProgressDlg() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        progressDialog.show();
    }

    @Override
    public void showProgressDlg(String content) {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        progressDialog.show();
        progressDialog.setText(content);
    }

    @Override
    public void hideProgressDlg() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public AProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        return progressDialog;
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.showShort(toast);
    }


    @Override
    public void gotoUri(String uri) {
        ((ZHApplication) getActivity().getApplication()).getUriMgr().viewRes(getActivity(), uri);
    }

    @Override
    public void showConfirmDlg(String tag, String title, String okText, String noText, Object arg) {
        if (confirmDlgMgr != null) {
            confirmDlgMgr.show(tag, title, okText, noText, arg);
        }
    }

    @Override
    public void hideConfirmDlg(String tag) {
        if (confirmDlgMgr != null) {
            confirmDlgMgr.hide(tag);
        }
    }

    @Override
    public void finishSelf() {
        getActivity().finish();
    }

    @Override
    public void onOkClicked(String tag, Object arg) {
    }

    @Override
    public void onNoClicked(String tag, Object arg) {
    }
    //========== dialog end ============
}
