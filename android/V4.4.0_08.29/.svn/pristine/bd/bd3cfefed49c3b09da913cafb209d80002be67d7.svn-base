package com.zhisland.android.blog.profile.controller.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.component.act.TitleType;


/**
 * 全部评价
 */
public class ActUserCommentList extends FragBaseActivity {

    public static final int TITLE_RIGHT_BUTTON_TAG = 111;
    public static final String INK_FROM_USER = "INK_FROM_USER";

    FragUserCommentList frag;
    private TextView rightBtn;

    public User userFrom;
    public boolean isUserSelf;

    public static void invoke(Context context, User user) {
        Intent intent = new Intent(context, ActUserCommentList.class);
        intent.putExtra(INK_FROM_USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        setSwipeBackEnable(true);
        getTitleBar().addBackButton();
        getTitleBar().setTitle("全部评价");

        userFrom = (User) getIntent().getSerializableExtra(INK_FROM_USER);
        isUserSelf = userFrom.uid == PrefUtil.Instance().getUserId();

        if (isUserSelf) {
            rightBtn = TitleCreator.Instance().createTextButton(this, "编辑",
                    R.color.color_dc);
            getTitleBar().addRightTitleButton(rightBtn, TITLE_RIGHT_BUTTON_TAG);
        }

        frag = new FragUserCommentList();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, frag);
        ft.commit();
    }

    public void setRightBtnEnable(boolean flag) {
        if (rightBtn != null) {
            rightBtn.setEnabled(flag);
            rightBtn.setTextColor(flag == true ? getResources().getColor(
                    R.color.color_dc) : getResources().getColor(
                    R.color.color_f3));
        }
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        super.onTitleClicked(view, tagId);
        if (tagId == TITLE_RIGHT_BUTTON_TAG) {
            FragUserCommentEdit.invoke(this, userFrom, frag.getDatas());
        }
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }
}
