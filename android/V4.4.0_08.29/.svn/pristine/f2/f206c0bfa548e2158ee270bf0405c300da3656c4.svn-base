package com.zhisland.android.blog.setting.controller;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.ScreenTool;

/**
 * 修改密码
 */
public class ActModifyPwd extends FragBaseActivity {

    public static final int TAG_RIGHT_BTN = 505;

    FragModifyPwd fragModifyPwd;

    public static void invoke(Context context) {
        Intent intent = new Intent(context, ActModifyPwd.class);
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        getTitleBar().addBackButton();
        getTitleBar().setTitle("修改密码");
        TextView rightBtn = TitleCreator.Instance().createTextButton(this,
                "确定", R.color.color_dc);
        getTitleBar().addRightTitleButton(rightBtn, TAG_RIGHT_BTN);
        fragModifyPwd = new FragModifyPwd();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragModifyPwd);
        ft.commit();
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        super.onTitleClicked(view, tagId);
        if (tagId == TAG_RIGHT_BTN) {
            doActionModify();
        }
    }

    public void doActionModify() {
        ScreenTool.HideInput(this);
        String newPwd = fragModifyPwd.getNewPwd();
        if (newPwd != null) {
            showProgressDlg();
            ZHApis.getAAApi().modifyPwd(ActModifyPwd.this, newPwd,
                    new TaskCallback<Object>() {

                        @Override
                        public void onSuccess(Object content) {
                            showToast("修改密码成功");
                            if (ActModifyPwd.this != null && !ActModifyPwd.this.isFinishing()) {
                                ActModifyPwd.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Throwable error) {
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            hideProgressDlg();
                        }
                    });
        }

    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
