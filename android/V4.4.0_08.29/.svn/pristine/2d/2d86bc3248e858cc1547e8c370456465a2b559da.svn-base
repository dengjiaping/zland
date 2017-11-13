package com.zhisland.android.blog.profile.controller.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.im.data.IMUser;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.ScreenTool;
import com.zhisland.lib.util.StringUtil;

/**
 * 填写评价
 */
public class ActWriteUserComment extends FragBaseActivity {

    public static final int TITLE_LEFT_BUTTON_TAG = 111;
    public static final int TITLE_RIGHT_BUTTON_TAG = 112;
    private static final String INK_FROM_USER = "INK_FROM_USER";

    FragWriteUserComment frag;
    private TextView rightBtn;
    private TextView leftBtn;

    public User userFrom;
    private CommonDialog dialog;

    public static void invoke(Context context, User user) {
        if (isShowFriendOnlyToast(user.uid)) {
            Intent intent = new Intent(context, ActWriteUserComment.class);
            intent.putExtra(INK_FROM_USER, user);
            context.startActivity(intent);
        }
    }

    /**
     * 评价时若不是好友关系，弹提示
     */
    private static final boolean isShowFriendOnlyToast(long uid) {
        IMUser user = com.zhisland.im.data.DBMgr.getHelper()
                .getUserDao().getIMUserByUid(uid);
        if (user == null || user.relation == null || !user.relation.equals(RelationConstants.BE_FOLLOWED + "")) {
            LayoutInflater inflater = LayoutInflater
                    .from(ZHApplication.APP_CONTEXT);
            View view = inflater.inflate(R.layout.toast_only_friend, null);
            Toast toast = new Toast(ZHApplication.APP_CONTEXT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
            return false;
        }
        return true;
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setSwipeBackEnable(false);
        getTitleBar().setTitle("写评价");
        userFrom = (User) getIntent().getSerializableExtra(INK_FROM_USER);

        leftBtn = TitleCreator.Instance().createTextButton(this, "取消",
                R.color.color_f2);
        getTitleBar().addLeftTitleButton(leftBtn, TITLE_LEFT_BUTTON_TAG);
        rightBtn = TitleCreator.Instance().createTextButton(this, "提交",
                R.color.color_dc);
        getTitleBar().addRightTitleButton(rightBtn, TITLE_RIGHT_BUTTON_TAG);

        frag = new FragWriteUserComment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, frag);
        ft.commit();
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        super.onTitleClicked(view, tagId);
        switch (tagId) {
            case TITLE_LEFT_BUTTON_TAG:
                back();
                break;
            case TITLE_RIGHT_BUTTON_TAG:
                // 提交评价
                frag.submit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        ScreenTool.HideInput(this);
        if (StringUtil.isNullOrEmpty(frag.getContent())) {
            finish();
        } else {
            showConfimDialog();
        }
    }

    /**
     * 发布评论取消 二次确认
     */
    private void showConfimDialog() {
        if (dialog == null) {
            dialog = new CommonDialog(this);
        }
        if (!dialog.isShowing()) {
            dialog.show();
            dialog.setTitle("确认取消发布此评论？");
            dialog.tvDlgCancel.setText("放弃取消");
            dialog.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
