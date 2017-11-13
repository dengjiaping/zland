package com.zhisland.android.blog.common.view.dlg;

import android.content.Context;
import android.view.View;

import com.zhisland.android.blog.aa.controller.ActGuide;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.view.dialog.CommonDlgListener;
import com.zhisland.lib.view.dialog.IConfirmDlgMgr;

import java.util.HashMap;

import retrofit.http.POST;

/**
 * 用来帮助一个component管理所有的确认对话框
 * Created by arthur on 2016/9/7.
 */
public class ConfirmDlgMgr implements IConfirmDlgMgr {

    private Context context;

    HashMap<String, CommonDialog> dlgMap = new HashMap<>();
    HashMap<String, Object> argMap = new HashMap<>();
    private CommonDlgListener listener;//对话框点击事件的监听

    public ConfirmDlgMgr(Context context) {
        this.context = context;
    }

    /**
     * 设置对话框的监听按钮
     *
     * @param listener
     */
    @Override
    public void setListener(CommonDlgListener listener) {
        this.listener = listener;
    }

    /**
     * 展示某个确认对话框
     *
     * @param tag        都画框的表示，每个manager实力中唯一
     * @param title      对话框标题
     * @param okText     ok按钮的文案
     * @param cancelText no按钮文案
     */
    @Override
    public void show(final String tag, String title, String okText, String cancelText, final Object arg) {
        CommonDialog commonDialog;
        if (!dlgMap.containsKey(tag)) {
            commonDialog = new CommonDialog(context);
            commonDialog.show();
            commonDialog.setTitle(title);
            commonDialog.setLeftBtnContent(cancelText);
            commonDialog.setRightBtnContent(okText);
            commonDialog.setTag(tag);
            commonDialog.setListener(listener);
            dlgMap.put(tag, commonDialog);
        } else {
            commonDialog = dlgMap.get(tag);
        }

        if (commonDialog.isShowing())
            return;

        commonDialog.setArg(arg);
        commonDialog.show();

    }

    /**
     * 隐藏指定的对话框
     *
     * @param tag
     */
    @Override
    public void hide(String tag) {
        if (dlgMap.containsKey(tag)) {
            CommonDialog dlg = dlgMap.get(tag);
            if (dlg.isShowing()) {
                dlg.dismiss();
            }
        }
    }


}
