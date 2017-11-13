package com.zhisland.android.blog.feed.view.impl;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.lib.component.act.TitleType;

/**
 * feed详情页面
 * Created by Mr.Tui on 2016/9/3.
 */
public class ActFeedDetail extends FragBaseActivity {

    public static final String INTENT_KEY_FEED = "intent_ket_feed";
    public static final String INTENT_KEY_FEED_ID = "intent_ket_feed_id";
    public static final String INTENT_KEY_SHOW_SEND_COMMENT = "intent_ket_show_send_comment";
    private static final int TAG_ID_RIGHT = 100;

    FragFeedDetail fragfeedDetail;

    public static void invoke(Context context, String feedId) {
        Intent intent = new Intent(context, ActFeedDetail.class);
        intent.putExtra(INTENT_KEY_FEED_ID, feedId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void invoke(Context context, Feed feed, boolean showSendCommentView) {
        Intent intent = new Intent(context, ActFeedDetail.class);
        intent.putExtra(INTENT_KEY_FEED, feed);
        intent.putExtra(INTENT_KEY_SHOW_SEND_COMMENT, showSendCommentView);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        getTitleBar().addBackButton();
        ImageView vRight = TitleCreator.Instance()
                .createImageButton(this, R.drawable.sel_btn_more_gray);
        getTitleBar().addRightTitleButton(vRight, TAG_ID_RIGHT);
        getTitleBar().setTitle("动态详情");
        fragfeedDetail = new FragFeedDetail();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragfeedDetail);
        ft.commit();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        if (tagId == TAG_ID_RIGHT) {
            fragfeedDetail.onMoreActionClick();
        }
        super.onTitleClicked(view, tagId);
    }

}

