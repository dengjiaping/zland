package com.zhisland.android.blog.common.upapp;

import org.apache.http.client.HttpResponseException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.setting.eb.EBSetting;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.act.BaseFragmentActivity;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.dialog.AProgressDialog;

import de.greenrobot.event.EventBus;

public class UpgradeMgr {

    private final Context context;
    private Dialog dlg;

    public UpgradeMgr(Context context) {
        this.context = context;
    }

    public void checkUpgrade(final boolean isFromSplash) {
        if (!isFromSplash) {
            dlg = this.createUpdateDialog();
            dlg.show();
        }

        ZHApis.getAAApi().update(context, isFromSplash,  new TaskCallback<ZHUpgrade>() {

            @Override
            public void onSuccess(ZHUpgrade content) {
                ActUpdateDialog.invoke(context, content, isFromSplash);
                PrefUtil.Instance().setLatestVersion(false);
                EventBus.getDefault()
                        .post(new EBSetting(
                                EBSetting.TYPE_REFRESH_VERSION_ICON, null));
            }

            @Override
            public void onFailure(Throwable error) {
                if (!isFromSplash && error instanceof HttpResponseException) {
                    HttpResponseException e = (HttpResponseException) error;
                    int statusCode = e.getStatusCode();
                    String message = error.getMessage();
                    if (StringUtil.isNullOrEmpty(message)) {
                        message = "目前版本已最新,暂无新版本";
                    }
                    switch (statusCode) {
                        case CodeUtil.CODE_NO_NEW_VERSION:
                        case CodeUtil.CODE_NO_VERSION_INFO:
                            ToastUtil.showShort(message);
                            PrefUtil.Instance().setLatestVersion(true);
                            EventBus.getDefault()
                                    .post(new EBSetting(
                                            EBSetting.TYPE_REFRESH_VERSION_ICON,
                                            null));
                            break;
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dlg != null && dlg.isShowing()) {
                    dlg.dismiss();
                }
            }
        });
    }

    private Dialog createUpdateDialog() {
        String nottext = "检查更新中...";

        AProgressDialog dialog = DialogUtil.createProgressDialog(context);
        dialog.setText(nottext);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                if (context instanceof Activity){
                    BaseFragmentActivity.cancelTask((Activity) context);
                }
            }
        });
        return dialog;
    }
}
