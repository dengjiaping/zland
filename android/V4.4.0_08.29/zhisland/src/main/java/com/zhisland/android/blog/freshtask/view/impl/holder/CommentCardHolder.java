package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.lib.bitmap.ImageWorkFactory;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 新手任务 ViewPager holder
 */
public class CommentCardHolder {

    @InjectView(R.id.tvCommentContent)
    public TextView tvCommentContent;

    @InjectView(R.id.tvPublisher)
    public TextView tvPublisher;

    @InjectView(R.id.ivUserFigure)
    public ImageView ivUserFigure;

    @InjectView(R.id.tvUserName)
    public TextView tvUserName;

    @InjectView(R.id.tvToUser)
    public TextView tvToUser;

    public UserComment item;
    private CommentCardHolderListener listener;

    public void fill(Context context, UserComment item) {
        this.item = item;
        tvCommentContent.setText(item.content);
        User publisher = item.publisher;
        tvPublisher.setText("- " + publisher.name + " " + publisher.combineCompanyAndPosition());
        User toUser = item.toUser;
        ImageWorkFactory.getFetcher().loadImage(toUser.figure, ivUserFigure, R.drawable.img_campaign_default);
        tvUserName.setText(toUser.getNameWithTypeGoldFire(context,false));
        tvToUser.setText(toUser.combineCompanyAndPosition());
    }

    @OnClick(R.id.rlPriceCard)
    public void onClickPriceCard() {
        if (listener != null) {
            listener.onCardClicked(item.toUser);
        }
    }


    public void setCardHolderListener(CommentCardHolderListener listener) {
        this.listener = listener;
    }
}
