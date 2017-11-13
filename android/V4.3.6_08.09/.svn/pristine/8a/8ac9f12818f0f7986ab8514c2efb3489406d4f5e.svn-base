package com.zhisland.android.blog.info.presenter;

import android.webkit.URLUtil;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.contacts.eb.EBContacts;
import com.zhisland.android.blog.info.model.impl.InfoHomeTabModel;
import com.zhisland.android.blog.info.view.IInfoHomeTab;
import com.zhisland.android.blog.info.view.impl.FragLinkEdit;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.util.StringUtil;

import org.apache.http.client.HttpResponseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public class InfoHomeTabPresenter extends BasePresenter<InfoHomeTabModel, IInfoHomeTab> {

    public static final String KEY_INVITATION = "KEY_INVITATION"
            + PrefUtil.Instance().getUserId();

    private Subscription hideDelayOb;

    @Override
    protected void updateView() {
        super.updateView();
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
    }

    /**
     * 页面由不可见到可见
     */
    public void onFocused() {
        checkClip();
    }

    public void onFreshTaskClick() {
        //跳转任务列表
        view().gotoFreshTask();
    }

    public void onAddLinkClick() {
        if (PermissionsMgr.getInstance().canInfoRecommend()) {
            view().gotoAddLink();
        } else {
            DialogUtil.showPermissionsDialog(view().getViewContext(), "");
        }
    }

    /**
     * 检查剪切板，是否有http的url。有则弹出提示
     */
    private void checkClip() {
        String clipText = view().getClipText();
        if (StringUtil.isNullOrEmpty(clipText) || !URLUtil.isNetworkUrl(clipText.trim())) {
            return;
        }
        String lastText = PrefUtil.Instance().getByKey(FragLinkEdit.KEY_LAST_CLIP, "");
        if (!clipText.equals(lastText)) {
            view().showPrompt(clipText);
            hidePromptDelay(2000);
            PrefUtil.Instance().setKeyValue(FragLinkEdit.KEY_LAST_CLIP, clipText);
        }
    }

    /**
     * 延迟delay毫秒隐藏提示。
     */
    private void hidePromptDelay(long delay) {
        if (hideDelayOb != null && (!hideDelayOb.isUnsubscribed())) {
            hideDelayOb.unsubscribe();
        }
        hideDelayOb = Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        view().hidePrompt();
                        hideDelayOb = null;
                    }
                });
    }

}
