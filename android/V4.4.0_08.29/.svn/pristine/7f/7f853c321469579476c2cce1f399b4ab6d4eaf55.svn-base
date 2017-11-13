package com.zhisland.lib.component.act;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;
import com.zhisland.lib.async.MyHandler;
import com.zhisland.lib.async.MyHandler.HandlerListener;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.async.http.task.TaskManager;
import com.zhisland.lib.authority.AuthorityMgr;
import com.zhisland.lib.bitmap.AsyncBitmapManager;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.component.lifeprovider.ActivityLifeProvider;
import com.zhisland.lib.mvp.view.IMvpView;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.dialog.AProgressDialog;
import com.zhisland.lib.view.dialog.CommonDlgListener;
import com.zhisland.lib.view.dialog.IConfirmDlgMgr;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public abstract class BaseFragmentActivity extends Activity implements
        HandlerListener, SwipeBackActivityBase, IMvpView, CommonDlgListener {

    protected String logTag;
    protected MyHandler handler = new MyHandler(this);
    protected LayoutInflater inflater;
    private boolean isStopped = false;

    private SwipeBackActivityHelper mHelper;

    // 是否开启onSaveInstanceState 功能，默认为关闭
    private boolean saveInstanceEnable = false;
    protected ActivityLifeProvider lifeProvider = new ActivityLifeProvider();
    private AProgressDialog progressDialog;
    private IConfirmDlgMgr confirmDlgMgr;

    /**
     * 如果不符合启动条件，返回false，activity会自动关闭，否则返回true
     *
     * @param savedInstanceState
     * @return
     */
    public boolean shouldContinueCreate(Bundle savedInstanceState) {
        return true;
    }

    /**
     * call super and configure window title feature
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        this.logTag = this.getClass().getSimpleName();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        if (shouldContinueCreate(savedInstanceState)) {
            onContinueCreate(savedInstanceState);
        } else {
            finish();
        }
    }

    /**
     * 如果{@link BaseTabFragPageActivity#shouldContinueCreate(Bundle)}
     * 返回true，则执行本函数，否则将跳过此函数
     *
     * @param savedInstanceState
     */
    @CallSuper
    public void onContinueCreate(Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getApplicationContext());
        lifeProvider.onNext(ActivityEvent.CREATE);
        confirmDlgMgr = ((ZHApplication) getApplication()).createConfirmDlgMgr(this);
        confirmDlgMgr.setListener(this);
    }

    //======swipeback=======
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * TITLE_NONE, TITLE_CUS, TITLE_LAYOUT
     */
    protected abstract int titleType();

    // =============================================
    // life cycle events

    @Override
    protected void onStart() {
        super.onStart();
        lifeProvider.onNext(ActivityEvent.START);
        isStopped = false;
        ((ZHApplication) getApplication()).startedActivityAdd();

    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeProvider.onNext(ActivityEvent.RESUME);
        ZHApplication.setCurrentActivity(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        lifeProvider.onNext(ActivityEvent.PAUSE);
        ZHApplication.setCurrentActivity(null);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifeProvider.onNext(ActivityEvent.STOP);
        isStopped = true;
        super.onStop();
        ((ZHApplication) getApplication()).startedActivityCut();
    }

    @Override
    protected void onDestroy() {
        MLog.i("ZHApp", "destroy: " + getClass().getSimpleName());
        lifeProvider.onNext(ActivityEvent.DESTROY);
        cancelTask(this);
        super.onDestroy();
    }

    /**
     * 是否开启onSaveInstanceState 功能，默认为关闭
     *
     * @param state true ：开启， false ：关闭
     */
    public void setSaveInstanceEnable(boolean state) {
        this.saveInstanceEnable = state;
    }

    /**
     * Activity 不保存任何状态，当Activity被回收后，重启时全部重新加载
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (saveInstanceEnable) {
            super.onSaveInstanceState(outState);
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    /**
     * 取消和这个activity所有相关的网络、异步请求
     *
     * @param activity
     */
    public static void cancelTask(Activity activity) {
        AsyncBitmapManager.cancelTask(activity.getBaseContext());
        AsyncBitmapManager.cancelTask(activity);

        ThreadManager.instance().cancelTask(activity, true);
        ThreadManager.instance().cancelTask(activity.getBaseContext(), true);

        TaskManager.cancelTask(activity);
        TaskManager.cancelTask(activity.getBaseContext());
    }

    // ==================================
    // ========abstract and interface methods==========

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    //===============rxjava==============

    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return lifeProvider.bindUntilEvent(event);
    }

    //========== dialog start ============

    @Override
    public void showProgressDlg() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(this);
        }
        progressDialog.show();
    }

    @Override
    public void showProgressDlg(String content) {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(this);
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
            progressDialog = new AProgressDialog(this);
        }
        return progressDialog;
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.showShort(toast);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        //是否需要权限拦截
        if (AuthorityMgr.Instance().handleAuthority(this, intent)) {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void gotoUri(String uri) {
        ((ZHApplication) getApplication()).getUriMgr().viewRes(this, uri);
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
        finish();
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public void onOkClicked(String tag, Object arg) {

    }

    @Override
    public void onNoClicked(String tag, Object arg) {

    }

    //========== dialog end ============
}
