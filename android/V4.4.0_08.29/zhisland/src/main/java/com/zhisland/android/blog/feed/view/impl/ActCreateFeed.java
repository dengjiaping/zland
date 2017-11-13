package com.zhisland.android.blog.feed.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.act.TitleType;

import butterknife.ButterKnife;

/**
 * 发布新鲜事
 */
public class ActCreateFeed extends FragBaseActivity {

    private final int TAG_LEFT = 100;
    private final int TAG_NEXT = 101;

    private TextView tvCancel;
    public TextView tvComplete;

    private FragCreateFeed fragCreateFeed;

    public static void invoke(Context context) {
        if (PermissionsMgr.getInstance().canPublicFeed()) {
            ZhislandApplication.trackerClickEvent(null, TrackerAlias.CLICK_EVENT_PUBLISH_START);

            Intent intent = new Intent(context, ActCreateFeed.class);
            context.startActivity(intent);
        } else {
            DialogUtil.showPermissionsDialog(context, null);
        }
    }


    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);

        ButterKnife.inject(this);
        getTitleBar().setTitle("发新鲜事");

        tvCancel = TitleCreator.Instance().createTextButton(this, "取消",
                R.color.txt_light_gray);
        tvComplete = TitleCreator.Instance().createTextButton(this, "发布",
                getResources().getColorStateList(R.color.sel_color_dc));
        tvComplete.setEnabled(false);
        getTitleBar().addLeftTitleButton(tvCancel, TAG_LEFT);
        getTitleBar().addRightTitleButton(tvComplete, TAG_NEXT);

        fragCreateFeed = new FragCreateFeed();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragCreateFeed);
        ft.commit();
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        super.onTitleClicked(view, tagId);
        switch (tagId) {
            case TAG_LEFT:
                // 取消发布
                back();
                break;
            case TAG_NEXT:
                if (fragCreateFeed != null) {
                    fragCreateFeed.onPublishClicked();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default: {
                if (fragCreateFeed != null) {
                    fragCreateFeed.onActivityResult(requestCode, resultCode, data);
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (fragCreateFeed != null) {
            fragCreateFeed.back();
        }
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}